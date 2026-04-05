package chain.controller;

import chain.handler.*;
import chain.model.HandleResult;
import chain.model.OrderRequest;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChainController {

    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            send(exchange, 405, "text/plain; charset=utf-8", "Method Not Allowed");
            return;
        }

        Map<String, String> params = parseQuery(exchange.getRequestURI());
        String product = params.getOrDefault("product", "");
        int quantity = parseInt(params.get("quantity"), 0);
        double unitPrice = parseDouble(params.get("unitPrice"), 0);

        OrderRequest request = new OrderRequest(product, quantity, unitPrice);

        // 组装责任链：参数校验 → 库存校验 → 风控校验 → 审批
        OrderHandler head = new ParamCheckHandler();
        head.setNext(new StockCheckHandler())
            .setNext(new RiskCheckHandler())
            .setNext(new ApprovalHandler());

        List<HandleResult> trace = new ArrayList<>();
        HandleResult finalResult = head.handle(request, trace);

        // 构建 JSON 响应
        StringBuilder stepsJson = new StringBuilder("[");
        for (int i = 0; i < trace.size(); i++) {
            HandleResult r = trace.get(i);
            stepsJson.append("{")
                    .append("\"handler\":\"").append(r.getHandler()).append("\",")
                    .append("\"passed\":").append(r.isPassed()).append(",")
                    .append("\"message\":\"").append(escapeJson(r.getMessage())).append("\"")
                    .append("}");
            if (i < trace.size() - 1) stepsJson.append(",");
        }
        stepsJson.append("]");

        String json = "{"
                + "\"product\":\"" + escapeJson(product) + "\","
                + "\"quantity\":" + quantity + ","
                + "\"unitPrice\":" + unitPrice + ","
                + "\"totalAmount\":" + request.totalAmount() + ","
                + "\"finalPassed\":" + finalResult.isPassed() + ","
                + "\"finalMessage\":\"" + escapeJson(finalResult.getMessage()) + "\","
                + "\"chain\":" + stepsJson
                + "}";

        send(exchange, 200, "application/json; charset=utf-8", json);
    }

    private String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null) return defaultValue;
        try { return Integer.parseInt(value); }
        catch (NumberFormatException e) { return defaultValue; }
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
            String key = decode(kv[0]);
            String value = kv.length > 1 ? decode(kv[1]) : "";
            map.put(key, value);
        }
        return map;
    }

    private String decode(String value) {
        try { return URLDecoder.decode(value, StandardCharsets.UTF_8.name()); }
        catch (UnsupportedEncodingException e) { throw new IllegalStateException(e); }
    }
}
