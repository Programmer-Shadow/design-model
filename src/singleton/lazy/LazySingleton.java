package singleton.lazy;

/**
 * 懒汉式 + synchronized：第一次调用时才创建实例。
 * 线程安全，但每次 getInstance() 都加锁，性能差。
 */
public class LazySingleton {

    private static LazySingleton instance;

    private LazySingleton() {}

    public static synchronized LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
