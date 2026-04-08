package singleton.eager;

/**
 * 饿汉式：类加载时就创建实例。
 * 线程安全（JVM 类加载机制保证），但不支持懒加载。
 */
public class EagerSingleton {

    private static final EagerSingleton INSTANCE = new EagerSingleton();

    private EagerSingleton() {}

    public static EagerSingleton getInstance() {
        return INSTANCE;
    }
}
