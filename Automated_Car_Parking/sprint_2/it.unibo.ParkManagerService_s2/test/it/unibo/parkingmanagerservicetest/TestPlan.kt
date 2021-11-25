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
import kotlinx.coroutines.Job


//ORDINARE I TEST, COME???

class TestPlan {
	
		companion object tester {

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
			delay(2000)
		}
 	}

 
//----------------------------------------------------------------------------------------------------------------------------	

/*========================
 -	A FREE SLOT	: 	YES
 -	INDOOR		:	FREE
 -	OUTDOOR		:	FREE
===========================*/ 
 	@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun testWorkflow(){
		
 		`it.unibo`.utils.ParkingSlotsKb.setArea(false,false,false,false,true,false)
 		`it.unibo`.utils.ParkingSlotsKb.indoorFree  = true
 		`it.unibo`.utils.ParkingSlotsKb.outdoorFree = true
	
		runBlocking{
			val channelForObserver = Channel<String>()
			testingObserver!!.addObserver(channelForObserver, "slotnum")
 
			clientactor!!.request("reqenter","reqenter(bob)","parkingmanagerservice")
			
			println("+++++++++ testreqenter ")	
			
			var result = channelForObserver.receive()
			println("+++++++++ testreqenter RESULT=$result +++++++++")
			slotnum = result.substringAfter("(",result).substringBefore(")",result).toInt()
			assertTrue(slotnum > 0)
			
			delay(2000)
			
			//--------------------------------------------------------------------------------
			
			testingObserver!!.addObserver(channelForObserver, "receipt")
			
			clientactor!!.request("carenter","carenter(ok)","parkingmanagerservice")
			
			println("+++++++++ testcarenter")	
			result = channelForObserver.receive()
			token = result.substringAfter("(",result).substringBefore(")",result).toInt()
			println("+++++++++ testcarenter RESULT=$result +++++++++")
			assertTrue(token > 10000)
			
			delay(4000)
			//----------------------------------------------------------------------------------
			
			testingObserver!!.addObserver(channelForObserver!!, "pickup")

			clientactor!!.forward("pickup","pickup($token)","parkingmanagerservice")
			
			println("+++++++++ testpickup")
			result = channelForObserver.receive()
			println("+++++++++ testpickup RESULT=$result +++++++++")
			assertTrue(result.substringAfter("(",result).substringBefore(")",result).toInt() > 0)
			
			//-----------------------------------------------------------------------------------
 			
	  	}
 	}
	
/*========================
 -	A FREE SLOT	: 	YES
 -	INDOOR		:	NOT FREE  --> FREE
 -	OUTDOOR		:	NOT FREE  --> FREE
=================================*/ 
 	//@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun testWorkflow2(){
		
 		`it.unibo`.utils.ParkingSlotsKb.setArea(false,false,false,true,true,false)
 		`it.unibo`.utils.ParkingSlotsKb.indoorFree  = false
		`it.unibo`.utils.ParkingSlotsKb.outdoorFree  = false
	
		runBlocking{
			
			val channelForObserver = Channel<String>()
			
			testingObserver!!.addObserver(channelForObserver, "wait")
			clientactor!!.request("reqenter","reqenter(bob)","parkingmanagerservice")
			
			println("+++++++++ testreqenter ")	
			var result = channelForObserver.receive()
			println("+++++++++ testreqenter RESULT=$result +++++++++")
			slotnum = result.substringAfter("(",result).substringBefore(")",result).toInt()
			assertTrue(slotnum > 0)
			
			`it.unibo`.utils.ParkingSlotsKb.setSlot(4, false)
			`it.unibo`.utils.ParkingSlotsKb.indoorFree  = true
			
			delay(3000)
			
			//--------------------------------------------------------------------------------
			
			testingObserver!!.addObserver(channelForObserver, "receipt")
			
			clientactor!!.request("carenter","carenter(ok)","parkingmanagerservice")
			
			println("+++++++++ testcarenter")	
			result = channelForObserver.receive()
			token = result.substringAfter("(",result).substringBefore(")",result).toInt()
			println("+++++++++ testcarenter RESULT=$result +++++++++")
			assertTrue(token > 10000)
			
			delay(3000)
			//----------------------------------------------------------------------------------
			
			testingObserver!!.addObserver(channelForObserver, "pickup")

			clientactor!!.forward("pickup","pickup($token)","parkingmanagerservice")
			
			delay(3000)
			
			`it.unibo`.utils.ParkingSlotsKb.outdoorFree = true
			
			println("+++++++++ testpickup")
			result = channelForObserver.receive()
			println("+++++++++ testpickup RESULT=$result +++++++++")
			assertTrue(result.substringAfter("(",result).substringBefore(")",result).toInt() > 0)
			
			delay(2000)
			
			clientactor!!.emit(MsgUtil.buildEvent("client", "carwidrawn","cw(taken)"))
			
		}
	}
			
