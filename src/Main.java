import abstractfactory.controller.UIController;
import chain.controller.ChainController;
import singleton.controller.SingletonController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import factorymethod.controller.PaymentController;
import simplefactory.controller.ShapeController;
import strategy.controller.OrderController;
import strategy.dao.OrderDAO;
import strategy.service.PricingService;
import template.controller.ReportController;
import template.dao.ReportDAO;

import java.io.IOException;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpServer server = createServerWithFallback(8080, 10);

        OrderController orderController = new OrderController(new PricingService(new OrderDAO()));
        ReportController reportController = new ReportController(new ReportDAO());
        ShapeController shapeController = new ShapeController();
        PaymentController paymentController = new PaymentController();
        UIController uiController = new UIController();
        ChainController chainController = new ChainController();
        SingletonController singletonController = new SingletonController();

        // 策略模式（HTML 从文件读取）
        server.createContext("/", exchange -> serveHtmlFile(exchange, "static/strategy.html"));
        server.createContext("/api/quote", orderController::handle);

        // 模板方法模式（HTML 从文件读取）
        server.createContext("/template", exchange -> serveHtmlFile(exchange, "static/template.html"));
        server.createContext("/api/export", reportController::handle);

        // 简单工厂模式（HTML 从文件读取）
        server.createContext("/simple-factory", exchange -> serveHtmlFile(exchange, "static/simple-factory.html"));
        server.createContext("/api/shape", shapeController::handle);

        // 工厂方法模式（HTML 从文件读取）
        server.createContext("/factory-method", exchange -> serveHtmlFile(exchange, "static/factory-method.html"));
        server.createContext("/api/payment", paymentController::handle);

        // 抽象工厂模式（HTML 从文件读取）
        server.createContext("/abstract-factory", exchange -> serveHtmlFile(exchange, "static/abstract-factory.html"));
        server.createContext("/api/ui", uiController::handle);

        // 责任链模式（HTML 从文件读取）
        server.createContext("/chain", exchange -> serveHtmlFile(exchange, "static/chain.html"));
        server.createContext("/api/order-check", chainController::handle);

        // 单例模式（HTML 从文件读取）
        server.createContext("/singleton", exchange -> serveHtmlFile(exchange, "static/singleton.html"));
        server.createContext("/api/singleton", singletonController::handle);

        server.setExecutor(null);
        server.start();

        System.out.println("Server started at http://localhost:" + server.getAddress().getPort());
    }

    private static void serveHtmlFile(HttpExchange exchange, String filePath) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, 405, "Method Not Allowed");
            return;
        }
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }

    private static void sendText(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }

    private static HttpServer createServerWithFallback(int basePort, int maxAttempts) throws IOException {
        for (int i = 0; i < maxAttempts; i++) {
            int port = basePort + i;
            try {
                return HttpServer.create(new InetSocketAddress(port), 0);
            } catch (BindException ignored) {
            }
        }
        throw new IOException("No available port from " + basePort + " to " + (basePort + maxAttempts - 1));
    }
}
