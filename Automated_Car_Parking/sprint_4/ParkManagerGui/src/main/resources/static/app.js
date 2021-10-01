var stompClient = null;
//alert("app.js")

//SIMULA UNA FORM che invia comandi POST
/*
function sendRequestData( params, method) {
 var hostAddr = "http://localhost:8083/sonar";
   method = method || "post"; // il metodo POST � usato di default
    console.log(" sendRequestData  params=" + params + " method=" + method);
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", hostAddr);
    var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", "move");
        hiddenField.setAttribute("value", params);
     	//console.log(" sendRequestData " + hiddenField.getAttribute("name") + " " + hiddenField.getAttribute("value"));
        form.appendChild(hiddenField);
    document.body.appendChild(form);
    console.log("body children num= "+document.body.children.length );
    form.submit();
    document.body.removeChild(form);
    console.log("body children num= "+document.body.children.length );
}
*/


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
        //msg: {"content":"led(off):12"} or {"content":"sonarvalue(D)"}
             //alert(msg)
             var jsonMsg = JSON.parse(msg.body).content;
             if( jsonMsg.includes("slotnum")) showMsg( jsonMsg.replace("slotnum", ""), "slotDisplay" );
             else if( jsonMsg.includes("token")) showMsg( jsonMsg.replace("token", ""), "tokenDisplay" );
             else if( jsonMsg.includes("fan")) showMsg( jsonMsg.replace("fan", ""), "fanDisplay" );
             else if( jsonMsg.includes("path")) showMsg( jsonMsg.replace("path", ""), "pathDisplay" );
             else showMsg( jsonMsg.replace("status", ""), "statusDisplay" );
             /*switch(jsonMsg) {
               case jsonMsg.includes("welcome"):
                     showMsg( jsonMsg, "greetingsDisplay" );
                     break;
               case jsonMsg.includes("slotnum"):
                     showMsg( jsonMsg, "slotDisplay" );
                     break;
               case jsonMsg.includes("token"):
                    showMsg( jsonMsg, "tokenDisplay" );
                    break;
               default:
                    showMsg( "Errore", "errorDisplay" );
             }*/
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

function sendUpdateRequest(){
	console.log(" sendUpdateRequest "  );
    stompClient.send("/app/update", {}, JSON.stringify({'name': 'update' }));
}

function showMsg(message, outputId) {
console.log(message );
    $("#"+outputId).html( "<pre>"+message.replace(/\n/g,"<br/>")+"</pre>" );
    //$("#applmsgintable").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
         console.log(" ------- form " + e );
         //e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendRequestData(); });


//USED BY POST-BASED GUI
//$( "#sonarvalue" ).click(function() { sendRequestData( "w") });

});



