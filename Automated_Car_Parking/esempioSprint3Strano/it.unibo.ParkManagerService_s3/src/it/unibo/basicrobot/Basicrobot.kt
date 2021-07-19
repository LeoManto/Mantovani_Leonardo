/* Generated by AN DISI Unibo */ 
package it.unibo.basicrobot

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Basicrobot ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
		  var StepTime      = 0L
		  var StartTime     = 0L     
		  var Duration      = 0L  
		  var RobotType     = "" 
		  var CurrentMove   = "moveUnknown"
		  
		 
		  val mapname     = "parkingMap" 		 
		  var Myself      = myself  
		  var CurrentPlannedMove = ""
		  var CurPath	= ""
		  var CurSlot   = ""
		  val planner = `it.unibo`.utils.CoordinatesKb
		
		
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("basicrobot | START")
						unibo.robot.robotSupport.create(myself ,"basicrobotConfig.json" )
						 RobotType = unibo.robot.robotSupport.robotKind  
						itunibo.planner.plannerUtil.initAI(  )
						itunibo.planner.plannerUtil.loadRoomMap( "$mapname"  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						unibo.robot.robotSupport.move( "h"  )
						delay(1000) 
						unibo.robot.robotSupport.move( "l"  )
						unibo.robot.robotSupport.move( "r"  )
						discardMessages = false
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						pathexecutil.register( Myself  )
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						println("basicrobot  | waiting .................. ")
					}
					 transition( edgeName="goto",targetState="path", cond=doswitch() )
				}	 
				state("path") { //this:State
					action { //it:State
						planner.getPathPlan( "indoor"  )
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitch() )
				}	 
				state("execPlannedMoves") { //this:State
					action { //it:State
						delay(300) 
						  CurrentPlannedMove = itunibo.planner.plannerUtil.getNextPlannedMove()  
						println("+++++++++++++++++++++++++++++++ $CurrentPlannedMove")
					}
					 transition( edgeName="goto",targetState="doMove", cond=doswitchGuarded({ CurrentPlannedMove.length>0  
					}) )
					transition( edgeName="goto",targetState="halt", cond=doswitchGuarded({! ( CurrentPlannedMove.length>0  
					) }) )
				}	 
				state("halt") { //this:State
					action { //it:State
						println("path finished")
					}
				}	 
				state("doMove") { //this:State
					action { //it:State
						StepTime =  
						unibo.robot.robotSupport.move( "$CurrentPlannedMove"  )
						stateTimer = TimerActor("timer_doMove", 
							scope, context!!, "local_tout_basicrobot_doMove", StepTime )
					}
					 transition(edgeName="t00",targetState="stepDone",cond=whenTimeout("local_tout_basicrobot_doMove"))   
				}	 
				state("stepDone") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if (CurrentPlannedMove == "w") { 
						unibo.robot.robotSupport.move( "h"  )
						} 
						itunibo.planner.plannerUtil.updateMap( "$CurrentPlannedMove"  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						stateTimer = TimerActor("timer_stepDone", 
							scope, context!!, "local_tout_basicrobot_stepDone", 300.toLong() )
					}
					 transition(edgeName="t01",targetState="execPlannedMoves",cond=whenTimeout("local_tout_basicrobot_stepDone"))   
				}	 
				state("turnMove") { //this:State
					action { //it:State
						if(  CurrentPlannedMove == "l" || CurrentPlannedMove == "r"   
						 ){unibo.robot.robotSupport.move( "$CurrentPlannedMove"  )
						}
					}
					 transition(edgeName="t02",targetState="rotationDone",cond=whenDispatch("moveok"))
				}	 
				state("rotationDone") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.updateMap( "$CurrentPlannedMove"  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						stateTimer = TimerActor("timer_rotationDone", 
							scope, context!!, "local_tout_basicrobot_rotationDone", 300.toLong() )
					}
					 transition(edgeName="t03",targetState="execPlannedMoves",cond=whenTimeout("local_tout_basicrobot_rotationDone"))   
				}	 
			}
		}
}
