/* Generated by AN DISI Unibo */ 
package it.unibo.trolley

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Trolley ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
		val mapname     = "parkingMap" 		 
		var Myself      = myself  
		var CurrentPlannedMove = ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						unibo.robot.robotSupport.create(myself ,"basicrobotConfig.json" )
						itunibo.planner.plannerUtil.initAI(  )
						println("&&&  trolley loads the parking map from the given file ...")
						itunibo.planner.plannerUtil.loadRoomMap( "$mapname"  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						pathexecutil.register( Myself  )
					}
					 transition( edgeName="goto",targetState="moveToIndoor", cond=doswitch() )
				}	 
				state("setParkingArea") { //this:State
					action { //it:State
					}
				}	 
				state("moveToIndoor") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.planForGoal( "2", "2"  )
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitch() )
				}	 
				state("execPlannedMoves") { //this:State
					action { //it:State
						delay(300) 
						  CurrentPlannedMove = itunibo.planner.plannerUtil.getNextPlannedMove()  
						println("+++++++++++++++++++++++++++++++ $CurrentPlannedMove")
					}
					 transition( edgeName="goto",targetState="doMove", cond=doswitch() )
				}	 
				state("doMove") { //this:State
					action { //it:State
					}
					 transition( edgeName="goto",targetState="wMove", cond=doswitchGuarded({ CurrentPlannedMove == "w"  
					}) )
					transition( edgeName="goto",targetState="turnMove", cond=doswitchGuarded({! ( CurrentPlannedMove == "w"  
					) }) )
				}	 
				state("wMove") { //this:State
					action { //it:State
						delay(300) 
						pathexecutil.doMove(myself ,"p" )
					}
					 transition(edgeName="t00",targetState="stepDone",cond=whenDispatch("moveok"))
				}	 
				state("stepDone") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						itunibo.planner.plannerUtil.updateMap( "w"  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						stateTimer = TimerActor("timer_stepDone", 
							scope, context!!, "local_tout_trolley_stepDone", 300.toLong() )
					}
					 transition(edgeName="t01",targetState="execPlannedMoves",cond=whenTimeout("local_tout_trolley_stepDone"))   
				}	 
				state("turnMove") { //this:State
					action { //it:State
						if(  CurrentPlannedMove == "l" || CurrentPlannedMove == "r"   
						 ){pathexecutil.doMove(myself ,"$CurrentPlannedMove" )
						}
					}
					 transition(edgeName="t02",targetState="rotationDone",cond=whenDispatch("moveok"))
				}	 
				state("rotationDone") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.updateMap( "$CurrentPlannedMove"  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						stateTimer = TimerActor("timer_rotationDone", 
							scope, context!!, "local_tout_trolley_rotationDone", 300.toLong() )
					}
					 transition(edgeName="t03",targetState="execPlannedMoves",cond=whenTimeout("local_tout_trolley_rotationDone"))   
				}	 
				state("parkthecar") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						if(  ! itunibo.planner.plannerUtil.atPos(4,1)  
						 ){itunibo.planner.plannerUtil.planForGoal( "4", "1"  )
						}
						else
						 {itunibo.planner.plannerUtil.planForGoal( "0", "0"  )
						 }
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitchGuarded({ ! itunibo.planner.plannerUtil.atHome()  
					}) )
					transition( edgeName="goto",targetState="end", cond=doswitchGuarded({! ( ! itunibo.planner.plannerUtil.atHome()  
					) }) )
				}	 
				state("end") { //this:State
					action { //it:State
						println("AT HOME ...")
					}
				}	 
			}
		}
}
