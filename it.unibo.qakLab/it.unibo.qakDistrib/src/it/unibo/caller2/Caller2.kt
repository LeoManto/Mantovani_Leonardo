/* Generated by AN DISI Unibo */ 
package it.unibo.caller2

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Caller2 ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("caller2 request cmd ")
						request("cmd", "cmd(callerco2)" ,"resource" )  
					}
					 transition( edgeName="goto",targetState="waitAnswer", cond=doswitch() )
				}	 
				state("waitAnswer") { //this:State
					action { //it:State
						println("caller2 waiting ... ")
					}
					 transition(edgeName="t01",targetState="handleAlarm",cond=whenEvent("alarm"))
					transition(edgeName="t02",targetState="handleReply",cond=whenReply("replytocmd"))
				}	 
				state("handleReply") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
					}
				}	 
				state("handleAlarm") { //this:State
					action { //it:State
						println("---------------------------------------------------")
						println("$name in ${currentState.stateName} | $currentMsg")
						println("---------------------------------------------------")
					}
					 transition( edgeName="goto",targetState="waitAnswer", cond=doswitch() )
				}	 
			}
		}
}
