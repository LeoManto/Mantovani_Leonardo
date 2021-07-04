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

const val maxTemp = 40
const val minTemp = 20
 
class thermometerSimulator (name : String ) : ActorBasic( name ) {
	
	var work = true
	var tempAtt = Temperature
	
	
	private val mainScope = CoroutineScope(Dispatchers.Default)
	
	
	
	override suspend fun actorBody(msg: ApplMessage) {
  		if( msg.msgId() == "startthermometer" &&  msg.msgType() == "dispatch") {
			startThermometer()
		}else if(msg.msgId() == "stopinc" &&  msg.msgType() == "dispatch"){
			println("High temperature, stop inc process.")
			work = false
		}else if(msg.msgId() == "normtemp" &&  msg.msgType() == "event"){
			println("Temperature is now ok, restart inc process.")
			work = true
		}
 	}
	
	fun startThermometer(){
		println("Thermometer START | THERMOMETER")
		tempAtt.setTemp(minTemp)
		mainScope.launch{
			
			while(work==true){
				
				//println("Actual temperature: " + tempAtt.getTemp())
				delay(2000)
				tempAtt.incTemp()
				
				//-------------------------------------------------------------------------------------------------------
				//se si vuole inviare un evento al mockActor...
				val m5 = MsgUtil.buildEvent(name, "hottemp", tempAtt.getTemp().toString())
				emit(m5)
				
				//se si vuole inviare un evento al mockActor...
				//forward("hottemp", tempAtt.getTemp().toString(), "fan")
				//-------------------------------------------------------------------------------------------------------
				
				
				delay(2000)
				while(work == false){
						delay(4000)
						//println("Actual temperature: " + tempAtt.getTemp())
				}
				
				//-------------------------------------------------------------------------------------------------------
				//Soluzione precedente con anche il mamager
				/*if(tempAtt.getTemp() >= maxTemp){
					work = false
					val m5 = MsgUtil.buildEvent(name, "hottemp", "hot(temp)")
					emit(m5)
					while(work == false){
						delay(4000)
						//println("Actual temperature: " + tempAtt.getTemp())
					}
				}*/
				//-------------------------------------------------------------------------------------------------------
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