//-----------------------------------------------------------------------------------


	
/*========================
 -	A FREE SLOT	: 	NO FREE SLOT
 -	INDOOR		:	NOT FREE
 -	OUTDOOR		:	FREE
===========================*/ 
 	//@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun testWorkflow3(){
		
 		`it.unibo`.utils.ParkingSlotsKb.setArea(false,false,false,false,false,false)
 		`it.unibo`.utils.ParkingSlotsKb.indoorFree  = false
 		`it.unibo`.utils.ParkingSlotsKb.outdoorFree = true
	
		runBlocking{
			
			val channelForObserver = Channel<String>()
			
			
			testingObserver!!.addObserver(channelForObserver, "toHome")
			clientactor!!.request("reqenter","reqenter(bob)","parkingmanagerservice")
			
			println("+++++++++ testNotFreeSlot ")	
			var result = channelForObserver.receive()
			println("+++++++++ testNotFreeSlot RESULT=$result +++++++++")
			assertEquals(result, "toHome(V)")
			
			`it.unibo`.utils.ParkingSlotsKb.setSlot(4, true)
			println("One free slot")
			
			//--------------------------------------------------------------------------------
			
			
			
			//--------------------------------------------------------------------------------
			
			delay(1000)
			println("+++++++++ testreqenter ")	
			testingObserver!!.addObserver(channelForObserver, "wait")
			clientactor!!.request("reqenter","reqenter(bob)","parkingmanagerservice")
			
			result = channelForObserver.receive()
			println("+++++++++ testreqenter RESULT=$result +++++++++")
			slotnum = result.substringAfter("(",result).substringBefore(")",result).toInt()
			assertTrue(slotnum > 0)
			
			`it.unibo`.utils.ParkingSlotsKb.indoorFree = true
			println("Free indoor")
			
			delay(1000)
			
			//--------------------------------------------------------------------------------
			
			testingObserver!!.addObserver(channelForObserver, "receipt")
			
			clientactor!!.request("carenter","carenter(ok)","parkingmanagerservice")
			
			println("+++++++++ testcarenter")
			result = channelForObserver.receive()
			token = result.substringAfter("(",result).substringBefore(")",result).toInt()
			println("+++++++++ testcarenter RESULT=$result +++++++++")
			assertTrue(token > 10000)
			
			delay(1000)
			//----------------------------------------------------------------------------------

			
			
			testingObserver!!.addObserver(channelForObserver, "pickup")

			clientactor!!.forward("pickup","pickup($token)","parkingmanagerservice")
			
			delay(500)
			
			println("+++++++++ testpickup")
			result = channelForObserver.receive()
			println("+++++++++ testpickup RESULT=$result +++++++++")
			assertTrue(result.substringAfter("(",result).substringBefore(")",result).toInt() > 0)
			}
 	}

			
			
//-----------------------------------------------------------------------------------
			
			
 				
/*========================
 -	A FREE SLOT	: 	ONE FREE SLOT
 -	INDOOR		:	FREE
 -	OUTDOOR		:	FREE
 -	TICKET		: 	NOT VALID
===========================*/		
	//@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun testWorkflow4(){
		
 		`it.unibo`.utils.ParkingSlotsKb.setArea(false,false,false,true,false,false)
 		`it.unibo`.utils.ParkingSlotsKb.indoorFree  = true
 		`it.unibo`.utils.ParkingSlotsKb.outdoorFree = true
	
		runBlocking{
			
			val channelForObserver = Channel<String>()
			
			testingObserver!!.addObserver(channelForObserver, "slotnum")

			clientactor!!.request("reqenter","reqenter(bob)","parkingmanagerservice")			
			
			println("+++++++++ testreqenter ")	
			
			var result = channelForObserver.receive()
			println("+++++++++ testreqenter RESULT=$result +++++++++")
			slotnum = result.substringAfter("(",result).substringBefore(")",result).toInt()
			assertTrue(slotnum > 0)
			
			delay(2000)
			
			//--------------------------------------------------------------------------------
			
			testingObserver!!.addObserver(channelForObserver, "receipt")
			
			clientactor!!.request("carenter","carenter(ok)","parkingmanagerservice")
			
			println("+++++++++ testcarenter")	
			result = channelForObserver.receive()
			token = result.substringAfter("(",result).substringBefore(")",result).toInt()
			println("+++++++++ testcarenter RESULT=$result +++++++++")
			assertTrue(token > 10000)
			
			delay(1000)
			//----------------------------------------------------------------------------------

			
			
			testingObserver!!.addObserver(channelForObserver, "pickup")
			
			clientactor!!.forward("pickup","pickup(12345)","parkingmanagerservice")
			
			delay(500)			
			
			println("+++++++++ testpickupError")
			result = channelForObserver.receive()
			println("+++++++++ testpickupError RESULT=$result +++++++++")
			assertTrue(result.substringAfter("(",result).substringBefore(")",result).toInt() < 0)
			
			
			//-----------------------------------------------------------------------------------
			
			delay(1000)

			clientactor!!.forward("pickup","pickup($token)","parkingmanagerservice")
			
			delay(500)
			
			println("+++++++++ testpickupCorrect")
			result = channelForObserver.receive()
			println("+++++++++ testpickupCorrect RESULT=$result +++++++++")
			assertTrue(result.substringAfter("(",result).substringBefore(")",result).toInt() > 0)
 			
	  	}
 	}
	
