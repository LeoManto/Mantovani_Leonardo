/* Generated by AN DISI Unibo */ 
package it.unibo.radargui

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Radargui ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 var DoReply     = false
		  var DistanceStr  = "0"
		  var Distance     = 0
		  var Angle        = "0"
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("radargui start")
						radarPojo.radarSupport.setUpRadarGui(  )
					}
					 transition( edgeName="goto",targetState="waitForDataToShow", cond=doswitch() )
				}	 
				state("waitForDataToShow") { //this:State
					action { //it:State
					}
					 transition(edgeName="t00",targetState="showSpotReply",cond=whenRequest("polar"))
					transition(edgeName="t01",targetState="showSpotNoReply",cond=whenDispatch("polar"))
					transition(edgeName="t02",targetState="showSpotNoReply",cond=whenEvent("polar"))
				}	 
				state("showSpotNoReply") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("polar(D,A)"), Term.createTerm("polar(D,A)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												DistanceStr = payloadArg(0)
											    Distance    = DistanceStr.toInt()
												Angle       = payloadArg(1) 
								                DoReply     = false
						}
					}
					 transition( edgeName="goto",targetState="showSpot", cond=doswitch() )
				}	 
				state("showSpotReply") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("polar(D,A)"), Term.createTerm("polar(D,A)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												DistanceStr = payloadArg(0)
											    Distance    = DistanceStr.toInt()
												Angle       = payloadArg(1) 
								                DoReply     = true
						}
					}
					 transition( edgeName="goto",targetState="showSpot", cond=doswitch() )
				}	 
				state("showSpot") { //this:State
					action { //it:State
						if(  Distance <= 90  
						 ){radarPojo.radarSupport.update( DistanceStr, Angle  )
						if(  DoReply  
						 ){answer("polar", "fromRadarGui", "fromRadarGui(done($DistanceStr))"   )  
						}
						}
					}
					 transition( edgeName="goto",targetState="waitForDataToShow", cond=doswitch() )
				}	 
			}
		}
}
