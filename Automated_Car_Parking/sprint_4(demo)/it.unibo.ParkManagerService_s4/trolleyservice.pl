%====================================================================================
% trolleyservice description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "parkingArea/feedback").
context(ctxtrolley, "127.0.0.1",  "TCP", "5685").
 qactor( trolley, ctxtrolley, "it.unibo.trolley.Trolley").
