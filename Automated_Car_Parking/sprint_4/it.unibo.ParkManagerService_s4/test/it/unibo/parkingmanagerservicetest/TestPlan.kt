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

		var parkingserviceObserver 	: CoapObserverForTesting ? = null	  
		var weightObserver   		: CoapObserverForTesting ? = null
		var trolleyObserver   		: CoapObserverForTesting ? = null	
		var sonarhandlerObserver   	: CoapObserverForTesting ? = null	
		var sonarsimulatorObserver  : CoapObserverForTesting ? = null	
		var outdoottimerObserver	: CoapObserverForTesting ? = null	
		
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
		outdoottimerObserver	= CoapObserverForTesting("testingTimerobs","ctxparkingservice","outdoortimer","5683")
		sonarsimulatorObserver 	= CoapObserverForTesting("testingSonarsimulatorobs","ctxparkingservice","outsonar","5683")
			
		
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}
		} 
  	}
	
	@After
	fun removeObs(){
		println("+++++++++ AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR")
		
		parkingserviceObserver!!.terminate()
		parkingserviceObserver = null
		weightObserver!!.terminate()
		weightObserver = null
		trolleyObserver!!.terminate()
		trolleyObserver = null
		sonarhandlerObserver!!.terminate()
		sonarhandlerObserver = null
		runBlocking{
			delay(2000)
		}
 	}

 
//----------------------------------------------------------------------------------------------------------------------------	

