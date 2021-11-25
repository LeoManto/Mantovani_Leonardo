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
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

const val maxTemp = 40
const val minTemp = 10
 
class thermometerSimulator (name : String ) : ActorBasic( name ) {
	
	var increment	= false
	var showing		= false
	var tempAtt = Temperature
	
	
	private var mainScope :CoroutineScope = CoroutineScope(Dispatchers.Default)
	//private val showScope = CoroutineScope(Dispatchers.Default)
	var job : Job? = null
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi		
	override suspend fun actorBody(msg: ApplMessage) {
  		if( msg.msgId() == "startthermometer" &&  msg.msgType() == "dispatch") {
			tempAtt.setTemp(minTemp)
			increment	= true
			showing		= true
			startIncrement()
			showTemp()
		}else if(msg.msgId() == "stopinc" &&  msg.msgType() == "dispatch"){
			//println("High temperature, stop inc process | THERMOMETER")
			increment	= false
			stopTherm()
		}else if(msg.msgId() == "normtemp" &&  msg.msgType() == "event"){
			//println("Temperature is now ok, restart inc process | THERMOMETER")
			increment	= true
			startIncrement()
		}
 	}
	
	fun startIncrement(){
		//println("Increment TEMP START | THERMOMETER")
		job = mainScope.launch{
			while(increment){
				delay(3000)
				tempAtt.incTemp()
				delay(3000)		
			}
		}
	}
	
	fun stopTherm(){
		job!!.cancel()
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi	
	fun showTemp() {
		mainScope.launch{
			println("Show TEMP START | THERMOMETER")
			while(showing){
				var t = tempAtt.getTemp().toString()
				val m5 = MsgUtil.buildEvent(name, "temp", t)
				emit(m5)
				forward("updateGui", "temp($t °C)" ,"guiupdater" )
				////updateResourceRep( "{\"temp\":\"$t °C\"}")
				delay(3000)
			}
		}
	}
	

	
}

class Temperature(){
	companion object temp{
		var tempAtt = 0
		@Synchronized fun incTemp(){
            tempAtt += 5
		}
	  
		@Synchronized fun decTemp(){
            tempAtt -= 5
        }

        @Synchronized fun getTemp(): Int {
			return this.tempAtt
		}
		@Synchronized fun setTemp(temp : Int) {
			tempAtt = temp
		}
	}	
}

