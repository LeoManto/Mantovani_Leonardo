package it.unibo.parkingmanagerservicetest

import org.junit.Assert.*
import java.net.UnknownHostException
import org.junit.BeforeClass
import cli.System.IO.IOException
import org.junit.Test
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import it.unibo.kactor.QakContext
import org.junit.Before
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import org.junit.AfterClass
import it.unibo.kactor.sysUtil
import it.unibo.parkingmanagerservice.test.utils.CoapObserverForTesting
import it.unibo.kactor.ApplMessage
import org.junit.After


 
//ORDINARE I TEST, COME???

class TestPlan0 {
	
		companion object{

		var testingObserver   : CoapObserverForTesting ? = null
		var systemStarted         = false
		val channelSyncStart      = Channel<String>()
		var myactor               : ActorBasic? = null
		var counter               = 1
			
		var token	=	0
		var slotnum =	0

		@JvmStatic
        @BeforeClass
		//@Target([AnnotationTarget.FUNCTION]) annotation class BeforeClass
		//@Throws(InterruptedException::class, UnknownHostException::class, IOException::class)
		fun init() {
			GlobalScope.launch{
				it.unibo.ctxParkingservice.main()
			}
			GlobalScope.launch{
				myactor = QakContext.getActor("parkingmanagerservice")
				//clientactor = QakContext.getActor("client")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor=QakContext.getActor("parkingmanagerservice")
				}
				channelSyncStart.send("starttesting")
				
			}
		}//init
		
		@JvmStatic
	    @AfterClass
		fun terminate() {
			println("terminate the testing")
		}
			
	}//object
	
	
	
	@Before
	fun checkSystemStarted()  {
		println("\n=================================================================== ") 
	    println("+++++++++ BEFOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE testingObserver=$testingObserver")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			
		} 
		if( testingObserver == null)
			testingObserver = CoapObserverForTesting("obstesting${counter++}")
  	}
	
	
	@After
	fun removeObs(){
		println("+++++++++ AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${testingObserver!!.name}")
		testingObserver!!.terminate()
		testingObserver = null
		runBlocking{
			delay(1000)
		}
 	}
	
    @Test
    @kotlinx.coroutines.ObsoleteCoroutinesApi
	fun testreqenterSlotFree(){
		
	  	println("+++++++++ testreqenter ")	  
 		var result  = ""
		
		runBlocking{
			
			val channelForObserver = Channel<String>()
			testingObserver!!.addObserver(channelForObserver!!, "slotnum")
			println("+++++++++ testslotnum +++++++++")
			var msg = MsgUtil.buildRequest("client","reqenter","reqenter(bob)","parkingmanagerservice")
			MsgUtil.sendMsg(msg, myactor!!)
			
			result = channelForObserver!!.receive()
			println("+++++++++ testreqenter RESULT=$result +++++++++")
			slotnum = result.substringAfter("(",result).substringBefore(")",result).toInt()
			assertTrue(slotnum > 0)
			delay(1000)
	  
//----------------------------------------------------------------------------------------------------------------------------
			
			println("+++++++++ testweightsensor +++++++++")	 
			val tempchannel	 =  Channel<String>()
			val weightObs = CoapObserverForTesting("obsweight","ctxparkingservice", "weightsensor","5683")
			weightObs.addObserver(tempchannel, "weight")
			
			msg = MsgUtil.buildRequest("client","carenter","carenter($slotnum)","parkingmanagerservice")
		   	MsgUtil.sendMsg(msg, myactor!!)
			
			var resultTemp = tempchannel.receive()
			println("+++++++++ testweightsensor RESULT=$resultTemp +++++++++")
			assertTrue(resultTemp.substringAfter("(",resultTemp).substringBefore(")",resultTemp).toInt() > 0)
			delay(1000)
			
//----------------------------------------------------------------------------------------------------------------------------			
			
			println("+++++++++ testreceipt +++++++++")
			
			testingObserver!!.addObserver(channelForObserver!!, "receipt")
			
			result = channelForObserver!!.receive()
			println("+++++++++ testcareneter RESULT=$result +++++++++")
			token = result.substringAfter("(",result).substringBefore(")",result).toInt()
			assertTrue(token > 0)
			assertEquals(slotnum, token) //solo in questo modello			 
		}
	}
 
//----------------------------------------------------------------------------------------------------------------------------	
	
