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
		}else if(msg.msgId() == "restart" &&  msg.msgType() == "dispatch"){
			work = true
		}
 	}
	
	fun startThermometer(){
		println("Thermometer START | THERMOMETER")
		tempAtt.setTemp(minTemp)
		mainScope.launch{
			
			while(work==true){
				
				println("Actual temperature: " + tempAtt.getTemp())
				delay(4000)
				tempAtt.incTemp()
				
				if(tempAtt.getTemp() >= maxTemp){
					work = false
					val m5 = MsgUtil.buildEvent(name, "hottemp", "hot(temp)")
					emit(m5)
					while(work == false){
						delay(4000)
						println("Actual temperature: " + tempAtt.getTemp())
					}
				}
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

