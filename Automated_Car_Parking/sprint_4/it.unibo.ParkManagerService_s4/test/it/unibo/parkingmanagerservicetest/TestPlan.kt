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
import itunibo.planner.plannerUtil
import unibo.robot.robotSupport
import org.json.JSONObject
import java.lang.NullPointerException
import java.lang.ProcessBuilder

//ORDINARE I TEST, COME???

class TestPlan {
	 
		companion object tester {

		var parkingObserver 		: CoapObserverForTesting ? = null	
		var parkingserviceObserver 	: CoapObserverForTesting ? = null	  
		var weightObserver   		: CoapObserverForTesting ? = null
		var trolleyObserver   		: CoapObserverForTesting ? = null	
		var sonarhandlerObserver   	: CoapObserverForTesting ? = null	
		var sonarsimulatorObserver  : CoapObserverForTesting ? = null	
			
		
		var systemStarted         = false
		val channelSyncStart      = Channel<String>()
		var myactor           : ActorBasic? = null
		var clientactor           : ActorBasic? = null
		var counter               = 1
	
		var position =	""
		var status =	""
		
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
		
	    println("+++++++++ BEFOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE")
		parkingserviceObserver 	= CoapObserverForTesting("testingServiceobs","ctxparkingservice","parkingmanagerservice","5683")  
		weightObserver   		= CoapObserverForTesting("testingWeightobs","ctxparkingservice","weightsensor","5683")
		trolleyObserver   		= CoapObserverForTesting("testingTrolleyobs","ctxparkingservice","trolley","5683")
		sonarhandlerObserver   	= CoapObserverForTesting("testingSonarhandlerobs","ctxparkingservice","sonarhandler","5683")
		sonarsimulatorObserver 	= CoapObserverForTesting("testingSonarsimulatorobs","ctxparkingservice","outsonar","5683")
			
		
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}
		} 
		if( parkingObserver == null)
			parkingObserver = CoapObserverForTesting("obstesting${counter++}")
  	}
	
	@After
	fun removeObs(){
		println("+++++++++ AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${parkingObserver!!.name}")

		parkingObserver!!.terminate()
		parkingObserver = null
		runBlocking{
			delay(2000)
		}
 	}

 
//----------------------------------------------------------------------------------------------------------------------------	