//-----------------------------------------------------------------------------------------
	
/*================
Simultaneous arrival of more clients
- ALL SLOTS FREE
- INDOOR  initially FREE
- OUTDOOR initially FREE   
==================*/ 		
	
	//@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun testWorkflow5(){
		
 		`it.unibo`.utils.ParkingSlotsKb.setArea(true,true,true,true,true,true)
 		`it.unibo`.utils.ParkingSlotsKb.indoorFree  = true
		`it.unibo`.utils.ParkingSlotsKb.outdoorFree  = true
		
		runBlocking{
			
			val channelForObserver = Channel<String>()
			
			testingObserver!!.addObserver(channelForObserver, "slotnum")
			
			var msg = MsgUtil.buildRequest("client1", "reqenter","reqenter(bob)","parkingmanagerservice")
			MsgUtil.sendMsg(msg,myactor!!)
			msg = MsgUtil.buildRequest("client2", "reqenter","reqenter(george)","parkingmanagerservice")
			MsgUtil.sendMsg(msg,myactor!!)

			println("+++++++++ testreqenter1 ")	
			
			var result = channelForObserver.receive()
			println("+++++++++ testreqenter RESULT=$result +++++++++")
			slotnum = result.substringAfter("(",result).substringBefore(")",result).toInt()
			assertTrue(slotnum > 0)
			
			delay(2000)
			//--------------------------------------------------------------------------------
			
			testingObserver!!.addObserver(channelForObserver, "receipt")
			
			msg = MsgUtil.buildRequest("client1", "carenter","carenter(bob)","parkingmanagerservice")
			MsgUtil.sendMsg(msg,myactor!!)
			
			println("+++++++++ testcarenter1")	
			result = channelForObserver.receive()
			token = result.substringAfter("(",result).substringBefore(")",result).toInt()
			println("+++++++++ testcarenter RESULT=$result +++++++++")
			assertTrue(token > 10000)
			
			delay(1000)
			
			println("+++++++++ testreqenter2")	
			
			result = channelForObserver.receive()
			println("+++++++++ testreqenter RESULT=$result +++++++++")
			var slotnum2 = result.substringAfter("(",result).substringBefore(")",result).toInt()
			assertTrue(slotnum2 > 0 && !slotnum2.equals(slotnum))
			
			delay(2000)
			//--------------------------------------------------------------------------------
			
			testingObserver!!.addObserver(channelForObserver, "receipt")
			
			msg = MsgUtil.buildRequest("client2", "carenter","carenter(george)","parkingmanagerservice")
			MsgUtil.sendMsg(msg,myactor!!)
			
			println("+++++++++ testcarenter2")	
			result = channelForObserver.receive()
			var token2 = result.substringAfter("(",result).substringBefore(")",result).toInt()
			println("+++++++++ testcarenter RESULT=$result +++++++++")
			assertTrue(token2 > 10000 && !token2.equals(token))
	  	}
 	}
	
//-----------------------------------------------------------------------------------
	

	
//-----------------------------------------------------------------------------------------		
	
/*===========================
 
 Unit Testing: FAN, THERMOMETER
============================*/	
	//@Test
	fun testThermometerAndFan(){
		`it.unibo`.utils.ParkingSlotsKb.setArea(true,true,true,true,true,true)
 		`it.unibo`.utils.ParkingSlotsKb.indoorFree   = true
		`it.unibo`.utils.ParkingSlotsKb.outdoorFree  = true
		
		runBlocking{
			
			val channelForUnitTesting = Channel<String>()
			testingObserver!!.addObserver(channelForUnitTesting, "null")
			var result = ""
			
			val thermoObs = CoapObserverForTesting("obsthermometer","ctxparkingservice", "thermometer", "5683")
			val fanObs = CoapObserverForTesting("obsfan","ctxparkingservice", "fan", "5683")
			thermoObs!!.addObserver(channelForUnitTesting, "temp")
			fanObs!!.addObserver(channelForUnitTesting, "fan")
			
			var fanON = false
			var tmp   = 0
			while(!fanON){
				result = channelForUnitTesting.receive()
				println("result = $result")
				if(result != "fan(ON)"){
					var temperature = result.substringAfter("(",result).substringBefore(")",result).toInt()
					assertTrue(temperature >= tmp)
					tmp = temperature
					delay(1000)
				}
				else{
					fanON = true
				}
			}
			while(fanON){
				result = channelForUnitTesting.receive()
				println("result = $result")
				if(result != "fan(OFF)"){
					var temperature = result.substringAfter("(",result).substringBefore(")",result).toInt()
					assertTrue(temperature <= tmp)
					tmp = temperature
					delay(1000)
				}
				else{
					fanON = false
				}
			}
			
		}
}
	
	
}	



