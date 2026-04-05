package simplefactory.controller;

import com.sun.net.httpserver.HttpExchange;
import simplefactory.factory.ShapeFactory;
import simplefactory.model.Shape;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ShapeController {

    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            send(exchange, 405, "text/plain; charset=utf-8", "Method Not Allowed");
            return;
        }

        Map<String, String> params = parseQuery(exchange.getRequestURI());
        String type = params.getOrDefault("type", "circle");
        double param1 = parseDouble(params.get("param1"), 5);
        double param2 = parseDouble(params.get("param2"), 3);

        try {
            Shape shape = ShapeFactory.create(type, param1, param2);
            double area = Math.round(shape.area() * 100.0) / 100.0;

            String json = "{"
                    + "\"type\":\"" + type + "\","
                    + "\"className\":\"" + shape.name() + "\","
                    + "\"describe\":\"" + escapeJson(shape.describe()) + "\","
                    + "\"area\":" + area + ","
                    + "\"factoryTrace\":\"ShapeFactory.create(\\\"" + type + "\\\") → new " + shape.name() + "(...)\""
                    + "}";
            send(exchange, 200, "application/json; charset=utf-8", json);
        } catch (IllegalArgumentException e) {
            send(exchange, 400, "text/plain; charset=utf-8", e.getMessage());
        }
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
