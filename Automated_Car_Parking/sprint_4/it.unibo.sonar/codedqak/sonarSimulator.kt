//package rx
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.runBlocking

/*
-------------------------------------------------------------------------------------------------
 
-------------------------------------------------------------------------------------------------
 */

class sonarSimulator ( name : String ) : ActorBasic( name ) {
  
	val data = sequence<Int>{
		var v0 = 80
		yield(v0)
		while(true){
			v0 = v0 - 5
			yield( v0 )  //yield cede il controllo, rende disponibile valore per valore.
		}
	}
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg : ApplMessage){
  		println("$tt $name | received  $msg "  )
		if( msg.msgId() == "simulatorstart") startDataReadSimulation(   )
     }
  	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
/*
 per 10 volte prende un valore dalla yield, fornendo 10 valori decrescenti, che vengono uno ad uno propagata
 mediante Event, ma NON tramite EMIT ma "emitLocalStreamEvent" che propaga l'evento solo a chi si è registrato
 presso un attore "datalogger", che lo propaga a sua volta ai suoi osservatori.
 */
	suspend fun startDataReadSimulation(    ){
  			var i = 0
			while( i < 10 ){
 	 			val m1 = "distance( ${data.elementAt(i*2)} )"
				i++
 				val event = MsgUtil.buildEvent( name,"sonar",m1)								
  				emitLocalStreamEvent( event )
 				//println("$tt $name | generates $event")
 				//emit(event)  //APPROPRIATE ONLY IF NOT INCLUDED IN A PIPE
 				delay( 2000 )
  			}			
			terminate()
	}

} 

//@kotlinx.coroutines.ObsoleteCoroutinesApi
//@kotlinx.coroutines.ExperimentalCoroutinesApi
//fun main() = runBlocking{
// //	val startMsg = MsgUtil.buildDispatch("main","start","start","datasimulator")
//	val consumer  = dataConsumer("dataconsumer")
//	val simulator = sonarSimulator( "datasimulator" )
//	val filter    = dataFilter("datafilter", consumer)
//	val logger    = dataLogger("logger")
//	simulator.subscribe( logger ).subscribe( filter ).subscribe( consumer ) 
//	MsgUtil.sendMsg("start","start",simulator)
//	simulator.waitTermination()
// } 