/*	
	
  @Test
  @kotlinx.coroutines.ObsoleteCoroutinesApi
	fun testcareneter(){
 		println("+++++++++ testcarenter ")
 		val msg = MsgUtil.buildRequest("client","carenter","carenter($slotnum)","parkingmanagerservice")
		var result  = ""
		runBlocking{
			val obsanswer = Channel<String>()
			CoapObserverForTesting.addObserver("obsr", "parkingmanagerservice", obsanswer, "receipt")
			result = sendAndObserve(obsanswer, msg)
			println("+++++++++ testcarenter RESULT=$result")
			CoapObserverForTesting.removeObserver()
		}
 		token = result.substringAfter("(",result).substringBefore(")",result).toInt()
		assertTrue(token > 0)
		assertEquals(slotnum, token) //solo in questo modello
	}
	
//----------------------------------------------------------------------------------------------------------------------------		
	
  @Test
  @kotlinx.coroutines.ObsoleteCoroutinesApi
	fun testreqpickup(){
 		println("+++++++++ testreqpickup ")
 		val msg = MsgUtil.buildDispatch("client","pickup","pickup($token)","parkingmanagerservice")
		var result  = ""
		runBlocking{
			val obsanswer = Channel<String>()
			CoapObserverForTesting.addObserver("obsr", "parkingmanagerservice", obsanswer, "tokenID")
			result = sendAndObserve(obsanswer, msg)
			println("+++++++++ testreqpickup RESULT=$result")
			CoapObserverForTesting.removeObserver()
		}
		assertEquals(result, "tokenID($token)")
	}
	
//----------------------------------------------------------------------------------------------------------------------------		
  @Test
  @kotlinx.coroutines.ObsoleteCoroutinesApi
	fun testoutsonar(){
 		println("+++++++++ testoutsonar ")
		var result  = ""
		runBlocking{
			val obsanswer = Channel<String>()
			CoapObserverForTesting.addObserver("obsr", "outsonar", obsanswer, "outsonar")
			println("+++++++++ testoutsonar RESULT=$result")
			CoapObserverForTesting.removeObserver()
		}
		assertEquals(result, "outsonar(true)")
	}
	
//----------------------------------------------------------------------------------------------------------------------------		
  @Test
  @kotlinx.coroutines.ObsoleteCoroutinesApi
	fun testfinalNoAlarm(){
 		println("+++++++++ testfinal ")
 		val msg = MsgUtil.buildEvent("client","carwithdrawn","cw(car)")
		var result  = ""
		runBlocking{
			val obsanswer = Channel<String>()
			CoapObserverForTesting.addObserver("obsr", "parkingmanagerservice", obsanswer, "end")
			result = sendAndObserve(obsanswer, msg)
			println("+++++++++ testfinal RESULT=$result")
			CoapObserverForTesting.removeObserver()
		}
		assertEquals(result, "end(bob)")
	}
	
//----------------------------------------------------------------------------------------------------------------------------		
  //@Test
  @kotlinx.coroutines.ObsoleteCoroutinesApi
	fun testfinalAlarm(){
 		println("+++++++++ testfinal ")
		var result  = ""
		runBlocking{
			val obsanswer = Channel<String>()
			CoapObserverForTesting.addObserver("obsr", "parkingmanagerservice", obsanswer, "alarm")
			println("+++++++++ testfinal RESULT=$result")
			CoapObserverForTesting.removeObserver()
		}
		assertEquals(result, "alarm(timeout)")
	}
*/
//----------------------------------------------------------------------------------------------------------------------------
		
		/*fun sendAndObserve( obschannel: Channel<String>, move: ApplMessage ) : String{
 		var result  = ""		
		val cmdh = MsgUtil.buildDispatch("tester", "cmd", "cmd(h)", "basicrobot")
		runBlocking{
 			MsgUtil.sendMsg(move, myactor!!)
			result = obschannel.receive()
			println("+++++++++  sendAndObserve RESULT=$result for move=$move")			
		    //The command w has the duration of 1000 msec
			//Observing moveactivated(w) BEFORE 1000 msec is misleading ...
		    if( ! result.contains("obstacle(w)") &&
				 move.msgContent() == "cmd(w)" || move.msgContent() == "cmd(s)" ){
				 delay(1100)
			} 
		    MsgUtil.sendMsg(cmdh, myactor!!)	//otherwise the virtualrobot does not execute next		
			//if basicrobot enters in state handleObstacle, it executes s,h, but without updating 
		}
		return result
	}*/
	
	
	
}


