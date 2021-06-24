
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

var busy = false
private val mainScope = CoroutineScope(Dispatchers.Default)
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
  		if( msg.msgId() == "startSonar") startSonar()
  		else if ( msg.msgId() == "stopSonar") mainScope.cancel()
			
 	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	  suspend fun startSonar(){
		mainScope.launch{
		 	busy = true
		    val time = Random.nextLong(1000, 12000)
		  	delay(time)	  	
		  	val m3 = MsgUtil.buildEvent(name, "sensor", "sensor(outdoor_free)")
		  	emit(m3)
		}
}
}