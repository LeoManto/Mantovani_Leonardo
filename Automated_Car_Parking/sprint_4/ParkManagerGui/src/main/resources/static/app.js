var stompClient = null;
var curTemp = "-1 °C"
var curStatus = "-1"

function setConnected(connected) {
console.log(" %%% app setConnected:" + connected );
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/it-unibo-iss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        //SUBSCRIBE to STOMP topic updated by the server
        stompClient.subscribe('/topic/infodisplay', function (msg) {

             var jsonMsg = JSON.parse(msg.body).content;
             if(jsonMsg.includes("weight")) showMsg( jsonMsg.replace("weight", ""), "weightDisplay" );
             else if(jsonMsg.includes("indoorStatus")) showMsg( jsonMsg.replace("indoorStatus", ""), "indoorDisplay" );
             else if(jsonMsg.includes("outdoorStatus")) showMsg( jsonMsg.replace("outdoorStatus", ""), "outdoorDisplay" );
             else if(jsonMsg.includes("temp")) {
                    showMsg( jsonMsg.replace("temp", ""), "tempDisplay" )
                    curTemp = jsonMsg.replace("temp", "").replace(" °C","")
                    }
             else if( jsonMsg.includes("fan"))
                 showMsg( jsonMsg.replace("fan", ""), "fanDisplay" );
             else if( jsonMsg.includes("status")) {
                    showMsg( jsonMsg.replace("status", ""), "statusDisplay" )
                    curStatus = jsonMsg.replace("status", "")
                }
              else if(jsonMsg.includes("slotLiberi")) showMsg( jsonMsg.replace("slotLiberi", ""), "slotDisplay" );
             else if(jsonMsg.includes("curDest")) showMsg( jsonMsg.replace("curDest", ""), "destDisplay" );
             else if(jsonMsg.includes("robotPos")) showMsg( jsonMsg.replace("robotPos", ""), "posDisplay" );
             else if(jsonMsg.includes("direction")) showMsg( jsonMsg.replace("direction", ""), "dirDisplay" );
             else if(jsonMsg.includes("alarm")) showMsg( jsonMsg.replace("alarm", ""), "alarmDisplay" );

             checkBtn()
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function showMsg(message, outputId) {
console.log(message );
    $("#"+outputId).html( "<pre>"+message.replace(/\n/g,"<br/>")+"</pre>" );
}

function checkBtn(value){
    if(parseInt(curTemp) >= 40 && curStatus !== "STOPPED"){
        document.getElementById("stopbtn").disabled = false  //attivo
        document.getElementById("resumebtn").disabled = true //non attivo
        }
    else if (parseInt(curTemp) < 40 && curStatus == "STOPPED" ){
        document.getElementById("resumebtn").disabled = false //attivo
        document.getElementById("stopbtn").disabled = true   //non attivo
        }
    else {
        document.getElementById("resumebtn").disabled = true // nonattivo
        document.getElementById("stopbtn").disabled = true   //non attivo
    }
}



