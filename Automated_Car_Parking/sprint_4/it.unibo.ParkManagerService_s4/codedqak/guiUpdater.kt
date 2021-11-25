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
 
class guiUpdater (name : String ) : ActorBasic( name ) {

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
		if( msg.msgId() == "updateGui" &&  msg.msgType() == "dispatch") {
			update(msg.msgContent().replace("'",""))
		}
}

	fun update(res : String){
		val resourceName = res.substringBefore("(","-")
		val resourceValue = res.substringAfter("(","-").substringBefore(")","-")
		updateResourceRep( "${convertToJSON(resourceName, resourceValue)}")
		}
	
	fun convertToJSON(name: String, value: String) : String {
		val jsonRes = "{\"$name\":\"$value\"}"
		return jsonRes	
	}
}