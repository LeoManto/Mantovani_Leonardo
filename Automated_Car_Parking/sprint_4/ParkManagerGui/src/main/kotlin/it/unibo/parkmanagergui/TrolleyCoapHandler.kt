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
                ValuesForGui.path = pathRep
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, pathRep)
            }
            else if (jsonContent.has("status")){
                val statusRep = ResourceRep("status" + HtmlUtils.htmlEscape( jsonContent.getString("status"))  )
                sysUtil.colorPrint("TrolleyCoapHandler | status value=${statusRep.content}", Color.BLUE)
                ValuesForGui.status = statusRep
                println("statusRes: $statusRep" )
                println("contentStusa: ${statusRep.content}" )
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, statusRep)
            }
            if (jsonContent.has("curDest")){
                val curDestRep = ResourceRep("curDest" + HtmlUtils.htmlEscape( jsonContent.getString("curDest"))  )
                ValuesForGui.curDest = curDestRep
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, curDestRep)
            }
            if (jsonContent.has("robotPos")){
                val robotPosRep = ResourceRep("robotPos" + HtmlUtils.htmlEscape( jsonContent.getString("robotPos"))  )
                sysUtil.colorPrint("TrolleyCoapHandler | path value=${robotPosRep.content}", Color.BLUE)
                ValuesForGui.robotPos = robotPosRep
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, robotPosRep)
            }
            if (jsonContent.has("direction")){
                val directionRep = ResourceRep("direction" + HtmlUtils.htmlEscape( jsonContent.getString("direction"))  )
                ValuesForGui.directiion = directionRep
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, directionRep)
            }

        }catch(e:Exception){
            println(e.stackTraceToString())
            sysUtil.colorPrint("TrolleyCoapHandler | ERROR=${content}", Color.RED)
        }
    }

    override fun onError() {
        System.err.println("TrolleyCoapHandler  |  FAILED  ")
    }


}