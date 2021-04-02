package it.unibo.cautiousExplorerActors.supports;

public class AnswerAvailable {

    private String answer = null;

    public synchronized void put(String info, String move) {
        answer = info;
        notify();
    }

    public synchronized String get(){
        while(answer == null){
            try{
                wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            finally { }
        }
        String myAnswer = answer;
        answer = null;
        return myAnswer;
    }

}
