package singleton.enumtype;

/**
 * 枚举单例：最简洁、最安全的写法。
 * JVM 保证枚举实例唯一，天然防反射攻击和序列化破坏。
 * Effective Java 推荐。
 *
 * 实际场景：全局配置管理器，应用启动时加载一次配置，全局共享。
 */
public enum EnumSingleton {

    INSTANCE;

    private String dbUrl;
    private int maxRetry;

    // 枚举构造函数，JVM 保证只执行一次
    EnumSingleton() {
        this.dbUrl = "jdbc:mysql://localhost:3306/app";
        this.maxRetry = 3;
    }

    public String getDbUrl() { return dbUrl; }
    public int getMaxRetry() { return maxRetry; }

    public String getInfo() {
        return "EnumSingleton[dbUrl=" + dbUrl + ", maxRetry=" + maxRetry + "]";
    }
}