/*========================
 -	Type: Integration Test
 -  COMPLETE WORKFLOW
===========================*/
	
	//@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun completeWorkflow(){
		
		runBlocking{
			
			val channelForIndoorStart	= Channel<String>()
			val channelForOutdoorStart	= Channel<String>()
			val channelForTrolleyPosStart= Channel<String>()
			val channelForIndoor 		= Channel<String>()
			val channelForOutdoor 		= Channel<String>()
			val channelForParkingOp 	= Channel<String>()
			val channelForWeight 		= Channel<String>()
			val channelForSlots 		= Channel<String>()
			//val channelForTrolleyStat 	= Channel<String>()
			val channelForRobotPos		= Channel<String>()
			val channelForHome			= Channel<String>()
			val channelForAlarm			= Channel<String>()
			
			delay(1000)
			
			parkingserviceObserver!!.addObserver(channelForIndoorStart, "indoorAtStart")
			parkingserviceObserver!!.addObserver(channelForOutdoorStart, "outdoorAtStart")
			parkingserviceObserver!!.addObserver(channelForSlots, "freeslots")
			parkingserviceObserver!!.addObserver(channelForRobotPos, "posRobot")
			parkingserviceObserver!!.addObserver(channelForParkingOp, "slotnum")
			parkingserviceObserver!!.addObserver(channelForParkingOp, "receipt")
			parkingserviceObserver!!.addObserver(channelForHome, "movingTo")
			
			weightObserver!!.addObserver(channelForIndoor, "indoorS")
			weightObserver!!.addObserver(channelForWeight, "carWeight")
			
			sonarhandlerObserver!!.addObserver(channelForOutdoor, "outdoorS")
			
			outdoottimerObserver!!.addObserver(channelForAlarm, "alarm")
			
			parkingserviceObserver!!.addObserver(channelForParkingOp, "carslotnum")
		
			delay(5000)
			
			/*
			val processBuilder : ProcessBuilder = ProcessBuilder("pause.bat")
			processBuilder.start().waitFor()
			*/
										
//clientactor!!.forward("startManager","system(ready)","parkingmanagerservice")
			
			delay(20000)
			
			
			var indoorStatus = channelForIndoorStart.receive().substringAfter("(","-").substringBefore(")","-")
			println("TEST indoorAtStart: $indoorStatus")
			assertTrue(indoorStatus.equals("FREE"))
			
			var outdoorStatus = channelForOutdoorStart.receive().substringAfter("(","-").substringBefore(")","-")
			println("TEST outdoorStatus: $outdoorStatus")
			assertTrue(outdoorStatus.equals("FREE"))
			
			var slotsfree 		= channelForSlots.receive().substringAfter("(","-").substringBefore(")","-").toInt()
			println("TEST slotsfree: $slotsfree")
			assertTrue(slotsfree == 6)
			
			var robotPosition = channelForRobotPos.receive().substringAfter("(","-").substringBefore(")","-")
			println("TEST robotPosition: $robotPosition")
			assertTrue(robotPosition.equals("0,0"))
			
			delay(3000)
	
//clientactor!!.request("reqenter","reqenter(bob)","parkingmanagerservice")
			
			
			var slotnum 		= channelForParkingOp.receive().substringAfter("(","-").substringBefore(")","-").toInt()
			println("TEST slotnum: $slotnum")
			assertTrue(slotnum > 0 && slotnum < 7)
			
			delay(3000)
			
//clientactor!!.request("carenter","carenter(ok)","parkingmanagerservice")		
			
			var weight	= channelForWeight.receive().substringAfter("(","").substringBefore(")","").toInt()
			println("TEST weight: $weight")
			assertTrue(weight > 0)
			println("TEST weight: SUCCESS")
			
			
			indoorStatus 		= channelForIndoor.receive().substringAfter("(","").substringBefore(")","")
			println("TEST indoorStatus: $indoorStatus")
			assertTrue(indoorStatus.equals("BUSY"))
			println("TEST indoorStatus SUCCESS")
			
			var result = channelForRobotPos.receive()
			robotPosition = result.substringAfter("(","").substringBefore(")","")
			println("TEST robotPosition: $robotPosition")
			assertTrue(robotPosition.equals("indoor"))
			println("TEST robotPosition SUCCESS")
			
			result = channelForIndoor.receive()
			indoorStatus 		= result.substringAfter("(","").substringBefore(")","")
			println("TEST indoorStatus: $indoorStatus")
			assertTrue(indoorStatus.equals("FREE"))
			println("TEST indoorStatus SUCCESS")
			
			var receipt 		= channelForParkingOp.receive().substringAfter("(","-").substringBefore(")","-").toInt()
			println("TEST receipt: $receipt")
			assertTrue(receipt > 10000 && receipt < 70000)		
			
			result = channelForRobotPos.receive()
			robotPosition = result.substringAfter("(","").substringBefore(")","")
			println("TEST robotPosition: $robotPosition")
			assertTrue(robotPosition.equals("p$slotnum"))
			
			
			slotsfree 		= channelForSlots.receive().substringAfter("(","-").substringBefore(")","-").toInt()
			println("TEST slotsfree: $slotsfree")
			assertTrue(slotsfree == 5)
			
			
			delay(2000)
			
			var home 		= channelForHome.receive().substringAfter("(","-").substringBefore(")","-")
			println("TEST moving to HOME: $home")
			assertTrue(home.equals("HOME"))
			
			println("FINISH PARKING OP")

			delay(4000)
			
//clientactor!!.forward("pickup","pickup($receipt)","parkingmanagerservice")
			
			var carslotnum = channelForParkingOp.receive().substringAfter("(","-").substringBefore(")","-").toInt()
			println("TEST carslotnum: $carslotnum")
			assertTrue(carslotnum.equals(slotnum))
			println("TEST carslotnum SUCCESS")
			
			result = channelForRobotPos.receive()
			robotPosition = result.substringAfter("(","").substringBefore(")","")
			println("TEST robotPosition: $robotPosition")
			assertTrue(robotPosition.equals("p$carslotnum"))
			
			result = channelForRobotPos.receive()
			robotPosition = result.substringAfter("(","").substringBefore(")","")
			println("TEST robotPosition: $robotPosition")
			assertTrue(robotPosition.equals("outdoor"))
			
			outdoorStatus = channelForOutdoor.receive().substringAfter("(","").substringBefore(")","")
			println("TEST outdoorStatus: $outdoorStatus")
			assertTrue(outdoorStatus.equals("BUSY"))
			
			outdoorStatus = channelForOutdoor.receive().substringAfter("(","").substringBefore(")","")
			println("TEST outdoorStatus: $outdoorStatus")
			if(outdoorStatus.equals("FREE")){
				assertTrue(outdoorStatus.equals("FREE"))
			}
			else{
				var alarm = channelForAlarm.receive().substringAfter("(","").substringBefore(")","")
				assertTrue(alarm.equals("TIMEOUT"))
			}
			
			home = channelForHome.receive().substringAfter("(","-").substringBefore(")","-")
			println("TEST moving to HOME: $home")
			assertTrue(home.equals("HOME"))
			
			println("FINISH PICKING OP")

					
		}
 	}

	
//=========================================================================================================================	

@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun sonarTest(){
		
		runBlocking{
			var channelForSonar = Channel<String>()
			var channelForTimer = Channel<String>()
			sonarhandlerObserver!!.addObserver(channelForSonar, "sonarOutdoor")
			outdoottimerObserver!!.addObserver(channelForTimer, "alarm")
			
			delay(5000)
			
			myactor!!.forward("startsonar","start(sonar)","sonarhandler")
					 
			var outdoorStatus = channelForSonar.receive().substringAfter("(","-").substringBefore(")","-")
			assertTrue(outdoorStatus=="FREE")
			println("TEST outdoorStatus: $outdoorStatus")
			
			outdoorStatus = channelForSonar.receive().substringAfter("(","-").substringBefore(")","-")
			assertTrue(outdoorStatus.equals("BUSY"))
			println("TEST outdoorStatus: $outdoorStatus")
			
			var alarm = channelForTimer.receive().substringAfter("(","-").substringBefore(")","-")
			assertTrue(alarm.equals("TIMEOUT"))
			println("TEST alarm: $alarm")
		
		}
 	}	
}


