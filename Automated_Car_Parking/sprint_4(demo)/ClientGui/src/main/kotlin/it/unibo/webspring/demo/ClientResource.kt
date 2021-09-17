package it.unibo.webspring.demo

object ClientResource {
    private var clientThread : Client ?= null
    private var thread : Thread ?= null
    private const val reqenter  = "msg(reqenter,request,client,parkingmanagerservice,reqenter(client),1)"
    private const val carenter  = "msg(carenter,request,client,parkingmanagerservice,movetoin(ok),2)"
    private const val pickupmsg = "msg(pickup,dispatch,client,parkingmanagerservice,pickup(TOKENID),3)"

    private var curSlot     = ""
    private var curToken    = ""

    fun start(){
        clientThread = Client()
        thread = Thread(clientThread)
        thread!!.start()
    }

    fun send(type: String) {
        when (type) {
            "reqenter" -> clientThread!!.forward(reqenter)
            "carenter" -> clientThread!!.forward(carenter)
        }
    }

    fun send(type: String, token: String) {
        when (type) {
            "reqenter" -> clientThread!!.forward(reqenter)
            "carenter" -> clientThread!!.forward(carenter)
            "pickup"   -> clientThread!!.forward(pickupmsg.replace("TOKENID", token))
        }

    }

    fun setCurrentSlot(slot: String) {
        curSlot = slot
    }

    fun setCurrentToken(token: String) {
        curToken = token
    }

    fun getCurrentSlot() : String{
        val slot = curSlot
        curSlot = ""
        return slot
    }

    fun getCurrentToken() : String{
        val token = curToken
        curToken = ""
        return token
    }


}