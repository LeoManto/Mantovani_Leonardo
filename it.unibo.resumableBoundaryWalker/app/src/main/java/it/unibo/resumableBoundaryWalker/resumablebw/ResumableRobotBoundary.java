/*
===============================================================
ClientBoundaryWebsockArilAsynch.java
Use the aril language and the support specified in the
configuration file IssProtocolConfig.txt

The business logic is defined in RobotControllerArilBoundary
that is 'message-driven'
===============================================================
*/
package it.unibo.resumableBoundaryWalker.resumablebw;
import it.unibo.resumableBoundaryWalker.annotations.ArilRobotSpec;
import it.unibo.resumableBoundaryWalker.consolegui.ConsoleGui;
import it.unibo.resumableBoundaryWalker.interaction.IssOperations;
import it.unibo.resumableBoundaryWalker.supports.IssCommSupport;
import it.unibo.resumableBoundaryWalker.supports.RobotApplicationStarter;


@ArilRobotSpec
public class ResumableRobotBoundary {
    private RobotApplInputController controller;

    //Constructor
    public ResumableRobotBoundary(IssOperations rs){
        IssCommSupport rsComm = (IssCommSupport)rs;
        controller            = new RobotApplInputController(rsComm, true, true );
        rsComm.registerObserver( controller );

        System.out.println("ResumableRobotBoundary | CREATED with rsComm=" + rsComm);
        new ConsoleGui(  controller );
    }


    public static void main(String args[]){
        try {
            System.out.println("ResumableRobotBoundary | main start n_Threads=" + Thread.activeCount());
            Object appl = RobotApplicationStarter.createInstance(ResumableRobotBoundary.class);
            System.out.println("ResumableRobotBoundary  | appl n_Threads=" + Thread.activeCount());
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}
