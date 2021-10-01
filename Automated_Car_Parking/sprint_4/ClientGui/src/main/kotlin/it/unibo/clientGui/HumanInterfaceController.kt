package it.unibo.clientGui

import com.andreapivetta.kolor.Color
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.ApplMessageType
import it.unibo.actor0.sysUtil
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.HtmlUtils

@Controller
class HumanInterfaceController {

    @Value("\${human.logo}")
    var appName: String?    = null


    //var coap    = CoapSupport("coap://localhost:5683", "ctxparkingservice/parkingmanagerservice")

    var applicationModelRep = "waiting"

    @Autowired
    var  simpMessagingTemplate : SimpMessagingTemplate? = null


    init {
        ClientResource.start(this)

        //coap.observeResource( WebPageCoapHandler(this) )
    }




    @GetMapping("/")    //defines that the method handles GET requests.
    fun entry(model: Model): String {
        model.addAttribute("arg", appName)
        model.addAttribute("advise", "Welcome in the best automated parking ever, Click on \"Enter Request to start\"")
        println("HumanInterfaceController | entry model=$model")
        return "clientRobotGui"
    }

    /*
    Spring provides a Model object which can be passed into the controller.
    You can configure this model object via the addAttribute(key, value) method.
     */


    @GetMapping("/model")
    @ResponseBody   //With this annotation, the String returned by the methods is sent to the browser as plain text.
    fun  homePage( model: Model) : String{
        model.addAttribute("arg", appName)
        sysUtil.colorPrint("HumanInterfaceController | homePage model=$model", Color.GREEN)
        return String.format("HumanInterfaceController text normal state= $applicationModelRep"  );
    }

    //@RequestMapping methods assume @ResponseBody semantics by default.
    //https://springframework.guru/spring-requestmapping-annotation/


    @PostMapping("/reqenter")
    fun req_enter(viewmodel : Model) : String {
        sysUtil.colorPrint("HumanInterfaceController | requestType: reqenter ", Color.GREEN)
        ClientResource.send("reqenter")
        var advise = "Please wait until your car is parked"
        viewmodel.addAttribute("advise", advise)
        return "clientRobotGui"
    }

    /*@GetMapping("/waitSlotnum")
    fun show_slotnum(model: Model) : String {
        var slot = ""
        model.addAttribute("advise","Waiting for SLOTNUM")
        do {
            slot = ClientResource.getCurrentSlot()
        }while( slot.equals("") )
        model.addAttribute("advise", "SLOTNUM: $slot . \n Click on \"Car In\" when you're in INDOOR-area")
        return  "clientRobotGui"
    }*/


    @PostMapping("/carenter")
    fun car_enter(model: Model) : String {
        sysUtil.colorPrint("HumanInterfaceController | requestType: carenter ", Color.GREEN)
        ClientResource.send("carenter")
        var advise = "Please wait until your car is parked"
        model.addAttribute("advise", advise)
        return  "clientRobotGui"
    }


    /*@GetMapping("/waitToken")
    fun show_token(model: Model) : String {
        var token = ""
        model.addAttribute("advise","Waiting for TOKEN-ID")
        do {
            token = ClientResource.getCurrentToken()
        }while( token.equals("") )
        model.addAttribute("advise","Your TOKEN-ID is $token")
        return "clientRobotGui"
        }*/


    @PostMapping("/pickup")
    fun pickup(model: Model, @RequestParam(name = "token") token : String) : String {
        sysUtil.colorPrint("HumanInterfaceController | requestType: pickup ", Color.GREEN)
        ClientResource.send("pickup", token)
        //ClientResource.changeAdvise("Token sended: $token")
        var advise = "Token sended: $token. Check that it's correct!"
        model.addAttribute("advise", advise)
        return  "clientRobotGui"
    }


    @PostMapping("/slot")
    fun slotnum(model: Model, @RequestParam(name = "slotnum") slotnum : String) : String {
        //println("PARAMM $slotnum")
        model.addAttribute("advise","SLOTNUM: $slotnum")
        return  "clientRobotGui"
    }


    @PostMapping("/token")
    fun token(model: Model, @RequestParam(name = "tokenid") tokenid : String) : String {
        //println("PARAMM $tokenid")
        model.addAttribute("advise","TOKEN ID: $tokenid")
        return  "clientRobotGui"
    }

    /*
    @PostMapping("/reqenter")
    fun req_enter(viewmodel : Model) : String {
        sysUtil.colorPrint("HumanInterfaceController | requestType: reqenter ", Color.GREEN)
        val msg = ApplMessage("reqenter", ApplMessageType.request.toString(), "client", "parkingmanagerservice", "client", "1")
        //coap.updateResource(msg.toString())
        Thread.sleep(400);  //QUITE A LONG TIME ...
        //val resourceRep = coap.readResource()
        //val resRep      = ResourceRep( ""+ HtmlUtils.htmlEscape(resourceRep))
        //sysUtil.colorPrint("HumanInterfaceController | resRep:$resRep", Color.BLUE)
        var advise = "Please wait until your car can be parked"
        //viewmodel.addAttribute("advise", "${resourceRep}")
        return "clientRobotGui"
    }

    @PostMapping("/carenter")
    fun car_enter(viewmodel : Model) : String {
        sysUtil.colorPrint("HumanInterfaceController | requestType: carenter ", Color.GREEN)
        val m = ApplMessage("carenter", ApplMessageType.request.toString(), "client", "parkingmanagerservice", "client", "1")
        //coap.updateResource(m.toString())
        Thread.sleep(400);  //QUITE A LONG TIME ...
        //val resourceRep = coap.readResource()
        //val resRep      = ResourceRep( ""+ HtmlUtils.htmlEscape(resourceRep))
        //sysUtil.colorPrint("HumanInterfaceController | resRep:$resRep", Color.BLUE)
        var advise = "Please wait until your car is parked"
        viewmodel.addAttribute("advise", advise)
        return "clientRobotGui"
    }
*/
}
