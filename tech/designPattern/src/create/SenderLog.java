package create;

public class SenderLog implements Sender {
    @Override
    public void send() {
        System.out.println("Log send.");
    }
}