/*========================
 -	Type: Integration Test
 -  COMPLETE WORKFLOW
===========================*/
	
	@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun completeWorkflow(){
		
		runBlocking{
			val channelForIndoor 		= Channel<String>()
			val channelForOutdoor 		= Channel<String>()
			val channelForParkingOp 	= Channel<String>()
			val channelForWeight 		= Channel<String>()
			val channelForSlots 		= Channel<String>()
			val channelForTrolleyStat 	= Channel<String>()
			val channelForTrolleyPos	= Channel<String>()
			val channelForSystem		= Channel<String>()

			parkingserviceObserver!!.addObserver(channelForSystem, "systemready")
			parkingserviceObserver!!.addObserver(channelForSlots, "freeslots")
			parkingserviceObserver!!.addObserver(channelForParkingOp, "slotnum")
			parkingserviceObserver!!.addObserver(channelForIndoor, "indoor")
			
			delay(10000)
			
			val s = arrayOf("cmd.exe", "/c", ".\\test.bat")
			Runtime.getRuntime().exec(s)						
			
			/*
			val processBuilder : ProcessBuilder = ProcessBuilder(".\\test.bat")
			processBuilder.start().waitFor()
			*/
			
			//clientactor!!.forward("startManager","system(ready)","parkingmanagerservice")

			
			println("TEST ${channelForSystem.receive()}")
			
			delay(3000)
	
			//clientactor!!.request("reqenter","reqenter(bob)","parkingmanagerservice")
			
			var slotsfree 		= channelForSlots.receive().substringAfter("(","-").substringBefore(")","-").toInt()
			println("TEST slotsfree: $slotsfree")
			assertTrue(slotsfree > 0 && slotsfree < 7)
			
			var slotnum 		= channelForParkingOp.receive().substringAfter("(","-").substringBefore(")","-").toInt()
			assertTrue(slotnum > 0 && slotnum < 7)
			println("TEST slotnum: $slotnum")
			
			delay(3000)
			//--------------------------------------------------------------------------------
			
			//clientactor!!.request("carenter","carenter(ok)","parkingmanagerservice")
			
			weightObserver!!.addObserver(channelForIndoor, "indoor")
			weightObserver!!.addObserver(channelForWeight, "carWeight")
			trolleyObserver!!.addObserver(channelForTrolleyPos, "endPath")
			parkingserviceObserver!!.addObserver(channelForParkingOp, "receipt")
			
			var weight	= channelForWeight.receive().substringAfter("(","").substringBefore(")","").toInt()
			assertTrue(weight > 0)
			println("TEST weight: $weight")
			
			var indoor 		= channelForIndoor.receive().substringAfter("(","").substringBefore(")","").toString()
			assertTrue(indoor.equals("BUSY"))
			println("TEST indoor: $indoor")
			
			
			var posRobot = channelForTrolleyPos.receive().substringAfter("(","").substringBefore(")","").toString()
			assertTrue(posRobot.equals("indoor"))
			println("TEST pos Robo2t: $posRobot")
			
			var token = channelForParkingOp.receive().substringAfter("(","").substringBefore(")","").toInt()
			assertTrue(token > 10000 && token < 70000 )
			println("TEST token: $token")
			
			posRobot = channelForTrolleyPos.receive().substringAfter("(","").substringBefore(")","").toString()
			assertTrue(posRobot.equals("p${slotnum.toString()[0]}"))
			println("TEST pos Robot3: $posRobot")

			
			delay(5000)
			println("FINISH PARKING OP")
			//FINISH PARKING OP
			//----------------------------------------------------------------------------------

			try{
				sonarsimulatorObserver!!.addObserver(channelForOutdoor,"outdoor")
			}catch(e: NullPointerException){
				null
			}
			sonarhandlerObserver!!.addObserver(channelForOutdoor, "outdoor")
			
			clientactor!!.forward("pickup","pickup($token)","parkingmanagerservice")
			
			posRobot = channelForTrolleyPos.receive().substringAfter("(","").substringBefore(")","").toString()
			assertTrue(posRobot.equals("p${token.toString()[0]}"))
			
			posRobot = channelForTrolleyPos.receive().substringAfter("(","").substringBefore(")","").toString()
			assertTrue(posRobot.equals("outdoor"))
			
			var outdoor 		= channelForOutdoor.receive().substringAfter("(","").substringBefore(")","").toString()
			assertTrue(outdoor.equals("BUSY"))
			
			outdoor 		= channelForOutdoor.receive().substringAfter("(","").substringBefore(")","").toString()
			assertTrue(outdoor.equals("FREE"))
			
			delay(4000)			
			
	  	}
 	}
	
/*========================
 -	Type: Unit Test
 -  SINGLE MOVE 
===========================*/
	
	//@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun singleMove(){
		
		runBlocking{
			val channelTmp = Channel<String>()
 
			val move = "w"
			
			delay(3000)
			
			delay(robotSupport.move(move))
			robotSupport.move("h")
			
			delay(1000)
			
			plannerUtil.updateMap(  "$move" )
			
			println("+++++++++ testSingleMove ")	
			
			var curPos = plannerUtil.get_curPos().toString()
			
			parkingserviceObserver!!.addObserver(channelTmp,"null")
			trolleyObserver!!.addObserver(channelTmp,"null")
			
			assertEquals(curPos,"(0, 1)")			
	  	}
 	}
	
/*========================
 -	Type: Unit Test
 -  TRAVEL TO ONE DESTINATION
===========================*/
	
	//@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun travelToDestination(){

		runBlocking{
			val channelTmp  = Channel<String>()
			val channelForObserver  = Channel<String>()
			val channelForObserver2 = Channel<String>()

			delay(3000)
			
			myactor!!.forward("move","move(p5)","trolley")
			trolleyObserver!!.addObserver(channelForObserver, "path")
			trolleyObserver!!.addObserver(channelForObserver2, "stepdone")
			
			println("+++++++++ testTraveToDestination ")	
			
			var result = channelForObserver.receive()
			var expectedpath : String = result.substringAfter("(",result).substringBefore(")",result).toString()
			expectedpath = expectedpath.replace("[","").replace("]","").replace(",","").replace(" ","")
			println("EXP__PATHHHHHHHH: $expectedpath")
			//--------------------------------------------------------------------------------
			
			var realpath = ""
			var len = expectedpath.length
			println("LENGTH....$len")
			for(i in 1..expectedpath.length) {	
			result = channelForObserver2.receive()
			result = result.substringAfter("(",result).substringBefore(")",result)
			realpath = realpath.plus( result )
			}
			
			println("REAL__PATHHHHHHHH: $realpath")
 			parkingserviceObserver!!.addObserver(channelTmp,"null")
			assertEquals(expectedpath, realpath)
			
	  	}
 	}
	
