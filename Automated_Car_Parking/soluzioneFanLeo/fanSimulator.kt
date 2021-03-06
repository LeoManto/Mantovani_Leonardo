import it.unibo.kactor.MsgUtil

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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlin.system.exitProcess
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.Job

 
class fanSimulator (name : String ) : ActorBasic( name ) {
	
	var tempAtt = Temperature
	var work = true
	
	val mainScope = CoroutineScope(Dispatchers.Default)
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	override suspend fun actorBody(msg: ApplMessage) {
  		if( msg.msgId() == "startfan" &&  msg.msgType() == "dispatch") {
			work = true
			startFan()
		}
 	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun startFan() = runBlocking{
		println("Fan START | FANSIMU")
		
		val job : Job = mainScope.launch{
			
			while(work==true){
				delay(4000)
				tempAtt.decTemp()
				
				if(tempAtt.getTemp() <= minTemp){
					work = false
					val m5 = MsgUtil.buildEvent(name, "normtemp", "norm(temp)")
					emit(m5)
					delay(1000)
				}
			}
			
		}
		job.join()
		println("done")
	}
}
	
