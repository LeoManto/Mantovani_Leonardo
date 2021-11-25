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
import kotlinx.coroutines.runBlocking
 
class weightsensorSimulator (name : String ) : ActorBasic( name ) {
	
	var w = 0
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
  		if( msg.msgId() == "carindoorarrival" && msg.msgType() == "event") {
			simulateWeight()
		}
  		else if( msg.msgId() == "finished" && msg.msgType() == "event" && msg.msgContent() == "finished(indoor)"){
			w = 0
			forward("updateGui", "weight($w KG)" ,"guiupdater" )
			////updateResourceRep( "{\"weight\":\"$w KG\"}")
			updateResourceRep( "carWeight($w)" )
			println("trolley in INDOOR | WEIGHT")
			`it.unibo`.utils.ParkingSlotsKb.indoorFree  = true
			forward("updateGui", "indoorStatus(FREE)" ,"guiupdater" )
			////updateResourceRep( "{\"indoorStatus\":\"FREE\"}")
			updateResourceRep( "indoor(FREE)")

			//val m4 = MsgUtil.buildEvent(name, "weightsensor", "weight($w)")
		   //emit(m4)
		}
 	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	  suspend fun simulateWeight(){
		    w = Random.nextInt(750, 3000)
		    val m4 = MsgUtil.buildEvent(name, "weightsensor", "weight($w)")
		    emit(m4)
		    forward("updateGui", "weight($w KG)" ,"guiupdater" )
		    ////updateResourceRep( "{\"weight\":\"$w KG\"}")
		    if(w>0)
				`it.unibo`.utils.ParkingSlotsKb.indoorFree  = false
				forward("updateGui", "indoorStatus(BUSY)" ,"guiupdater" )
				////updateResourceRep( "{\"indoorStatus\":\"BUSY\"}")
				updateResourceRep( "indoor(BUSY)")
				
	}
} 