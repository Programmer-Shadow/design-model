package singleton.dcl;

/**
 * 双重检查锁（Double-Checked Locking）：
 * 第一次 check 避免不必要的加锁，第二次 check 防止重复创建。
 * volatile 禁止指令重排序，防止拿到未初始化完成的对象。
 */
public class DclSingleton {

    private static volatile DclSingleton instance;

    private DclSingleton() {}

    public static DclSingleton getInstance() {
        if (instance == null) {                    // 第一次检查（无锁）
            synchronized (DclSingleton.class) {
                if (instance == null) {            // 第二次检查（有锁）
                    instance = new DclSingleton();
                }
            }
        }
        return instance;
    }
}
