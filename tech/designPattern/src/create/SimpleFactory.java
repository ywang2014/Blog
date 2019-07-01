package create;

public class SimpleFactory {
    public Sender produce(SendType type) {
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
    public Sender produceEmailSender() {
        return new SenderEmail();
    }

    public Sender produceSmsSender() {
        return new SenderSms();
    }

    public Sender produceLogSender() {
        return new SenderLog();
    }
}


