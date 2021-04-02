package it.unibo.cautiousExplorerActors.interaction;

public class MsgRobotUtil {
    //movetimes (used by IssAnnotationUtil.fillMap)
    public static final int wtime    = 400;
    public static final int stime    = wtime;
    public static final int ltime    = 300;
    public static final int rtime    = ltime;
    public static final int htime    = 100;

    //cril
    public static final String forwardMsg   = "{\"robotmove\":\"moveForward\", \"time\": 400}";
    public static final String backwardMsg  = "{\"robotmove\":\"moveBackward\", \"time\": 400}";
    public static final String turnLeftMsg  = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    public static final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}";
    public static final String haltMsg      = "{\"robotmove\":\"alarm\", \"time\": 100}";


    //aril
    public static final String wMsg  = "w";
    public static final String lMsg  = "l";
    public static final String rMsg  = "r";
    public static final String sMsg  = "s";
    public static final String hMsg  = "h";
}
