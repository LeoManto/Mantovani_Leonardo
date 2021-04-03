package it.unibo.cautiousExplorerActors.supports;

import it.unibo.cautiousExplorerActors.annotations.IssAnnotationUtil;
import it.unibo.cautiousExplorerActors.interaction.IssObserver;
import it.unibo.cautiousExplorerActors.interaction.MsgRobotUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class IssArilRobotSupport implements IssCommSupport{

    private IssCommSupport support;
    private static HashMap<String, Integer> timeMap = new HashMap<String, Integer>();

    public IssArilRobotSupport(Object supportedObj, IssCommSupport support){
        this.support = support;
        IssAnnotationUtil.getMoveTimes(supportedObj, timeMap);
        timeMap.forEach((k,v) -> System.out.println("" + k + ":" + v));
    }

    public IssArilRobotSupport(String robotConfigFile, IssCommSupport support){
        this.support = support;

        if(IssAnnotationUtil.checkRobotConfigFile(robotConfigFile, timeMap)){
            timeMap.forEach((k,v) -> System.out.println("Move config" + k + ":" + v));
        }else{
            timeMap.put("h", MsgRobotUtil.htime );
            timeMap.put("l", MsgRobotUtil.ltime );
            timeMap.put("r", MsgRobotUtil.rtime );
            timeMap.put("w", MsgRobotUtil.wtime );
            timeMap.put("s", MsgRobotUtil.stime );
            timeMap.forEach(  ( k,v ) -> System.out.println("move default "+k+":"+v));
        }
    }

    protected String translate(String arilMove){
        switch(arilMove.trim()){
            case "h" : return "{\"robotmove\":\"alarm\", \"time\": "+ timeMap.get("h")+"}";
            case "w" : return "{\"robotmove\":\"moveForward\", \"time\": "+ timeMap.get("w")+"}";
            case "s" : return "{\"robotmove\":\"moveBackward\", \"time\": "+ timeMap.get("s")+"}";
            case "l" : return "{\"robotmove\":\"turnLeft\", \"time\": "+ timeMap.get("l")   + "}";
            case "r" : return "{\"robotmove\":\"turnRight\", \"time\":"+ timeMap.get("r")   + "}";
            default  : return "{\"robotmove\":\"alarm\", \"time\": "+ timeMap.get("h")+"}" ; //to avoid exceptions
        }
    }

//====================================================================================================

    @Override
    public void registerObserver(IssObserver obs) {
        support.registerObserver(obs);
    }

    @Override
    public void removeObserver(IssObserver obs) {
        support.removeObserver(obs);
    }

    @Override
    public void close() {
        try{
            support.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

//====================================================================================================

    @Override
    public void forward(@NotNull String move) {
        support.forward(translate(move));
    }

    @Override
    public void reply(@NotNull String msg) {

    }

    @Override
    public void request(@NotNull String move) {
        support.request(translate(move));
    }

    @NotNull
    @Override
    public String requestSynch(@NotNull String move) {
        return support.requestSynch(translate(move));
    }
}
