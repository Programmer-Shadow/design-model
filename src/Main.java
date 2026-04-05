import com.sun.net.httpserver.HttpServer;
import strategy.controller.OrderController;
import strategy.dao.OrderDAO;
import strategy.service.PricingService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpServer server = createServerWithFallback(8080, 10);

        OrderController orderController = new OrderController(new PricingService(new OrderDAO()));

        server.createContext("/", exchange -> {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                byte[] bytes = "Method Not Allowed".getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
                exchange.sendResponseHeaders(405, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
                return;
            }
            byte[] bytes = FrontendPage.HTML.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
        });
        server.createContext("/api/quote", orderController::handle);
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
}
