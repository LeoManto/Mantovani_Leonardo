package it.unibo.cautiousExplorerActors.consolegui;

import it.unibo.supports2021.ActorBasicJava;

import java.util.Observable;
import java.util.Observer;

public class ConsoleGuiActor extends ActorBasicJava implements Observer {

    private String[] buttonLabels = new String[] {"STOP", "RESUME"};

    public ConsoleGuiActor(){
        super("userConsole");
        GuiUtils.showSystemInfo();
        ButtonAsGui concreteButton = ButtonAsGui.createButtons("", buttonLabels);
        concreteButton.addObserver(this);
    }

    @Override
    protected void handleInput(String s) {

    }

    @Override
    public void update(Observable o, Object arg) {
        String move = arg.toString();
        String robotCmd = (move == "STOP") ? "{\"robotcmd\":\"STOP\" }" : "{\"robotcmd\":\"RESUME\" }";
        this.updateObservers(robotCmd);
    }

    public static void main(String[] args){
        new ConsoleGuiActor();
    }
}
