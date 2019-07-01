package conduct.observer;

import java.util.ArrayList;
import java.util.List;

public class TicketTransfer extends ObservableAbstract {
    private List<Ticket> tickets;

    public TicketTransfer() {
        this.tickets = new ArrayList<>();
        // 注册监听者
        super.addObservers(new NoticeEvent("ticket"));
        super.addObservers(new RecordEvent("ticket"));
    }

    public void addTicket(Ticket ticket) {
        super.setNoticed(true);
        this.tickets.add(ticket);
        super.noticeObservers(ticket);
    }

    public boolean updateTicket(Ticket ticket) {
        for (Ticket t : this.tickets) {
            if (t.getId().equals(ticket.getId())) {
                if (t.getStatus() != ticket.getStatus()) {
                    super.setNoticed(true);
                    t.setStatus(ticket.getStatus());
                    super.noticeObservers(ticket);
                    return true;
                }
            }
        }
        return false;
    }
}
