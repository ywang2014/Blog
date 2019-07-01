package create;

public class SenderEmail implements Sender {
    @Override
    public void send(){
        System.out.println("Email send.");
    }
}
