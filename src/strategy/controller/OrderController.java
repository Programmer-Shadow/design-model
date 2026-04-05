package strategy.controller;

import com.sun.net.httpserver.HttpExchange;
import strategy.model.Order;
import strategy.service.PricingService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

// Controller 层：解析 HTTP 请求参数，调用 Service，返回 JSON。
public class OrderController {
    private final PricingService pricingService;

    public OrderController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, 405, "Method Not Allowed");
            return;
        }

        Map<String, String> params = parseQuery(exchange.getRequestURI());
        // strategy 是"算法选择开关"，由前端传入：normal / vip / new-user。
        String strategy = params.getOrDefault("strategy", "normal");
        int quantity = parseInt(params.get("quantity"), 1);
        double unitPrice = parseDouble(params.get("unitPrice"), 100);

        Order order = new Order(unitPrice, quantity);
        // 关键点：Controller 不关心折扣细节，只委托给 Service。
        double total = pricingService.quote(strategy, order);

        String json = "{"
                + "\"strategy\":\"" + escape(strategy) + "\","
                + "\"unitPrice\":" + unitPrice + ","
                + "\"quantity\":" + quantity + ","
                + "\"total\":" + round2(total)
                + "}";
        sendJson(exchange, 200, json);
    }

    private Map<String, String> parseQuery(URI uri) {
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

    private String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported", e);
        }
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double parseDouble(String value, double defaultValue) {
        if (value == null) return defaultValue;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private String escape(String text) {
        return text.replace("\"", "\\\"");
    }

    private void sendJson(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendText(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
