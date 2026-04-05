package abstractfactory.controller;

import abstractfactory.factory.DarkThemeFactory;
import abstractfactory.factory.LightThemeFactory;
import abstractfactory.factory.UIFactory;
import abstractfactory.product.Alert;
import abstractfactory.product.Button;
import abstractfactory.product.Card;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UIController {

    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            send(exchange, 405, "text/plain; charset=utf-8", "Method Not Allowed");
            return;
        }

        Map<String, String> params = parseQuery(exchange.getRequestURI());
        String theme = params.getOrDefault("theme", "light");

        UIFactory factory;
        switch (theme) {
            case "dark": factory = new DarkThemeFactory(); break;
            default:     factory = new LightThemeFactory(); theme = "light";
        }

        Button button = factory.createButton();
        Card card = factory.createCard();
        Alert alert = factory.createAlert();

        String json = "{"
                + "\"theme\":\"" + theme + "\","
                + "\"factoryClass\":\"" + factory.factoryName() + "\","
                + "\"components\":{"
                + "\"button\":{\"class\":\"" + button.name() + "\",\"html\":\"" + escapeJson(button.render()) + "\"},"
                + "\"card\":{\"class\":\"" + card.name() + "\",\"html\":\"" + escapeJson(card.render()) + "\"},"
                + "\"alert\":{\"class\":\"" + alert.name() + "\",\"html\":\"" + escapeJson(alert.render()) + "\"}"
                + "},"
                + "\"steps\":["
                + "\"" + factory.factoryName() + ".createButton() → " + button.name() + "\","
                + "\"" + factory.factoryName() + ".createCard()   → " + card.name() + "\","
                + "\"" + factory.factoryName() + ".createAlert()  → " + alert.name() + "\""
                + "]"
                + "}";

        send(exchange, 200, "application/json; charset=utf-8", json);
    }

    private String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("'", "\\'");
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
