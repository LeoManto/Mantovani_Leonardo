package it.unibo.parkmanagergui

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
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

    var coapForParking    = CoapSupport("coap://localhost:5683", "ctxparkingservice/parkingmanagerservice")
    var coapForTrolley    = CoapSupport("coap://127.0.0.1:5685", "ctxtrolley/trolley")
    var coapForFan        = CoapSupport("coap://localhost:5683", "ctxparkingservice/fan")

    /*
     * Update the page vie socket.io when the application-resource changes.
     * See also https://www.baeldung.com/spring-websockets-send-message-to-user
     */

    @Autowired
    var simpMessagingTemplate : SimpMessagingTemplate? = null

    //var ws         = IssWsHtttpJavaSupport.creaeForWs ("localhost:8083")
    init{
        sysUtil.colorPrint("HumanInterfaceController | INIT", Color.GREEN)
        //connQakSupport = connQakCoap()
        //connQakSupport.createConnection()
        coapForParking.observeResource(ParkingCoapHandler(this) )
        coapForFan.observeResource(ParkingCoapHandler(this) )
        coapForTrolley.observeResource(TrolleyCoapHandler(this))

    }


    @GetMapping("/")    //defines that the method handles GET requests.
    fun entry(model: Model): String {
        model.addAttribute("arg", appName )
        sysUtil.colorPrint("HIController | entry model=$model", Color.GREEN)
        return  "ManagerGui"
    }


}