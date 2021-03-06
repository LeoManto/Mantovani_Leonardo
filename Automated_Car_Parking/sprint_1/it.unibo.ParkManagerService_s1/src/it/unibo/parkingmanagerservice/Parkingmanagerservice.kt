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
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						
								outSonarActor     = sysUtil.getActor("outsonar")!!
								weightSensorActor = sysUtil.getActor("weightsensor")!!
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
						 SLOTNUM = `it.unibo`.parkingmanagerservice.test.utils.Tester.getSlot()  
						updateResourceRep( "SLOTNUM"  
						)
						answer("reqenter", "slotsnum", "slotsnum($SLOTNUM)"   )  
						updateResourceRep( "slotsnum : slotsnum ($SLOTNUM)"  
						)
						println("Trolley is moving to Indoor | SERVICE")
					}
					 transition(edgeName="t08",targetState="carenter",cond=whenRequest("carenter"))
				}	 
				state("carenter") { //this:State
					action { //it:State
						 indoorFree = false  
						emit("carindoorarrival", "cia(car_arrived)" ) 
					}
					 transition(edgeName="t09",targetState="moveToSlotIn",cond=whenEvent("weightsensor"))
				}	 
				state("moveToSlotIn") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("weight(W)"), Term.createTerm("weight(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												var peso = payloadArg(0)
												println("Weight: " + peso)
						}
						println("Trolley moves from entrance to slot $SLOTNUM | SERVICE")
						 indoorFree = true  
						stateTimer = TimerActor("timer_moveToSlotIn", 
							scope, context!!, "local_tout_parkingmanagerservice_moveToSlotIn", 4000.toLong() )
					}
					 transition(edgeName="t010",targetState="receipt",cond=whenTimeout("local_tout_parkingmanagerservice_moveToSlotIn"))   
				}	 
				state("receipt") { //this:State
					action { //it:State
						 var TOKENID = SLOTNUM  
						answer("carenter", "receipt", "receipt($TOKENID)"   )  
						updateResourceRep( "receipt : receipt($TOKENID)"  
						)
						emit("carindoorarrival", "cia(car_arrived)" ) 
						println("Trolley moves to Home | SERVICE")
						stateTimer = TimerActor("timer_receipt", 
							scope, context!!, "local_tout_parkingmanagerservice_receipt", 1000.toLong() )
					}
					 transition(edgeName="t011",targetState="ready",cond=whenTimeout("local_tout_parkingmanagerservice_receipt"))   
				}	 
				state("handleToken") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("pickup(tokenID)"), Term.createTerm("pickup(tokenID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var tokenIN = payloadArg(0)  
						}
						println("checking Trolley status | SERVICE")
						stateTimer = TimerActor("timer_handleToken", 
							scope, context!!, "local_tout_parkingmanagerservice_handleToken", 500.toLong() )
					}
					 transition(edgeName="t012",targetState="picking",cond=whenTimeout("local_tout_parkingmanagerservice_handleToken"))   
				}	 
				state("picking") { //this:State
					action { //it:State
						println("Trolley picking car | SERVICE")
						delay(4000) 
						println("Car is in Outdoor area | SERVICE")
						println("Trolley moves to Home | SERVICE")
						emit("caroutdoorarrival", "coa(car_outdoor)" ) 
						 outdoorFree = false  
					}
					 transition(edgeName="t013",targetState="withdrawn",cond=whenEvent("carwithdrawn"))
					transition(edgeName="t014",targetState="timeout",cond=whenEvent("timeout"))
				}	 
				state("withdrawn") { //this:State
					action { //it:State
						 outdoorFree = true  
						println("Car withdrawn!")
					}
					 transition( edgeName="goto",targetState="ready", cond=doswitch() )
				}	 
				state("timeout") { //this:State
					action { //it:State
						println("%%%% TIMEOUT %%%%")
						emit("alarm", "timeout(alarm)" ) 
					}
				}	 
			}
		}
}
