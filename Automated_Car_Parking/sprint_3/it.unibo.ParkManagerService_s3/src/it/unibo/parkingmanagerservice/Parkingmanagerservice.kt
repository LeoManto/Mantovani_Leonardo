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
			lateinit var thermometerActor : ActorBasic
		 	lateinit var fanActor : ActorBasic
			
			var SLOTNUM = 1
			var INDOORTOKEN  = "1" //tokenid dato al client
			var WEIGHT 		  = 0
			
			var OUTDOORTOKEN = "1" //tokenid ricevuto dal client
			var CARSLOTNUM =  0
			
			var GOAL 	   = ""
			var trolleyPos = ""
			
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						
								outSonarActor     = sysUtil.getActor("outsonar")!!
								weightSensorActor = sysUtil.getActor("weightsensor")!!
								thermometerActor = sysUtil.getActor("thermometer")!!
								fanActor = sysUtil.getActor("fan")!!
						forward("startthermometer", "thermometer(V)" ,"thermometer" ) 
						println("Park System START | SERVICE")
						stateTimer = TimerActor("timer_s0", 
							scope, context!!, "local_tout_parkingmanagerservice_s0", 1000.toLong() )
					}
					 transition(edgeName="t00",targetState="check",cond=whenTimeout("local_tout_parkingmanagerservice_s0"))   
				}	 
				state("check") { //this:State
					action { //it:State
						delay(2000) 
					}
					 transition( edgeName="goto",targetState="acceptReqEnter", cond=doswitchGuarded({ `it.unibo`.utils.ParkingSlotsKb.checkSlots() > 0  
					}) )
					transition( edgeName="goto",targetState="checkOutdoor", cond=doswitchGuarded({! ( `it.unibo`.utils.ParkingSlotsKb.checkSlots() > 0  
					) }) )
				}	 
				state("acceptReqEnter") { //this:State
					action { //it:State
					}
					 transition( edgeName="goto",targetState="ready", cond=doswitchGuarded({ `it.unibo`.utils.ParkingSlotsKb.outdoorFree &&
											unibo.robot.TrolleyKb.trolleyStatus != `it.unibo`.utils.TrolleyStatus.STOPPED
					}) )
					transition( edgeName="goto",targetState="readyOnlyReqEnter", cond=doswitchGuarded({! ( `it.unibo`.utils.ParkingSlotsKb.outdoorFree &&
											unibo.robot.TrolleyKb.trolleyStatus != `it.unibo`.utils.TrolleyStatus.STOPPED
					) }) )
				}	 
				state("checkOutdoor") { //this:State
					action { //it:State
					}
					 transition( edgeName="goto",targetState="readyOnlyOutdoor", cond=doswitchGuarded({ `it.unibo`.utils.ParkingSlotsKb.outdoorFree &&
													unibo.robot.TrolleyKb.trolleyStatus != `it.unibo`.utils.TrolleyStatus.IDLE  
					}) )
					transition( edgeName="goto",targetState="notReady", cond=doswitchGuarded({! ( `it.unibo`.utils.ParkingSlotsKb.outdoorFree &&
													unibo.robot.TrolleyKb.trolleyStatus != `it.unibo`.utils.TrolleyStatus.IDLE  
					) }) )
				}	 
				state("ready") { //this:State
					action { //it:State
						println("INDOOR and OUTDOOR Avaiable | SERVICE")
						stateTimer = TimerActor("timer_ready", 
							scope, context!!, "local_tout_parkingmanagerservice_ready", 5000.toLong() )
					}
					 transition(edgeName="t01",targetState="moveToHome",cond=whenTimeout("local_tout_parkingmanagerservice_ready"))   
					transition(edgeName="t02",targetState="acceptin",cond=whenRequest("reqenter"))
					transition(edgeName="t03",targetState="acceptout",cond=whenDispatch("pickup"))
				}	 
				state("readyOnlyReqEnter") { //this:State
					action { //it:State
						println("INDOOR Avaiable | SERVICE")
						stateTimer = TimerActor("timer_readyOnlyReqEnter", 
							scope, context!!, "local_tout_parkingmanagerservice_readyOnlyReqEnter", 5000.toLong() )
					}
					 transition(edgeName="t04",targetState="moveToHome",cond=whenTimeout("local_tout_parkingmanagerservice_readyOnlyReqEnter"))   
					transition(edgeName="t05",targetState="acceptin",cond=whenRequest("reqenter"))
				}	 
				state("readyOnlyOutdoor") { //this:State
					action { //it:State
						println("OUTDOOR Avaiable | SERVICE")
						stateTimer = TimerActor("timer_readyOnlyOutdoor", 
							scope, context!!, "local_tout_parkingmanagerservice_readyOnlyOutdoor", 5000.toLong() )
					}
					 transition(edgeName="t06",targetState="moveToHome",cond=whenTimeout("local_tout_parkingmanagerservice_readyOnlyOutdoor"))   
					transition(edgeName="t07",targetState="acceptout",cond=whenDispatch("pickup"))
				}	 
				state("notReady") { //this:State
					action { //it:State
						stateTimer = TimerActor("timer_notReady", 
							scope, context!!, "local_tout_parkingmanagerservice_notReady", 1000.toLong() )
					}
					 transition(edgeName="t08",targetState="check",cond=whenTimeout("local_tout_parkingmanagerservice_notReady"))   
				}	 
				state("moveToHome") { //this:State
					action { //it:State
						 if(unibo.robot.TrolleyKb.trolleyStatus == `it.unibo`.utils.TrolleyStatus.IDLE  && GOAL != "home") {  
						println("##### IN MOVE TO HOME ##########")
						println("Moving Trolley to HOME")
						updateResourceRep( "toHome(V)"  
						)
						 GOAL = "home"  
						forward("move", "move($GOAL)" ,"trolley" ) 
						 }  
					}
					 transition( edgeName="goto",targetState="check", cond=doswitch() )
				}	 
				state("acceptin") { //this:State
					action { //it:State
						 SLOTNUM = `it.unibo`.utils.ParkingSlotsKb.findSlot()  
						 `it.unibo`.utils.ParkingSlotsKb.setSlot(SLOTNUM, false)  
						 if(! `it.unibo`.utils.ParkingSlotsKb.indoorFree || 
											unibo.robot.TrolleyKb.trolleyStatus == `it.unibo`.utils.TrolleyStatus.STOPPED
									){  
						answer("reqenter", "waitIndoor", "slotsnum($SLOTNUM)"   )  
						updateResourceRep( "wait(${SLOTNUM}) "  
						)
						 }  
						 else { 
						answer("reqenter", "slotsnum", "slotsnum($SLOTNUM)"   )  
						updateResourceRep( "slotnum(${SLOTNUM}) "  
						)
						println("SLOTNUM = $SLOTNUM | SERVICE")
						 }  
					}
					 transition(edgeName="t09",targetState="carenter",cond=whenRequest("carenter"))
				}	 
				state("carenter") { //this:State
					action { //it:State
						emit("carindoorarrival", "cia(car_arrived)" ) 
						 GOAL = "indoor"  
						forward("move", "move($GOAL)" ,"trolley" ) 
						println("Trolley is moving to Indoor | SERVICE")
					}
					 transition(edgeName="t010",targetState="weightcheck",cond=whenEvent("weightsensor"))
				}	 
				state("weightcheck") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("weight(W)"), Term.createTerm("weight(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												WEIGHT = payloadArg(0).toInt()
												println("Weight: " + WEIGHT)
						}
					}
					 transition( edgeName="goto",targetState="waitTrolleyIndoor", cond=doswitchGuarded({ WEIGHT > 0  
					}) )
					transition( edgeName="goto",targetState="weightError", cond=doswitchGuarded({! ( WEIGHT > 0  
					) }) )
				}	 
				state("waitTrolleyIndoor") { //this:State
					action { //it:State
					}
					 transition(edgeName="t011",targetState="receipt",cond=whenEvent("finished"))
				}	 
				state("weightError") { //this:State
					action { //it:State
						println("Car not actual in INDOOR-AREA")
					}
					 transition( edgeName="goto",targetState="check", cond=doswitch() )
				}	 
				state("receipt") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("finished(V)"), Term.createTerm("finished(GOAL)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 trolleyPos = payloadArg(0)  
						}
						 if(trolleyPos == "indoor") {  
						println("Trolley is in INDOOR| SERVICE")
						 	INDOORTOKEN = `it.unibo`.utils.ParkingSlotsKb.generateToken(SLOTNUM)  
						answer("carenter", "receipt", "receipt($INDOORTOKEN)"   )  
						updateResourceRep( "receipt($INDOORTOKEN)"  
						)
						 }  
					}
					 transition( edgeName="goto",targetState="moveToSlotIn", cond=doswitchGuarded({ trolleyPos == "indoor" 
					}) )
					transition( edgeName="goto",targetState="waitTrolleyIndoor", cond=doswitchGuarded({! ( trolleyPos == "indoor" 
					) }) )
				}	 
				state("moveToSlotIn") { //this:State
					action { //it:State
						 	GOAL = "p" + SLOTNUM.toString()	 
						forward("move", "move($GOAL)" ,"trolley" ) 
						println("Trolley moves from INDOOR to $SLOTNUM")
					}
					 transition(edgeName="t012",targetState="checkPark",cond=whenEvent("finished"))
				}	 
				state("checkPark") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("finished(V)"), Term.createTerm("finished(GOAL)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 trolleyPos = payloadArg(0)  
						}
					}
					 transition( edgeName="goto",targetState="parkedCar", cond=doswitchGuarded({ trolleyPos == GOAL  
					}) )
					transition( edgeName="goto",targetState="parkingError", cond=doswitchGuarded({! ( trolleyPos == GOAL  
					) }) )
				}	 
				state("parkedCar") { //this:State
					action { //it:State
						println("Car is parked at p$SLOTNUM")
						stateTimer = TimerActor("timer_parkedCar", 
							scope, context!!, "local_tout_parkingmanagerservice_parkedCar", 500.toLong() )
					}
					 transition(edgeName="t013",targetState="check",cond=whenTimeout("local_tout_parkingmanagerservice_parkedCar"))   
				}	 
				state("parkingError") { //this:State
					action { //it:State
						println("Parking Error")
					}
					 transition( edgeName="goto",targetState="check", cond=doswitch() )
				}	 
				state("acceptout") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("pickup(OUTDOORTOKEN)"), Term.createTerm("pickup(OUTDOORTOKEN)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  OUTDOORTOKEN = payloadArg(0).toString()  
						}
						 	CARSLOTNUM = `it.unibo`.utils.ParkingSlotsKb.checkToken(OUTDOORTOKEN)  
					}
					 transition( edgeName="goto",targetState="picking", cond=doswitchGuarded({ CARSLOTNUM > 0  
					}) )
					transition( edgeName="goto",targetState="tokenError", cond=doswitchGuarded({! ( CARSLOTNUM > 0  
					) }) )
				}	 
				state("picking") { //this:State
					action { //it:State
						delay(1000) 
						 `it.unibo`.utils.ParkingSlotsKb.setSlot(CARSLOTNUM, true) 
									GOAL = "p" + CARSLOTNUM.toString()
						forward("move", "move($GOAL)" ,"trolley" ) 
						println("Trolley picking car from slot $CARSLOTNUM | SERVICE")
					}
					 transition(edgeName="t014",targetState="checkCarPicked",cond=whenEvent("finished"))
				}	 
				state("checkCarPicked") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("finished(V)"), Term.createTerm("finished(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												trolleyPos = payloadArg(0)
						}
					}
					 transition( edgeName="goto",targetState="moveToOut", cond=doswitchGuarded({ trolleyPos == GOAL  
					}) )
					transition( edgeName="goto",targetState="trolleyToPickingSlot", cond=doswitchGuarded({! ( trolleyPos == GOAL  
					) }) )
				}	 
				state("trolleyToPickingSlot") { //this:State
					action { //it:State
						println("WAIT THAT TROLLEY ARRIVE AT PICKING SLOT | SERVICE")
						stateTimer = TimerActor("timer_trolleyToPickingSlot", 
							scope, context!!, "local_tout_parkingmanagerservice_trolleyToPickingSlot", 5000.toLong() )
					}
					 transition(edgeName="t015",targetState="check",cond=whenTimeout("local_tout_parkingmanagerservice_trolleyToPickingSlot"))   
					transition(edgeName="t016",targetState="checkCarPicked",cond=whenEvent("finished"))
				}	 
				state("moveToOut") { //this:State
					action { //it:State
						 GOAL = "outdoor"  
						forward("move", "move($GOAL)" ,"trolley" ) 
					}
					 transition(edgeName="t017",targetState="carInOutdoor",cond=whenEvent("finished"))
				}	 
				state("carInOutdoor") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("finished(V)"), Term.createTerm("finished(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 trolleyPos = payloadArg(0)  
						}
						 if (trolleyPos == "outdoor") { 
						emit("caroutdoorarrival", "coa(car_arrived)" ) 
						println("Car is in Outdoor area | SERVICE")
						 }  
					}
					 transition( edgeName="goto",targetState="check", cond=doswitchGuarded({ trolleyPos == "outdoor"  
					}) )
					transition( edgeName="goto",targetState="outdoorError", cond=doswitchGuarded({! ( trolleyPos == "outdoor"  
					) }) )
				}	 
				state("outdoorError") { //this:State
					action { //it:State
						println("outdoor Error")
					}
					 transition( edgeName="goto",targetState="check", cond=doswitch() )
				}	 
				state("tokenError") { //this:State
					action { //it:State
						println("Invalid insert Token!")
					}
					 transition( edgeName="goto",targetState="check", cond=doswitch() )
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
