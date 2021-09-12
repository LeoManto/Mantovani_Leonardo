package it.unibo.webspring.demo

import it.unibo.webspring.demo.ClientResource

object ClientResource {
    private var clientThread : Client ?= null
    private var thread : Thread ?= null
    private const val reqenter = "msg(reqenter,request,client,parkingmanagerservice,reqenter(client),1)"
    private const val carenter = "msg(carenter,request,client,parkingmanagerservice,movetoin(ok),2)"
    private const val pickupmsg = "msg(reqpickup,dispatch,client,parkingmanagerservice,reqpickup(TOKENID),3)"

    fun start(){
        clientThread = Client()
        thread = Thread(clientThread)
        thread!!.start()
    }

    fun send(type: String) {
        when (type) {
            "reqenter" -> clientThread!!.sendMessage(reqenter)
            "carenter" -> clientThread!!.sendMessage(carenter)
        }
    }

    fun send(type: String, token: String) {
        println("send start")
        when (type) {
            "reqenter" -> clientThread!!.sendMessage(reqenter)
            "carenter" -> clientThread!!.sendMessage(carenter)
            "pickup"   -> clientThread!!.sendMessage(pickupmsg.replace("TOKENID", token))
        }
    }

}