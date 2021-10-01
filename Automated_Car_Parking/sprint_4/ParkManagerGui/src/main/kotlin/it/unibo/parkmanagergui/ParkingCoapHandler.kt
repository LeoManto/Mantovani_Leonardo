package it.unibo.parkmanagergui

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import org.json.JSONObject
import org.springframework.web.util.HtmlUtils
import java.lang.Exception

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
            if (jsonContent.has("fan")){
                val fanRep = ResourceRep("fan" + HtmlUtils.htmlEscape( jsonContent.getString("fan"))  )
                sysUtil.colorPrint("ParkingCoapHandler | fan value=${fanRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, fanRep)
            }
            if (jsonContent.has("slotnum")){
                val slotRep = ResourceRep("slotnum" + HtmlUtils.htmlEscape( jsonContent.getString("slotnum"))  )
                sysUtil.colorPrint("ParkingCoapHandler | slot value=${slotRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, slotRep)
            }
            if (jsonContent.has("token")      ) {
                /* The resource shows something new  */
                //sysUtil.colorPrint("WebPageCoapHandler | value: $content simpMessagingTemplate=${controller.simpMessagingTemplate}", Color.BLUE)
                val tokenRep = ResourceRep("token" + HtmlUtils.htmlEscape( jsonContent.getString("token")))
                sysUtil.colorPrint("ParkingCoapHandler | token value=${tokenRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, tokenRep)
            }
        }catch(e:Exception){
            sysUtil.colorPrint("ParkingCoapHandler | ERROR=${content}", Color.RED)
        }
    }

    override fun onError() {
        System.err.println("ParkingCoapHandler  |  FAILED  ")
    }
}