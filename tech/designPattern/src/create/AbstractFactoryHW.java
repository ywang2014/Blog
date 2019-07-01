package create;

/**
 * Hua Wei
 */
public class AbstractFactoryHW implements AbstractFactory {
    @Override
    public Sender produceSender() {
        return new SenderEmail();
    }

    @Override
    public SmsTemplate produceSmsTemplate() {
        return new SmsTemplateTR();
    }
}
