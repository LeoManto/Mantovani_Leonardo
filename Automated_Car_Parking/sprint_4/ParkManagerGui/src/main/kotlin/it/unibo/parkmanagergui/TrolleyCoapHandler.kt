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
    class TrolleyCoapHandler(val controller: HIController) : CoapHandler {

    override fun onLoad(response: CoapResponse) {
        val content: String = response.getResponseText()
        sysUtil.colorPrint("TrolleyCoapHandler | response content=$content", Color.GREEN )
        //response={"sonarvalue":"D"} or {"info":"somevalue"}

        try {
            val jsonContent = JSONObject(content)
            if (jsonContent.has("path")){
                val pathRep = ResourceRep("path" + HtmlUtils.htmlEscape( jsonContent.getString("path"))  )
                sysUtil.colorPrint("TrolleyCoapHandler | path value=${pathRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, pathRep)
            }
            if (jsonContent.has("status")){
                val statusRep = ResourceRep("status" + HtmlUtils.htmlEscape( jsonContent.getString("status"))  )
                sysUtil.colorPrint("TrolleyCoapHandler | status value=${statusRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, statusRep)
            }

        }catch(e:Exception){
            sysUtil.colorPrint("TrolleyCoapHandler | ERROR=${content}", Color.RED)
        }
    }

    override fun onError() {
        System.err.println("TrolleyCoapHandler  |  FAILED  ")
    }
}