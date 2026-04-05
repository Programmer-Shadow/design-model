import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import pricing.DiscountContext;
import pricing.Order;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        // 启动内置 HTTP 服务。端口被占用时自动尝试下一个端口。
        HttpServer server = createServerWithFallback(8080, 10);
        // 创建策略上下文：后续所有折扣计算都通过它调度具体策略。
        DiscountContext discountContext = new DiscountContext();

        server.createContext("/", Main::handleHome);
        // 前端通过 strategy 参数选择策略，实现“同一入口，不同算法”。
        server.createContext("/api/quote", exchange -> handleQuote(exchange, discountContext));
        server.setExecutor(null);
        server.start();

        System.out.println("Server started at http://localhost:" + server.getAddress().getPort());
    }

    private static HttpServer createServerWithFallback(int basePort, int maxAttempts) throws IOException {
        for (int i = 0; i < maxAttempts; i++) {
            int port = basePort + i;
            try {
                return HttpServer.create(new InetSocketAddress(port), 0);
            } catch (BindException ignored) {
                // 当前端口被占用，继续尝试下一个端口。
            }
        }
        throw new IOException("No available port from " + basePort + " to " + (basePort + maxAttempts - 1));
    }

    private static void handleHome(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, 405, "Method Not Allowed");
            return;
        }
        sendHtml(exchange, 200, FrontendPage.HTML);
    }

    private static void handleQuote(HttpExchange exchange, DiscountContext discountContext) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, 405, "Method Not Allowed");
            return;
        }

        Map<String, String> params = parseQuery(exchange.getRequestURI());
        // strategy 是“算法选择开关”，由前端传入：normal / vip / new-user。
        String strategy = params.getOrDefault("strategy", "normal");
        int quantity = parseInt(params.get("quantity"), 1);
        double unitPrice = parseDouble(params.get("unitPrice"), 100);

        Order order = new Order(unitPrice, quantity);
        // 关键点：业务代码不关心具体折扣细节，只委托给上下文。
        double total = discountContext.calculateTotal(strategy, order);

        String json = "{"
                + "\"strategy\":\"" + escape(strategy) + "\","
                + "\"unitPrice\":" + unitPrice + ","
                + "\"quantity\":" + quantity + ","
                + "\"total\":" + round2(total)
                + "}";
        sendJson(exchange, 200, json);
    }

    private static Map<String, String> parseQuery(URI uri) {
        Map<String, String> map = new HashMap<>();
        String query = uri.getRawQuery();
        if (query == null || query.isEmpty()) {
            return map;
        }

        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            String key = decode(kv[0]);
            String value = kv.length > 1 ? decode(kv[1]) : "";
            map.put(key, value);
        }
        return map;
    }

    private static String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported", e);
        }
    }

    private static int parseInt(String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static double parseDouble(String value, double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private static String escape(String text) {
        return text.replace("\"", "\\\"");
    }

    private static void sendJson(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void sendText(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void sendHtml(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
