package it.unibo.cautiousExplorerActors.supports;

import it.unibo.cautiousExplorerActors.interaction.IssOperations;
import it.unibo.cautiousExplorerActors.interaction.IssObserver;

public interface IssCommSupport extends IssOperations {
    void registerObserver( IssObserver obs );
    void removeObserver( IssObserver obs );
    void close();
}
