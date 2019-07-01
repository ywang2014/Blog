package create;

/**
 * 静态工厂方法模式
 */
public class StaticSimpleFactory {
    public static Sender produce(SendType type) {
        switch (type) {
            case EMAIL:
                return new SenderEmail();
            case SMS:
                return new SenderSms();
            case LOG:
                return new SenderLog();
            default:
                throw new IllegalArgumentException("Send type never go here.");
        }
    }

    public enum SendType {
        EMAIL,
        SMS,
        LOG,
        ;
    }

    // Type 2 create sender
    public static Sender produceEmailSender() {
        return new SenderEmail();
    }

    public static Sender produceSmsSender() {
        return new SenderSms();
    }

    public static Sender produceLogSender() {
        return new SenderLog();
    }
}
