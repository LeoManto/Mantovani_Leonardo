package it.unibo.webspring.demo

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import okhttp3.internal.wait

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class HumanInterfaceController {

    init {
        ClientResource.start()
    }


    @Value("\${human.logo}")
    var appName: String?    = null
    var applicationModelRep = "waiting"

    @GetMapping("/")    //defines that the method handles GET requests.
    fun entry(model: Model): String {
        model.addAttribute("arg", appName+"xxx")
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
    fun req_enter(model: Model) : String {
        sysUtil.colorPrint("HumanInterfaceController | requestType: reqenter ", Color.GREEN)
        ClientResource.send("reqenter")
        return "redirect:waitSlotnum"
    }


    @GetMapping("/waitSlotnum")
    fun show_slotnum(model: Model) : String {
        var slot = ""
        model.addAttribute("advise","Waiting for SLOTNUM")
        do {
            slot = ClientResource.getCurrentSlot()
        }while( slot.equals("") )
        model.addAttribute("advise", "SLOTNUM: $slot . \n Click on \"Car In\" when you're in INDOOR-area")
        return  "clientRobotGui"
    }


    @PostMapping("/carenter")
    fun car_enter(model: Model) : String {
        sysUtil.colorPrint("HumanInterfaceController | requestType: carenter ", Color.GREEN)
        ClientResource.send("carenter")
        var advise = "Please wait until your car is parked"
        model.addAttribute("advise", advise)
        return  "redirect:waitToken"
    }


    @GetMapping("/waitToken")
    fun show_token(model: Model) : String {
        var token = ""
        model.addAttribute("advise","Waiting for TOKEN-ID")
        do {
            token = ClientResource.getCurrentToken()
        }while( token.equals("") )
        model.addAttribute("advise","Your TOKEN-ID is $token")
        return "clientRobotGui"
        }


    @PostMapping("/pickup")
    fun pickup(model: Model, @RequestParam(name = "token") token : String) : String {
        sysUtil.colorPrint("HumanInterfaceController | requestType: pickup ", Color.GREEN)
        ClientResource.send("pickup", token )
        //ClientResource.changeAdvise("Token sended: $token")
        var advise = "Token sended: $token. Check that it's correct!"
        model.addAttribute("advise", advise)
        return  "clientRobotGui"
    }

    /*
    @PostMapping("/slot")
    fun slotnum(model: Model, @RequestParam(name = "slot") slotnum : String) : String {
        println("PARAMM $slotnum")
        model.addAttribute("advise","SLOTNUM: $slotnum")
        return  "clientRobotGui"
    }
    */


}
