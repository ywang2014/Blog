package conduct.observer;

public class RecordEvent implements ObserverInterface {
    private String uuid;

    @Override
    public void doSomething(ObservableAbstract observable, Object obj) {
        if (obj instanceof Ticket) {
            addRecord((Ticket) obj);
        }
    }

    private void addRecord(Ticket ticket) {
        switch (ticket.getType()) {
            case 1:
            case 2:
            case 3:
                // 客服工单类型
                break;
            case 4:
                // 反馈工单类型
                break;
            default:
                throw new IllegalArgumentException("Ticket status never go here.");
        }
    }

    public RecordEvent(String uuid) {
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
        if (obj instanceof RecordEvent) {
            RecordEvent notice = (RecordEvent) obj;
            return this.uuid.equals(notice.getUuid());
        }
        return false;
    }
}
