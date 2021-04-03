package it.unibo.cautiousExplorerActors.interaction;

public interface IssOperations {
    void forward(String msg);
    void request(String msg);
    void reply(String msg);
    String requestSynch(String msg);
}
