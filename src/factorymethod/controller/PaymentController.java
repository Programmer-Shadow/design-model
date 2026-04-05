package factorymethod.controller;

import com.sun.net.httpserver.HttpExchange;
import factorymethod.factory.AlipayFactory;
import factorymethod.factory.CreditCardFactory;
import factorymethod.factory.PaymentFactory;
import factorymethod.factory.WechatPayFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentController {

    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            send(exchange, 405, "text/plain; charset=utf-8", "Method Not Allowed");
            return;
        }

        Map<String, String> params = parseQuery(exchange.getRequestURI());
        String method = params.getOrDefault("method", "alipay");
        double amount = parseDouble(params.get("amount"), 100);

        PaymentFactory factory;
        switch (method) {
            case "wechat":     factory = new WechatPayFactory();  break;
            case "creditcard": factory = new CreditCardFactory(); break;
            default:           factory = new AlipayFactory();     method = "alipay";
        }

        String result = factory.processPayment(amount);

        String json = "{"
                + "\"method\":\"" + method + "\","
                + "\"amount\":" + amount + ","
                + "\"factoryClass\":\"" + factory.factoryName() + "\","
                + "\"processorClass\":\"" + factory.getProcessorName() + "\","
                + "\"result\":\"" + escapeJson(result) + "\","
                + "\"steps\":" + buildStepsJson(factory.getLastSteps())
                + "}";
        send(exchange, 200, "application/json; charset=utf-8", json);
    }

    private String buildStepsJson(List<String> steps) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < steps.size(); i++) {
            sb.append("\"").append(escapeJson(steps.get(i))).append("\"");
            if (i < steps.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private double parseDouble(String value, double defaultValue) {
        if (value == null) return defaultValue;
        try { return Double.parseDouble(value); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    private void send(HttpExchange exchange, int status, String contentType, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }

    private Map<String, String> parseQuery(URI uri) {
        Map<String, String> map = new HashMap<>();
        String query = uri.getRawQuery();
        if (query == null || query.isEmpty()) return map;
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            map.put(kv[0], kv.length > 1 ? kv[1] : "");
        }
        return map;
    }
}
