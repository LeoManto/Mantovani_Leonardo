package it.unibo.cautiousExplorerActors.cautiousExplorerWithActor;

import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.IssWsHttpJavaSupport;
import org.json.JSONObject;

public class CautiousExplorerActor extends ActorBasicJava {

    final String forwardMsg   = "{\"robotmove\":\"moveForward\", \"time\": 350}";
    final String backwardMsg  = "{\"robotmove\":\"moveBackward\", \"time\": 350}";
    final String turnLeftMsg  = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}";
    final String haltMsg      = "{\"robotmove\":\"alarm\", \"time\": 100}";

    private enum State {start, walking, obstacle, end};
    private IssWsHttpJavaSupport support;
    private State curState = State.start;
    private int stepNum = 1;
    private RobotMovesInfo moves = new RobotMovesInfo(true);

    private boolean tripStopped = true;

    public CautiousExplorerActor(String name, IssWsHttpJavaSupport support){
        super(name);
        this.support = support;
    }

    protected void fsm(String move, String endMove){
        System.out.println( myname + " | fsm state=" + curState + " tripStopped=" + tripStopped  + " stepNum=" + stepNum + " move=" + move + " endmove=" + endMove);

        switch(curState){
            case start: {
                if(move.equals("resume")){
                    moves.showRobotMovesRepresentation();
                    doStep();
                    curState = State.walking;
                    break;
                }
            }
            case walking: {
                if(move.equals("resume")){
                    moves.updateMovesRep("w");
                }else if(move.equals("moveForward") && endMove.equals("true")){
                    moves.updateMovesRep("w");

                    if(!tripStopped){
                        doStep();
                    }else{
                        System.out.println("Please resume...");
                    }
                }else if(move.equals("moveForward") && endMove.equals("false")){
                    curState = State.obstacle;
                    turnLeft();
                }else{
                    System.out.println("IGNORE answer of turnLeft");
                }
                break;
            }
            case obstacle: {
                if(stepNum < 4){
                    stepNum++;
                    moves.updateMovesRep("l");
                    moves.showRobotMovesRepresentation();

                    if(!tripStopped){
                        curState = State.walking;
                        doStep();
                    }else{
                        System.out.println("Please resume...");
                    }
                }else{
                    curState = State.end;
                    turnLeft();
                }
                break;
            }
            case end: {
                if(move.equals("turnleft")){
                    System.out.println("BOUNDARY WALK IS ENDED");
                    moves.showRobotMovesRepresentation();
                    turnRight();
                }else{
                    System.out.println("RESET");
                    stepNum = 1;
                    curState = State.start;
                    tripStopped = true;
                    moves.cleanMovesRepresentation();
                    moves.showRobotMovesRepresentation();
                }
                break;
            }

            default : {
                System.out.println("Error - curState = " + curState);
            }
        }
    }

//==================================================================================================================

    protected void doStep(){
        support.forward(forwardMsg);
        delay(1000);
    }

    protected void turnLeft(){
        support.forward(turnLeftMsg);
        delay(500);
    }

    protected void turnRight(){
        support.forward(turnRightMsg);
        delay(500);
    }

//==================================================================================================================

    @Override
    protected void handleInput(String msg) {
        msgDriven(new JSONObject(msg));
    }

    private void msgDriven(JSONObject infoJson) {
        if( infoJson.has("endmove") )        fsm(infoJson.getString("move"), infoJson.getString("endmove"));
        else if( infoJson.has("sonarName") ) handleSonar(infoJson);
        else if( infoJson.has("collision") ) handleCollision(infoJson);
        else if( infoJson.has("robotcmd") )  handleRobotCmd(infoJson);
    }

    private void handleCollision(JSONObject infoJson) {
        //TODO
    }

    private void handleRobotCmd(JSONObject robotCmd) {
        String cmd = (String) robotCmd.get("robotCmd");
        System.out.println("===================================================="    );
        System.out.println("ResumableBoundaryWalkerActor | handleRobotCmd cmd=" + cmd  );
        System.out.println("===================================================="    );
        if(cmd.equals("STOP")){
            tripStopped = true;
        }
        if(cmd.equals("RESUME")){
            tripStopped = false;
            fsm("resume", "");
        }
    }

    private void handleSonar(JSONObject sonarInfo) {
        String sonaName = (String) sonarInfo.get("sonarName");
        int distance = (Integer) sonarInfo.get("distance");
    }

}
