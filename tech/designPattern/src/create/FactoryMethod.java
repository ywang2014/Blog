package create;

/**
 * 工厂方法
 *      一次只能生产一个产品 -- 类似于生产线
 */
public interface FactoryMethod {
    Sender produce();
}
