package it.unibo.parkmanagergui

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import org.json.JSONObject
import org.springframework.web.util.HtmlUtils
import java.lang.Exception
import javax.management.ValueExp
import kotlin.reflect.jvm.internal.ReflectProperties

/*
An object of this class is registered as observer of the resource
 */
    class ParkingCoapHandler(val controller: HIController) : CoapHandler {
    override fun onLoad(response: CoapResponse) {
        val content: String = response.getResponseText()
        sysUtil.colorPrint("ParkingCoapHandler | response content=$content", Color.GREEN )
        //response={"sonarvalue":"D"} or {"info":"somevalue"}

        try {
            val jsonContent = JSONObject(content)
            if (jsonContent.has("indoorStatus")){
                val indoorRep = ResourceRep("indoorStatus" + HtmlUtils.htmlEscape( jsonContent.getString("indoorStatus"))  )
                sysUtil.colorPrint("ParkingCoapHandler | indoor value=${indoorRep.content}", Color.BLUE)
                ValuesForGui.indoor = indoorRep
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, indoorRep)
            }
            else if (jsonContent.has("outdoorStatus")){
                val outdoorRep = ResourceRep("outdoorStatus" + HtmlUtils.htmlEscape( jsonContent.getString("outdoorStatus"))  )
                sysUtil.colorPrint("ParkingCoapHandler | outdoor value=${outdoorRep.content}", Color.BLUE)
                ValuesForGui.outdoor = outdoorRep
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, outdoorRep)
            }
            else if (jsonContent.has("weight")){
                val weightRep = ResourceRep("weight" + HtmlUtils.htmlEscape( jsonContent.getString("weight"))  )
                sysUtil.colorPrint("ParkingCoapHandler | weight value=${weightRep.content}", Color.BLUE)
                ValuesForGui.weight = weightRep
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, weightRep)
            }
            else if (jsonContent.has("fan")){
                val fanRep = ResourceRep("fan" + HtmlUtils.htmlEscape( jsonContent.getString("fan"))  )
                sysUtil.colorPrint("ParkingCoapHandler | fan value=${fanRep.content}", Color.BLUE)
                ValuesForGui.fan = fanRep
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, fanRep)
            }
            else if (jsonContent.has("temp")) {
                /* The resource shows something new  */
                //sysUtil.colorPrint("WebPageCoapHandler | value: $content simpMessagingTemplate=${controller.simpMessagingTemplate}", Color.BLUE)
                val tempRep = ResourceRep("temp" + HtmlUtils.htmlEscape( jsonContent.getString("temp")))
                ValuesForGui.temp = tempRep
                sysUtil.colorPrint("ParkingCoapHandler | temp value=${tempRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, tempRep)
            }

        }catch(e:Exception){
            sysUtil.colorPrint("ParkingCoapHandler | ERROR=${content}", Color.RED)
        }
    }

    override fun onError() {
        System.err.println("ParkingCoapHandler  |  FAILED  ")
    }
}