package conduct.observer;

public class NoticeEvent implements ObserverInterface {
    private String uuid;

    @Override
    public void doSomething(ObservableAbstract observable, Object obj) {
        if (obj instanceof Ticket) {
            noticeTicket((Ticket)obj);
        }
    }

    private void noticeTicket(Ticket ticket) {
        switch (ticket.getStatus()) {
            case 0:
                // 创建工单通知事件
                break;
            case 1:
                // 跟进工单通知事件
                break;
            case 2:
                // 处理工单通知事件
                break;
            case 3:
                // 工单结办通知事件
                break;
            default:
                throw new IllegalArgumentException("Ticket status never go here.");
        }
    }

    public NoticeEvent(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NoticeEvent) {
            NoticeEvent notice = (NoticeEvent) obj;
            return this.uuid.equals(notice.getUuid());
        }
        return false;
    }
}
