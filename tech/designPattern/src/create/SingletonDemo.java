package create;

/**
 * 单例模式
 *      volatile增加可见性，防止指令重排列
 *          在Java指令中创建对象和赋值操作是分开进行的，
 *          也就是说instance = new SingletonDemo();语句是分两步执行的.
 *
 */
public class SingletonDemo {

    private static volatile SingletonDemo instance;

    private SingletonDemo() {
        // default constructor
    }

    public static SingletonDemo newInstance() {
        if (instance == null) {
            synchronized (instance) {
                if (instance == null) {
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }

    public void method() {
        System.out.println("Singleton can do many things.");
    }

    // 完美单例模式
    private static class SingletonFactory {
        private static SingletonDemo instance = new SingletonDemo();
    }

    public static SingletonDemo getInstance() {
        return SingletonFactory.instance;
    }
}
