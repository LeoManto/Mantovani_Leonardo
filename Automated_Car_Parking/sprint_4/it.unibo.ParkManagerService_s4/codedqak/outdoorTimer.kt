
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
import it.unibo.utils.CoapObserver
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
 
class outdoorTimer (name : String ) : ActorBasic( name ) {

val DTFREE  = 10 //seconds
var tmp     = DTFREE
var busy 	= false
	
private var timerScope = CoroutineScope(Dispatchers.Default)

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
		if( msg.msgId() == "startimer" &&  msg.msgType() == "dispatch") {
			timerScope = CoroutineScope(Dispatchers.Default)
			startTimer()
		}
	  	else if ( msg.msgId() == "stoptimer" &&  msg.msgType() == "dispatch") {
	  		stopTimer()
		}
 	}

	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	  suspend fun stopTimer(){
		    timerScope.cancel()
		    tmp = DTFREE
		}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
 	fun startTimer(){
		timerScope.launch{
			  tmp = DTFREE
			  while( tmp > 0){  		
			  	tmp = tmp - 1 
			  	delay(1000)
	  }
			updateResourceRep( "outdoor(BUSY)")
			updateResourceRep( "alarm(TIMEOUT)")
			forward("updateGui", "alarm(TIMEOUT)" ,"guiupdater" )
		}	
}

}