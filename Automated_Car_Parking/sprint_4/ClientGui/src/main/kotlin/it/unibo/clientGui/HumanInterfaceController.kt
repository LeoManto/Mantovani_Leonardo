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

    @Value("Gui for interaction")
    var appName: String?    = null

    var applicationModelRep = "waiting"

    @Autowired
    var  simpMessagingTemplate : SimpMessagingTemplate? = null


    init {
        ClientResource.start(this)

    }

    @GetMapping("/")    //defines that the method handles GET requests.
    fun entry(model: Model): String {
        model.addAttribute("arg", appName)
        model.addAttribute("advise", "Welcome in the best automated parking ever, Click on \"Enter Request to start\"")
        println("HumanInterfaceController | entry model=$model")
        return "clientRobotGui"
    }

    @GetMapping("/model")
    @ResponseBody   //With this annotation, the String returned by the methods is sent to the browser as plain text.
    fun  homePage( model: Model) : String{
        model.addAttribute("arg", appName)
        sysUtil.colorPrint("HumanInterfaceController | homePage model=$model", Color.GREEN)
        return String.format("HumanInterfaceController text normal state= $applicationModelRep"  );
    }

    @PostMapping("/reqenter")
    fun req_enter(viewmodel : Model) : String {
        sysUtil.colorPrint("HumanInterfaceController | requestType: reqenter ", Color.GREEN)
        ClientResource.send("reqenter")
        var advise = "Please wait until your car is parked"
        viewmodel.addAttribute("advise", advise)
        return "clientRobotGui"
    }

    @PostMapping("/carenter")
    fun car_enter(model: Model) : String {
        sysUtil.colorPrint("HumanInterfaceController | requestType: carenter ", Color.GREEN)
        ClientResource.send("carenter")
        var advise = "Please wait until your car is parked"
        model.addAttribute("advise", advise)
        return  "clientRobotGui"
    }

    @PostMapping("/pickup")
    fun pickup(model: Model, @RequestParam(name = "token") token : String) : String {
        sysUtil.colorPrint("HumanInterfaceController | requestType: pickup ", Color.GREEN)
        ClientResource.send("pickup", token)
        //ClientResource.changeAdvise("Token sended: $token")
        var advise = "Token sended: $token. Wait untill yout car is in OUTDOOR area.\nGoodbye"
        model.addAttribute("advise", advise)
        return  "clientRobotGui"
    }
}
