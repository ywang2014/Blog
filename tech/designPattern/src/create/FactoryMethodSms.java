package create;

public class FactoryMethodSms implements FactoryMethod {
    @Override
    public Sender produce() {
        return new SenderSms();
    }
}
