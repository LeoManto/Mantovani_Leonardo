package it.unibo.parkmanagergui


object SenderToPark {
        private var socketToPark : SocketManager?= null
        private var thread : Thread ?= null

    private const val stop =    "msg(stopTrolley,dispatch,manager,trolleystopper,stop(manager),404)"
    private const val resume =  "msg(resumeTrolley,dispatch,manager,trolleystopper,resume(manager),201)"

    fun start(controller: HIController){
            socketToPark = SocketManager()
            socketToPark!!.controller = controller
            thread = Thread(socketToPark)
            thread!!.start()
        }


    fun send(type: String) {
            when (type) {
                "stop" ->    socketToPark!!.forward(stop)
                "resume" ->  socketToPark!!.forward(resume)
            }
        }


}
