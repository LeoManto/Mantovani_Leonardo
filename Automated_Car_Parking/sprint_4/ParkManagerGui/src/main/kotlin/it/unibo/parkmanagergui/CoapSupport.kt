package it.unibo.parkmanagergui

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapObserveRelation
import org.eclipse.californium.core.CoapResponse


class CoapSupport(address: String, path: String) {
    private val client: CoapClient
    private lateinit var relation: CoapObserveRelation

    init { //"coap://localhost:5683/" + path
        val url = "$address/$path"
        client  = CoapClient(url)
        println("CoapSupport | STARTS url=$url client=$client")
        client.setTimeout(1000L)
        val rep = readResource()
        println("CoapSupport | initial rep=$rep")
        //observeResource( new MyHandler() );
    }

    fun readResource(): String {
        val respGet: CoapResponse = client.get()
        println("CoapSupport | readResource RESPONSE CODE: " + respGet.getCode())
        return respGet.getResponseText()
    }

    fun removeObserve() {
        relation.proactiveCancel()
    }

    fun observeResource(handler: CoapHandler?) {
        relation = client.observe(handler)
    }







}