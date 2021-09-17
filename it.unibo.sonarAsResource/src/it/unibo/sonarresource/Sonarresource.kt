/* Generated by AN DISI Unibo */ 
package it.unibo.sonarresource

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Sonarresource ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						println("sonarresource | waits ... ")
					}
					 transition(edgeName="t00",targetState="doUpdate",cond=whenDispatch("applupdate"))
					transition(edgeName="t01",targetState="handleSonarData",cond=whenEvent("sonarrobot"))
				}	 
				state("handleSonarData") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("sonar(V)"), Term.createTerm("sonar(V)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val Distance = payloadArg(0)  
								println("handleSonarData emit:$Distance")
								emit("local_appl", "job(sonarrobot($Distance))" ) 
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("doUpdate") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("applupdate(X)"), Term.createTerm("applupdate(sonarrobot(D))"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  val D  = applsupport.getSonarDistance( payloadArg(0)  )  
								println(" --- doUpdate sonarrobot D=$D")
								updateResourceRep( "{\"sonarvalue\":\"$D\"}"  
								)
						}
						if( checkMsgContent( Term.createTerm("applupdate(X)"), Term.createTerm("applupdate(led(V))"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  val V  = applsupport.getLedValue( payloadArg(0)  )  
								println(" --- doUpdate led V=$V")
								updateResourceRep( "{\"info\":\"led($V)\"}"  
								)
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}