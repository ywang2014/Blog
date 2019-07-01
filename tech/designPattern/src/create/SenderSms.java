package create;

public class SenderSms implements Sender {
    @Override
    public void send() {
        System.out.println("Sms send.");
    }
}
