package create;

/**
 * 抽象工厂
 *      一次可以生产多个产品 -- 真正的工厂
 */
public interface AbstractFactory {
    Sender produceSender();

    SmsTemplate produceSmsTemplate();
}
