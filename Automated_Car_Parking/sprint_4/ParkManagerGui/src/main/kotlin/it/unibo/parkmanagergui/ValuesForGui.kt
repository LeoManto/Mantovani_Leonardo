package it.unibo.parkmanagergui

object ValuesForGui {

    var controllerForCompanion : HIController ?= null
    var path : ResourceRep = ResourceRep("0")
    var status : ResourceRep  = ResourceRep("0")
    var curDest : ResourceRep  = ResourceRep("0")
    var robotPos : ResourceRep  = ResourceRep("0")
    var directiion : ResourceRep = ResourceRep("0")


    var indoor : ResourceRep  = ResourceRep("0")
    var outdoor : ResourceRep  = ResourceRep("0")
    var weight : ResourceRep  = ResourceRep("0")
    var fan : ResourceRep  = ResourceRep("0")
    var temp : ResourceRep  = ResourceRep("0")


    fun updateLastValues(){

        controllerForCompanion?.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, path)
        controllerForCompanion?.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, status)
        controllerForCompanion?.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, curDest)
        controllerForCompanion?.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, robotPos)
        controllerForCompanion?.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, directiion)

        controllerForCompanion?.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, indoor)
        controllerForCompanion?.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, outdoor)
        controllerForCompanion?.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, weight)
        controllerForCompanion?.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, fan)
        controllerForCompanion?.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, temp)

    }

    fun setController(c: HIController) {
        controllerForCompanion = c
    }
}