
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct
import java.util.Timer
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
 
class sonarSimulator (name : String ) : ActorBasic( name ) {

val DTFREE  = 10 //seconds
var tmp     = DTFREE
	
private var mainScope = CoroutineScope(Dispatchers.Default)
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
  		if( msg.msgId() == "caroutdoorarrival" &&  msg.msgType() == "event") startSonar()
  		else if ( msg.msgId() == "carwithdrawn" &&  msg.msgType() == "event") stopSonar()
 	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	  suspend fun startSonar(){
		
		 	`it.unibo`.utils.ParkingSlotsKb.outdoorFree  = false 
		 	mainScope = CoroutineScope(Dispatchers.Default)
		    startTimer()
		}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	  suspend fun stopSonar(){
	
		 	`it.unibo`.utils.ParkingSlotsKb.outdoorFree = true 
		    mainScope.cancel()
		    tmp = DTFREE
		}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
 	fun startTimer(){
		mainScope.launch{
			  tmp = DTFREE
			  while( tmp > 0){  		
			  	tmp = tmp - 1 
			  	delay(1000)
	  }

	  val m1 = MsgUtil.buildEvent(name, "timeout", "timeout(alarm)")
	  emit(m1)
				}	
}

}