/* Generated by AN DISI Unibo */ 
package it.unibo.parkingmanagerservice

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Parkingmanagerservice ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
			lateinit var weightSensorActor : ActorBasic
			lateinit var outSonarActor : ActorBasic
			
			var indoorFree 	  = true
			var outdoorFree	  = true
			var trolleyStatus = true
			var SLOTNUM 	  = 0
			var WEIGHT 		  = 0
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						
								outSonarActor     = sysUtil.getActor("outsonar")!!
								weightSensorActor = sysUtil.getActor("weightsensor")!!
								subscribeLocalActor("parkingmanagerservice")
						discardMessages = false
						println("Park System START | SERVICE")
					}
					 transition( edgeName="goto",targetState="ready", cond=doswitch() )
				}	 
				state("ready") { //this:State
					action { //it:State
						println("waiting for client | SERVICE")
					}
					 transition(edgeName="t06",targetState="handleToken",cond=whenDispatch("pickup"))
					transition(edgeName="t07",targetState="acceptin",cond=whenRequest("reqenter"))
				}	 
				state("acceptin") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("reqenter(V)"), Term.createTerm("reqenter(V)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var msg = payloadArg(0)  
						}
						 SLOTNUM = 1  
						updateResourceRep( "slotnum(${SLOTNUM})"  
						)
						answer("reqenter", "slotsnum", "slotsnum($SLOTNUM)"   )  
						println("Trolley is moving to Indoor | SERVICE")
					}
					 transition(edgeName="t08",targetState="carenter",cond=whenRequest("carenter"))
				}	 
				state("carenter") { //this:State
					action { //it:State
						emit("carindoorarrival", "cia(car_arrived)" ) 
						 indoorFree = false  
					}
					 transition(edgeName="t09",targetState="weightcheck",cond=whenEvent("weightsensor"))
				}	 
				state("weightcheck") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("weight(W)"), Term.createTerm("weight(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												WEIGHT = payloadArg(0).toInt()
												println("Weight: " + WEIGHT)
						}
					}
					 transition( edgeName="goto",targetState="moveToSlotIn", cond=doswitchGuarded({ WEIGHT > 0  
					}) )
					transition( edgeName="goto",targetState="weightError", cond=doswitchGuarded({! ( WEIGHT > 0  
					) }) )
				}	 
				state("moveToSlotIn") { //this:State
					action { //it:State
						println("Trolley moves from entrance to slot $SLOTNUM | SERVICE")
						 indoorFree = true  
					}
					 transition( edgeName="goto",targetState="receipt", cond=doswitch() )
				}	 
				state("weightError") { //this:State
					action { //it:State
						println("Car too light !!")
					}
					 transition( edgeName="goto",targetState="ready", cond=doswitch() )
				}	 
				state("receipt") { //this:State
					action { //it:State
						 var TOKENID = SLOTNUM  
						answer("carenter", "receipt", "receipt($TOKENID)"   )  
						updateResourceRep( "receipt(${TOKENID})"  
						)
						println("Trolley moves to Home | SERVICE")
						stateTimer = TimerActor("timer_receipt", 
							scope, context!!, "local_tout_parkingmanagerservice_receipt", 1000.toLong() )
					}
					 transition(edgeName="t010",targetState="ready",cond=whenTimeout("local_tout_parkingmanagerservice_receipt"))   
				}	 
				state("handleToken") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("pickup(TOKENID)"), Term.createTerm("pickup(TOKENID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  var TOKENIN = payloadArg(0)  
								updateResourceRep( "pickup(${TOKENIN})"  
								)
						}
						println("checking Trolley status | SERVICE")
						stateTimer = TimerActor("timer_handleToken", 
							scope, context!!, "local_tout_parkingmanagerservice_handleToken", 500.toLong() )
					}
					 transition(edgeName="t011",targetState="picking",cond=whenTimeout("local_tout_parkingmanagerservice_handleToken"))   
				}	 
				state("picking") { //this:State
					action { //it:State
						println("Trolley picking car | SERVICE")
						delay(4000) 
						println("Car is in Outdoor area | SERVICE")
						println("Trolley moves to Home | SERVICE")
						emit("caroutdoorarrival", "coa(car_outdoor)" ) 
						 outdoorFree = false  
						stateTimer = TimerActor("timer_picking", 
							scope, context!!, "local_tout_parkingmanagerservice_picking", 200.toLong() )
					}
					 transition(edgeName="t012",targetState="ready",cond=whenTimeout("local_tout_parkingmanagerservice_picking"))   
				}	 
			}
		}
}
