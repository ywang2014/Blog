package create;

public class FactoryMethodEmail implements FactoryMethod {
    @Override
    public Sender produce() {
        return new SenderEmail();
    }
}
