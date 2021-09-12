package it.unibo.webspring.demo

import java.io.*
import java.lang.Runnable
import java.net.InetAddress
import java.lang.Exception
import java.net.Socket
import java.net.UnknownHostException

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
                var lines = arrayOfNulls<String>(3)
                lines = content.split(",").toTypedArray();
                val id      = lines[0];
                val type    = lines[1];
                val sender  = lines[2];
                val dest    = lines[3]
                val msg     = lines[4]+")"
                val msgArg  = msg?.substringAfter("(",msg)?.substringBefore(")",msg)

                if(id.equals("slotnum")){

                }
            }
        } catch (e1: UnknownHostException) {
            e1.printStackTrace()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
    }

    fun sendMessage(message: String?) {
        Thread {
            try {
                if (null != socket) {
                    println("sendMessage start")
                    val out = PrintWriter(
                        BufferedWriter(
                            OutputStreamWriter(socket!!.getOutputStream())), true
                    )
                    out.println(message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    companion object {
        const val SERVER_IP = "localhost"
        const val SERVERPORT = 5683
    }
}