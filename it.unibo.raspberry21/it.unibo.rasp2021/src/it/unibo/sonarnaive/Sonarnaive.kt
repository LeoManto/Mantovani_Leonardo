/* Generated by AN DISI Unibo */ 
package it.unibo.sonarnaive

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Sonarnaive ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 var simulate = true  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("sonar START")
						discardMessages = true
						solve("consult('sonar2021ConfigKb.pl')","") //set resVar	
						solve("simulate(X)","") //set resVar	
						println(currentSolution)
						 val x = getCurSol("X").toString() 
								   simulate = ( x == "on")	
								   println( "simulate=$simulate" )
						if(  simulate  
						 ){forward("simulatorstart", "simulatorstart(ok)" ,"sonarsimulator" ) 
						}
						else
						 {forward("sonarstart", "sonarstart(ok)" ,"sonardatasource" ) 
						 }
					}
					 transition(edgeName="t00",targetState="handleSonarData",cond=whenEvent("sonar"))
				}	 
				state("handleSonarData") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("distance(V)"), Term.createTerm("distance(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val D = payloadArg(0)  
								emit("sonarrobot", "sonar($D)" ) 
						}
					}
					 transition(edgeName="t01",targetState="handleSonarData",cond=whenEvent("sonar"))
				}	 
			}
		}
}
