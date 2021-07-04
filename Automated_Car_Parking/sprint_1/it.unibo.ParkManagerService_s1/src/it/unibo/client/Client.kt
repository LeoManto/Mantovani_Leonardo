/* Generated by AN DISI Unibo */ 
package it.unibo.client

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Client ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
				var SLOTNUM = 0
				var TOKENID = 0 // sar� direttamente il num dello slot assegnato //
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("Client mock simulation START | CLIENT")
					}
					 transition( edgeName="goto",targetState="requestToEnter", cond=doswitch() )
				}	 
				state("requestToEnter") { //this:State
					action { //it:State
						delay(4000) 
						println("client notify his interest in entering | CLIENT")
						request("reqenter", "reqenter(client)" ,"parkingmanagerservice" )  
					}
					 transition(edgeName="t00",targetState="cartoindoor",cond=whenReply("slotsnum"))
				}	 
				state("noentry") { //this:State
					action { //it:State
					}
				}	 
				state("cartoindoor") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("slotsnum(SLOTNUM)"), Term.createTerm("slotsnum(SLOTNUM)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 SLOTNUM = payloadArg(0).toInt()  
								if(  SLOTNUM > 0  
								 ){println("SLOTNUM = $SLOTNUM | CLIENT")
								request("carenter", "carenter(V)" ,"parkingmanagerservice" )  
								}
						}
					}
					 transition(edgeName="t01",targetState="afterreceipt",cond=whenReply("receipt"))
				}	 
				state("afterreceipt") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("receipt(TOKENID)"), Term.createTerm("receipt(TOKENID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 TOKENID = payloadArg(0).toInt()  
								updateResourceRep( "TOKENID"  
								)
								println("client's TOKENID is $TOKENID | CLIENT")
						}
						stateTimer = TimerActor("timer_afterreceipt", 
							scope, context!!, "local_tout_client_afterreceipt", 10000.toLong() )
					}
					 transition(edgeName="t02",targetState="reqpickup",cond=whenTimeout("local_tout_client_afterreceipt"))   
				}	 
				state("reqpickup") { //this:State
					action { //it:State
						println("client notify his interest in picking his car | CLIENT")
						forward("pickup", "pickup($TOKENID)" ,"parkingmanagerservice" ) 
					}
					 transition(edgeName="t03",targetState="pickupcar",cond=whenEvent("caroutdoorarrival"))
				}	 
				state("pickupcar") { //this:State
					action { //it:State
						delay(2000) 
						emit("carwithdrawn", "cw(bye)" ) 
					}
				}	 
			}
		}
}
