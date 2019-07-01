package create;

public class FactoryMethodLog implements FactoryMethod {
    @Override
    public Sender produce() {
        return new SenderLog();
    }
}