/*========================
 -	Type: Unit Test
 -  DIRECTION TEST
===========================*/
	
	//@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun directionTest(){
		
 		
	
		runBlocking{
			
			val channelForObserver = Channel<String>()
			val channelForObserver2 = Channel<String>()
			
			parkingserviceObserver!!.addObserver(channelForObserver2, "receipt")
			trolleyObserver!!.addObserver(channelForObserver, "finished")
			
			
			myactor!!.forward("move","move(indoor)","trolley")
			
			
			var result = channelForObserver.receive()
			var direction = plannerUtil.getDirection()
			println("+++++++++ trolleyInIndoor RESULT=$result - DIRECTION=$direction+++++++++")
			position = result.substringAfter("(",result).substringBefore(")",result)
			assertEquals("indoor", position)
			assertEquals("downDir", plannerUtil.getDirection())
			
			
			delay(4000)
			//----------------------------------------------------------------------------------
			
			myactor!!.forward("move","move(p1)","trolley")
			
			
			result = channelForObserver.receive()
			direction = plannerUtil.getDirection()
			println("+++++++++ trolleyInP1 RESULT=$result - DIRECTION=$direction+++++++++")
			position = result.substringAfter("(",result).substringBefore(")",result)
			assertEquals("p1", position)
			assertEquals("leftDir", plannerUtil.getDirection())
			
			
			delay(4000)
			//----------------------------------------------------------------------------------
			
			myactor!!.forward("move","move(outdoor)","trolley")
			
			
			result = channelForObserver.receive()
			direction = plannerUtil.getDirection()
			println("+++++++++ trolleyInOutdoor RESULT=$result - DIRECTION=$direction+++++++++")
			position = result.substringAfter("(",result).substringBefore(")",result)
			assertEquals("outdoor", position)
			assertEquals("upDir", plannerUtil.getDirection())
			
			
			delay(4000)
			//----------------------------------------------------------------------------------
			
			myactor!!.forward("move","move(p2)","trolley")
			
			result = channelForObserver.receive()
			direction = plannerUtil.getDirection()
			println("+++++++++ trolleyInP2 RESULT=$result - DIRECTION=$direction+++++++++")
			position = result.substringAfter("(",result).substringBefore(")",result)
			assertEquals("p2", position)
			assertEquals("rightDir", plannerUtil.getDirection())
			
			delay(4000)
			//----------------------------------------------------------------------------------
			
			myactor!!.forward("move","move(home)","trolley")
			
			result = channelForObserver.receive()
			direction = plannerUtil.getDirection()
			println("+++++++++ trolleyInHome RESULT=$result - DIRECTION=$direction+++++++++")
			position = result.substringAfter("(",result).substringBefore(")",result)
			assertEquals("home", position)
			assertEquals("downDir", plannerUtil.getDirection())
	  	}
 	}
	
/*========================
 -	Type: Unit Test
 -  STATUS CHECK
===========================*/

	//@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun statusCheck(){
		
		runBlocking{
			
			val channelForObserver = Channel<String>()
			val channelForObserver2 = Channel<String>()
			
			parkingserviceObserver!!.addObserver(channelForObserver2, "receipt")
			trolleyObserver!!.addObserver(channelForObserver, "status")
			
			var result = channelForObserver.receive()
			println("+++++++++ trolleyInitialState RESULT=$result+++++++++")
			status = result.substringAfter("(",result).substringBefore(")",result)
			assertEquals("IDLE", status)
			
			delay(4000)
			//----------------------------------------------------------------------------------
			myactor!!.forward("move","move(indoor)","trolley")
			
			
			result = channelForObserver.receive()
			println("+++++++++ trolleyWorkingState RESULT=$result+++++++++")
			status = result.substringAfter("(",result).substringBefore(")",result)
			assertEquals("WORKING", status)
			
			
			delay(4000)
			//----------------------------------------------------------------------------------
			
			result = channelForObserver.receive()
			println("+++++++++ trolleyEndWorkState RESULT=$result+++++++++")
			status = result.substringAfter("(",result).substringBefore(")",result)
			assertEquals("IDLE", status)
			
	  	}
 	}
 	
}	



