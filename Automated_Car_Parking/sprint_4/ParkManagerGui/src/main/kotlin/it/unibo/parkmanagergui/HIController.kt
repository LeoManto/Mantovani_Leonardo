package it.unibo.parkmanagergui

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.beans.factory.annotation.Autowired

/*
The HIController USES the sonarresources via CoAP
See also it.unibo.boundaryWalk/userdocs/websocketInteraction.html
https://spring.io/guides/gs/messaging-stomp-websocket/

 */


@Controller
class HIController {
    @Value("\${human.logo}")
    var appName: String?    = null

    var coapForParking      = CoapSupport("coap://localhost:5683", "ctxparkingservice/parkingmanagerservice")
    var coapForTrolley      = CoapSupport("coap://127.0.0.1:5685", "ctxtrolley/trolley")
    var coapForFan          = CoapSupport("coap://localhost:5683", "ctxparkingservice/fan")
    var coapForThermometer  = CoapSupport("coap://localhost:5683", "ctxparkingservice/thermometer")
    var coapForWeightsensor = CoapSupport("coap://localhost:5683", "ctxparkingservice/weightsensor")
    //var coapForOutdoor      = CoapSupport("coap://localhost:5683", "ctxparkingservice/sonarhandler")
    //IF SIMULATE:
    var coapForOutdoor      = CoapSupport("coap://localhost:5683", "ctxparkingservice/outsonar")


    /*
     * Update the page vie socket.io when the application-resource changes.
     * See also https://www.baeldung.com/spring-websockets-send-message-to-user
     */

    @Autowired
    var simpMessagingTemplate : SimpMessagingTemplate? = null

    //var ws         = IssWsHtttpJavaSupport.creaeForWs ("localhost:8083")
    init{

        ValuesForGui.setController(this)
        SenderToPark.start(this)

        sysUtil.colorPrint("HumanInterfaceController | INIT", Color.GREEN)
        //connQakSupport = connQakCoap()
        //connQakSupport.createConnection()
        coapForParking.observeResource(ParkingCoapHandler(this) )
        coapForFan.observeResource(ParkingCoapHandler(this) )
        coapForThermometer.observeResource(ParkingCoapHandler(this))
        coapForTrolley.observeResource(TrolleyCoapHandler(this))
        coapForWeightsensor.observeResource(ParkingCoapHandler(this))
        coapForOutdoor.observeResource(ParkingCoapHandler(this))

    }

    @GetMapping("/")    //defines that the method handles GET requests.
    fun entry(model: Model): String {
        model.addAttribute("arg", appName )
        sysUtil.colorPrint("HIController | entry model=$model", Color.GREEN)
        return  "ManagerGui"
    }


    @PostMapping("/stop")
     fun sendStop(model: Model) : String {
        SenderToPark.send("stop")
    return  "ManagerGui"
    }


    @PostMapping("/resume")
     fun sendResume(model: Model) : String {
        SenderToPark.send("resume")
        return  "ManagerGui"
    }


}