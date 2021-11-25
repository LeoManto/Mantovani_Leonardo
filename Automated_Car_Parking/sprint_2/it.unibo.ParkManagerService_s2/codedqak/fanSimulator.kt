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
	
	val tempAtt = Temperature
	var workFan = true
	
	val mainScope = CoroutineScope(Dispatchers.Default)
	
//-------------------------------------------------------------------------------------
	val termSimulator = thermometerSimulator("")
//-------------------------------------------------------------------------------------

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	override suspend fun actorBody(msg: ApplMessage) {
  		if( msg.msgId() == "temp" &&  msg.msgType() == "event") { 
			var temp = msg.msgContent().toInt()
			if(temp >= maxTemp){
				workFan = true
				startFan()
			}
		}
 	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun startFan() {
		println("Fan START | FAN")
		updateResourceRep( "fan(ON)"  )
		forward("stopinc", "stopinc(temp)" ,"thermometer" ) 
		//-------------------------------------------------------------------------------------------------------
		val job : Job = mainScope.launch{
			
			while(workFan){

				tempAtt.decTemp()
				if(tempAtt.getTemp() <= minTemp){
					workFan = false
					updateResourceRep( "fan(OFF)"  )
					val m5 = MsgUtil.buildEvent(name, "normtemp", "norm(temp)")
					emit(m5)
				}
				delay(3000)
			}	
		}
	}
}
	
