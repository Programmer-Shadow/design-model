package singleton.enumtype;

/**
 * 枚举单例：最简洁、最安全的写法。
 * JVM 保证枚举实例唯一，天然防反射攻击和序列化破坏。
 * Effective Java 推荐。
 */
public enum EnumSingleton {

    INSTANCE;

    // 可以像普通类一样添加方法和字段
    public String getInfo() {
        return "EnumSingleton";
    }
}
