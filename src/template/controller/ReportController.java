package template.controller;

import com.sun.net.httpserver.HttpExchange;
import template.dao.ReportDAO;
import template.exporter.BigOrderCsvExporter;
import template.exporter.CsvReportExporter;
import template.exporter.JsonReportExporter;
import template.exporter.ReportExporter;
import template.exporter.XmlReportExporter;
import template.model.ReportData;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportController {
    private final ReportDAO reportDAO;

    public ReportController(ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }

    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            send(exchange, 405, "text/plain; charset=utf-8", "Method Not Allowed");
            return;
        }

        Map<String, String> params = parseQuery(exchange.getRequestURI());
        String format = params.getOrDefault("format", "json");

        ReportExporter exporter;
        switch (format) {
            case "csv":      exporter = new CsvReportExporter();     break;
            case "csv-big":  exporter = new BigOrderCsvExporter();   break;
            case "xml":      exporter = new XmlReportExporter();     break;
            default:         exporter = new JsonReportExporter();     format = "json";
        }

        List<ReportData> data = reportDAO.findAll();
        String output = exporter.export(data);

        // 返回 JSON 包装：把步骤日志和导出结果一起返回给前端
        String stepsJson = buildStepsJson(exporter.getLastSteps());
        String json = "{"
                + "\"format\":\"" + format + "\","
                + "\"exporterClass\":\"" + exporter.exporterName() + "\","
                + "\"steps\":" + stepsJson + ","
                + "\"output\":\"" + escapeJson(output) + "\""
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
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private void send(HttpExchange exchange, int status, String contentType, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
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
