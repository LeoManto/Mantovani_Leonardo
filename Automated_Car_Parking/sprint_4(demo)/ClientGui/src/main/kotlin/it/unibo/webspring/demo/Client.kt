package it.unibo.webspring.demo


import clients.ClientWithWebClient
import java.io.*
import java.lang.Runnable
import java.lang.Exception
import java.net.*

class Client : Runnable {


    private var socket: Socket? = null
    private var input: BufferedReader? = null
    override fun run() {
        try {
            val serverAddr = InetAddress.getByName(SERVER_IP)
            socket = Socket(serverAddr, SERVERPORT)

            while (!Thread.currentThread().isInterrupted) {

                input = BufferedReader(InputStreamReader(socket!!.getInputStream()))
                val message = input!!.readLine()

                val content = message.substringAfter("(",message).substringBefore(")",message)
                var lines = content.split(",").toTypedArray();
                val id          = lines[0]
                val type        = lines[1]
                val sender      = lines[2];
                val dest        = lines[3]
                val msg         = lines[4]+")"
                val msgArg      = msg?.substringAfter("(",msg)?.substringBefore(")",msg)
                println("%%%%% Reveived: $message")

                if(id.equals("slotsnum")){
                    println("slot: $msgArg")
                    //AClientApacheHttp.doPostWithParams("http://localhost:8081/slot","slot",msgArg)
                    ClientResource.setCurrentSlot(msgArg)
                }
                else if(id.equals("receipt")){
                    println("token: $msgArg")
                    ClientResource.setCurrentToken(msgArg)
                }
            }
        } catch (e1: UnknownHostException) {
            e1.printStackTrace()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
    }

    fun forward(message: String?) {
        Thread {
            try {
                if (null != socket) {
                    val out = PrintWriter(
                        BufferedWriter(
                            OutputStreamWriter(socket!!.getOutputStream())), true
                    )
                    out.println(message)
                    println(message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }



    companion object {
        const val SERVER_IP = "localhost" //ip del parkingmanagerservice
        const val SERVERPORT = 5683
    }



}


