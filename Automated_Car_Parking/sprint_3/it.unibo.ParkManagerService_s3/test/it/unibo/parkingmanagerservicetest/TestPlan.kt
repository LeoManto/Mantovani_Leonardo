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


//ORDINARE I TEST, COME???

class TestPlan {
	
		companion object tester {

		var testingObserver   : CoapObserverForTesting ? = null
		var trolleyObserver   : CoapObserverForTesting ? = CoapObserverForTesting("testingTrolleyobs","ctxparkingservice","trolley","5683")
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
		trolleyObserver!!.terminate()
		testingObserver = null
		trolleyObserver = null
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
			val channelForObserver = Channel<String>()
			val channelForObserver2 = Channel<String>()
			
			
			trolleyObserver!!.addObserver(channelForObserver, "finished")
			testingObserver!!.addObserver(channelForObserver2, "receipt")
			
			clientactor!!.request("reqenter","reqenter(bob)","parkingmanagerservice")
			
			
			delay(2000)
		
			//--------------------------------------------------------------------------------
			
			clientactor!!.request("carenter","carenter(ok)","parkingmanagerservice")
			
			var result = channelForObserver.receive()
			println("+++++++++ trolleyInIndoor RESULT=$result +++++++++")
			position = result.substringAfter("(",result).substringBefore(")",result)
			assertEquals("indoor", position)
			
			var result2 = channelForObserver2.receive()
			var token = result2.substringAfter("(",result2).substringBefore(")",result2).toInt()
			println("token = $token")
			
			delay(4000)
			//----------------------------------------------------------------------------------
			
			result = channelForObserver.receive()
			println("+++++++++ trolleyToParkingSlot RESULT=$result +++++++++")
			position = result.substringAfter("(",result).substringBefore(")",result)
			assertEquals("p1", position)
			
			delay(3000)
			
			//-----------------------------------------------------------------------------------
			
			clientactor!!.forward("pickup","pickup($token)","parkingmanagerservice")
			
			result = channelForObserver.receive()
			println("+++++++++ trolleyToParkingSlot(PICKUP) RESULT=$result +++++++++")
			position = result.substringAfter("(",result).substringBefore(")",result)
			assertEquals("p1", position)
			
			delay(4000)
			
			//-----------------------------------------------------------------------------------
			
			result = channelForObserver.receive()
			println("+++++++++ trolleyInOutdoor RESULT=$result +++++++++")
			position = result.substringAfter("(",result).substringBefore(")",result)
			assertEquals("outdoor", position)
			
			delay(6000)
			
			//-----------------------------------------------------------------------------------
			
			result = channelForObserver.receive()
			println("+++++++++ trolleyInHome RESULT=$result +++++++++")
			position = result.substringAfter("(",result).substringBefore(")",result)
			assertEquals("home", position)
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
			val channelForObserver = Channel<String>()
			trolleyObserver!!.addObserver(channelForObserver, "stepdone")
 
			clientactor!!.request("reqenter","reqenter(bob)","parkingmanagerservice")
			
			println("+++++++++ testreqenter ")	
			
			var result = channelForObserver.receive()
			println("+++++++++ testreqenter RESULT=$result +++++++++")
			
			
			delay(2000)
			
			//--------------------------------------------------------------------------------
			
			testingObserver!!.addObserver(channelForObserver, "receipt")
			
			clientactor!!.request("carenter","carenter(ok)","parkingmanagerservice")
			
			println("+++++++++ testcarenter")	
			result = channelForObserver.receive()
			
			delay(4000)
			//----------------------------------------------------------------------------------
			
			testingObserver!!.addObserver(channelForObserver!!, "pickup")

			println("+++++++++ testpickup")
			result = channelForObserver.receive()
			println("+++++++++ testpickup RESULT=$result +++++++++")
			assertTrue(result.substringAfter("(",result).substringBefore(")",result).toInt() > 0)
			
			//-----------------------------------------------------------------------------------
 			
			
			
	  	}
 	}
	
/*========================
 -	Type: Unit Test
 -  TRAVEL TO ONE DESTINATION
===========================*/
	
	@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun travelToDestination(){

		runBlocking{
			val channelTmp  = Channel<String>()
			val channelForObserver  = Channel<String>()
			val channelForObserver2 = Channel<String>()

			delay(3000)
			
			myactor!!.forward("move","move(p5)","trolley")
			testingObserver!!.addObserver(channelTmp,"null")
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
			assertEquals(expectedpath, realpath)
 			
	  	}
 	}
	
/*========================
 -	Type: Unit Test
 -  DIRECTION TEST (LEO)
===========================*/
	
	//@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun directionTest(){
		
 		
	
		runBlocking{
			
			val channelForObserver = Channel<String>()
			val channelForObserver2 = Channel<String>()
			
			testingObserver!!.addObserver(channelForObserver2, "receipt")
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
 -  STATUS CHECK (LEO)
===========================*/

	//@Test
 	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun statusCheck(){
		
 		
	
		runBlocking{
			
			val channelForObserver = Channel<String>()
			val channelForObserver2 = Channel<String>()
			
			testingObserver!!.addObserver(channelForObserver2, "receipt")
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



