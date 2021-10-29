package it.unibo.parkmanagergui

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.delay
import org.json.JSONObject
import org.springframework.web.util.HtmlUtils
import java.io.*
import java.lang.Runnable
import java.lang.Exception
import java.net.*
import java.util.concurrent.TimeUnit

class SocketManager : Runnable {

    lateinit var controller : HIController
    private var socket: Socket? = null
    private var input: BufferedReader? = null
    override fun run() {
        try {
            val serverAddr = InetAddress.getByName(SERVER_IP)
            socket = Socket(serverAddr, SERVERPORT)

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


