package conduct.observer;

import java.util.ArrayList;
import java.util.List;

public class ObservableAbstract {
    private boolean noticed = false;
    private List<ObserverInterface> observers = new ArrayList<>();

    public boolean addObservers(ObserverInterface observer) {
        for (ObserverInterface obs : this.observers) {
            if (observer.equals(obs)) {
                return false;
            }
        }
        observers.add(observer);
        return true;
    }

    public void removeObservers(ObserverInterface observer) {
        observers.remove(observer);
    }

    public void noticeObservers(Object obj) {
        if (noticed) {
            observers.forEach(x -> x.doSomething(this, obj));
        }
        noticed = false;
    }


    public boolean isNoticed() {
        return noticed;
    }

    public void setNoticed(boolean noticed) {
        this.noticed = noticed;
    }
}
