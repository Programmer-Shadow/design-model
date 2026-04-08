package singleton.controller;

import com.sun.net.httpserver.HttpExchange;
import singleton.dcl.DclSingleton;
import singleton.eager.EagerSingleton;
import singleton.enumtype.EnumSingleton;
import singleton.holder.HolderSingleton;
import singleton.lazy.LazySingleton;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SingletonController {

    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            send(exchange, 405, "Method Not Allowed");
            return;
        }

        Map<String, String> params = parseQuery(exchange.getRequestURI());
        String type = params.getOrDefault("type", "eager");

        String className;
        String desc;
        String keyPoint;
        int hash1;
        int hash2;

        switch (type) {
            case "lazy":
                hash1 = LazySingleton.getInstance().hashCode();
                hash2 = LazySingleton.getInstance().hashCode();
                className = "LazySingleton";
                desc = "懒汉式 + synchronized：第一次调用时才创建，每次加锁";
                keyPoint = "整个方法加 synchronized，简单但性能差";
                break;
            case "dcl":
                hash1 = DclSingleton.getInstance().hashCode();
                hash2 = DclSingleton.getInstance().hashCode();
                className = "DclSingleton";
                desc = "双重检查锁：两次 null 检查 + volatile + synchronized";
                keyPoint = "volatile 禁止指令重排序，防止拿到半初始化对象";
                break;
            case "holder":
                hash1 = HolderSingleton.getInstance().hashCode();
                hash2 = HolderSingleton.getInstance().hashCode();
                className = "HolderSingleton";
                desc = "静态内部类：JVM 类加载机制保证懒加载 + 线程安全";
                keyPoint = "Holder 类只在 getInstance() 首次调用时加载";
                break;
            case "enum":
                hash1 = EnumSingleton.INSTANCE.hashCode();
                hash2 = EnumSingleton.INSTANCE.hashCode();
                className = "EnumSingleton";
                desc = "枚举单例：最安全，天然防反射和序列化攻击 " + EnumSingleton.INSTANCE.getInfo();
                keyPoint = "JVM 保证枚举实例全局唯一，Effective Java 推荐";
                break;
            default:
                hash1 = EagerSingleton.getInstance().hashCode();
                hash2 = EagerSingleton.getInstance().hashCode();
                className = "EagerSingleton";
                desc = "饿汉式：类加载时就创建实例，简单可靠";
                keyPoint = "static final 保证线程安全，但不支持懒加载";
                type = "eager";
        }

        String json = "{"
                + "\"type\":\"" + type + "\","
                + "\"className\":\"" + className + "\","
                + "\"hashCode1\":" + hash1 + ","
                + "\"hashCode2\":" + hash2 + ","
                + "\"isSame\":" + (hash1 == hash2) + ","
                + "\"description\":\"" + escapeJson(desc) + "\","
                + "\"keyPoint\":\"" + escapeJson(keyPoint) + "\""
                + "}";

        send(exchange, 200, json);
    }

    private String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void send(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
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
