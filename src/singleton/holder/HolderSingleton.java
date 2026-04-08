package singleton.holder;

/**
 * 静态内部类：利用 JVM 类加载机制实现懒加载 + 线程安全。
 * Holder 类只有在 getInstance() 被调用时才会被加载，触发 INSTANCE 初始化。
 * 推荐写法。
 */
public class HolderSingleton {

    private HolderSingleton() {}

    private static class Holder {
        private static final HolderSingleton INSTANCE = new HolderSingleton();
    }

    public static HolderSingleton getInstance() {
        return Holder.INSTANCE;
    }
}
