package create;

/**
 * San Xing
 */
public class AbstractFactorySX implements AbstractFactory {
    @Override
    public Sender produceSender() {
        return new SenderLog();
    }

    @Override
    public SmsTemplate produceSmsTemplate() {
        return new SmsTemplateCL();
    }
}
