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
		var myactor           : ActorBasic? = null
		var clientactor           : ActorBasic? = null
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
				clientactor = QakContext.getActor("client")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor=QakContext.getActor("parkingmanagerservice")
					clientactor = QakContext.getActor("client")
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

 
//----------------------------------------------------------------------------------------------------------------------------	

 	@Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
	fun testWorkflowSlotFree(){
		
 		var result  = ""
		
		runBlocking{
			val channelForObserver = Channel<String>()
			testingObserver!!.addObserver(channelForObserver!!, "slotnum")
			//var msg = MsgUtil.buildRequest("client","reqenter","reqenter(bob)","parkingmanagerservice")
			//MsgUtil.sendMsg(msg, myactor!!)
			clientactor!!.request("reqenter","reqenter(bob)","parkingmanagerservice")
			
			println("+++++++++ testreqenter ")	
			
			result = channelForObserver!!.receive()
			println("+++++++++ testreqenter RESULT=$result +++++++++")
			slotnum = result.substringAfter("(",result).substringBefore(")",result).toInt()
			assertTrue(slotnum > 0)
			
			delay(2000)
			
			//--------------------------------------------------------------------------------
			
			testingObserver!!.addObserver(channelForObserver!!, "receipt")
			
			val secondChannel      = Channel<String>() 
			var sensorObs = CoapObserverForTesting("obsweightsensor", "ctxparkingservice" ,"weightsensor", "5683")
			sensorObs.addObserver(secondChannel,"weight")
			
			//var msg = MsgUtil.buildRequest("client","carenter","carenter(ok)","parkingmanagerservice")
			//MsgUtil.sendMsg(msg, myactor!!)
			clientactor!!.request("carenter","carenter(ok)","parkingmanagerservice")
			
			println("+++++++++ testweightsensor")
			var resultSensor = secondChannel.receive()
			assertTrue(resultSensor.substringAfter("(",resultSensor).substringBefore(")",resultSensor).toInt() > 0 )
			println("+++++++++ testweightsensor RESULT=$result +++++++++")
			
			println("+++++++++ testcarenter")	
			result = channelForObserver!!.receive()
			token = result.substringAfter("(",result).substringBefore(")",result).toInt()
			assertTrue(token > 0)
			println("+++++++++ testcarenter RESULT=$result +++++++++")
			//assertEquals("receipt(1)", "receipt($slotnum)")
			assertEquals(slotnum, token)
			
			delay(4000)
			
			//----------------------------------------------------------------------------------
			
			testingObserver!!.addObserver(channelForObserver!!, "pickup")
			
			//msg = MsgUtil.buildDispatch("client","pickup","pickup($token)","parkingmanagerservice")
			//MsgUtil.sendMsg(msg, myactor!!)
			clientactor!!.forward("pickup","pickup($token)","parkingmanagerservice")
			
			println("+++++++++ testpickup")
			result = channelForObserver!!.receive()
			println("+++++++++ testpickup RESULT=$result +++++++++")
			assertTrue(result.substringAfter("(",result).substringBefore(")",result).toInt() > 0)
			
			//-----------------------------------------------------------------------------------
			
			sensorObs = CoapObserverForTesting("obsoutsonar", "ctxparkingservice" ,"outsonar", "5683")
			sensorObs.addObserver(secondChannel,"outsonar")
			println("+++++++++ testoutsensor1")
			result = secondChannel.receive()
			println("+++++++++ testoutsensor1 RESULT=$result +++++++++")
			assertEquals("outsonar(true)", result)
			
			delay(2000)
			//--------------------------------------------------------------------------------------
			
			println("+++++++++ testoutsensor2")
			clientactor!!.emit(MsgUtil.buildEvent("client","carwithdrawn","cw(ok)"))
 			result = secondChannel.receive()
			println("+++++++++ testoutsensor2 RESULT=$result +++++++++")
 			assertEquals("outsonar(false)", result)
 			
	  	}
 	}
}


