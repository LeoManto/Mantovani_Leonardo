
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct
import java.util.Timer
import kotlinx.coroutines.launch
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

 
class timerOutdoor (name : String ) : ActorBasic( name ) {

val DTFREE  = 10 //seconds
var tmp     = DTFREE
private val mainScope = CoroutineScope(Dispatchers.Default)
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
  		if( msg.msgId() == "timerstart") startTimer()
  		else if(msg.msgId() == "timerstop") {
			println("Timeout deactivated")
			mainScope.cancel()}
			
 	}	

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
 	fun startTimer(){
		mainScope.launch{
			  tmp = DTFREE
			  println("inizio timer")
			  while( tmp > 0){  		
			  	tmp = tmp - 1 
			  	delay(1000)
	  }
	  println("console timeout")
	  val m1 = MsgUtil.buildEvent(name, "timeout", "timeout(alarm)")
	  emit(m1)
				}	
}
	
}
