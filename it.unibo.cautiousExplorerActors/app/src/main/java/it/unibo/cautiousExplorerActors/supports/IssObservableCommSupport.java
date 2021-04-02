package it.unibo.cautiousExplorerActors.supports;




import it.unibo.cautiousExplorerActors.interaction.IssObserver;
import org.json.JSONObject;

import java.util.Vector;

public abstract class IssObservableCommSupport implements IssCommSupport{

    protected Vector<IssObserver> observers = new Vector<IssObserver>();

    protected void updateObservers(JSONObject jsonObj){
        observers.forEach(v ->  {
            v.handleInfo(jsonObj);
        });
    }

    public void registerObserver(IssObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(IssObserver obs) {
        observers.remove(obs);
    }

    @Override
    public void close() {

    }


}
