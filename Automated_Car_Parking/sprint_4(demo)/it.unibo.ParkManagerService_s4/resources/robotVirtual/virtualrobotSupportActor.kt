package robotVirtual

import java.io.PrintWriter
import java.net.Socket
import org.json.JSONObject
import java.io.BufferedReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.InputStreamReader
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
 
 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class virtualrobotSupportActor( name : String, val clientSocket : Socket? ) : ActorBasic( name ) {
	init{
		println("$tt $name | CREATING")
	}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg : ApplMessage){
// 		println("$tt $name | received  $msg "  )  //perceives all the application events
		if( msg.msgId() == "sonarstart"){
			println("$tt $name | STARTING")
			
		}
	}
}

 