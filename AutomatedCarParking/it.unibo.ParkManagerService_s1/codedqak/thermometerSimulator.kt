
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct
import java.util.Timer
import kotlin.random.Random
 
class thermometerSimulator (name : String ) : ActorBasic( name ) {

	var thermo = 5

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
  		if( msg.msgId() == "startThermometer") startThermo()
			
 	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	  suspend fun startThermo(){
		while(true){
		 	for( i in 4..40){
				thermo = i
				var time = Random.nextLong(1000,5000)
				println(thermo)
				delay(time)
				val m4 = MsgUtil.buildEvent(name, "thermometer", "temperature($thermo)")
				emit(m4)
			}
		}
}
}