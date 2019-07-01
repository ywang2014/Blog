package create;

public class FactoryDemo {
    public static void main(String[] args) {
        // Type 1 simple factory
        SimpleFactory factory = new SimpleFactory();
        Sender sender = factory.produce(SimpleFactory.SendType.EMAIL);
        sender.send();
        // Type 1.2
        factory.produceSmsSender().send();

        // Type 2 static factory
        StaticSimpleFactory.produce(StaticSimpleFactory.SendType.EMAIL).send();
        StaticSimpleFactory.produceLogSender().send();

        // Type 3 factory method
        FactoryMethod factory3 = new FactoryMethodEmail();
        Sender sender3 = factory3.produce();
        sender.send();

        // Type 4 singleton
        SingletonDemo.getInstance().method();

        // Type 5 Builder
        Car car = new Car.CarBuilder()
                .addType("FaLaLi")
                .addColor("Black")
                .addRegion("China")
                .addLength(6.5)
                .addWidth(3.0)
                .addWeight(1.5)
                .addPrice(30.5)
                .build();
    }
}
