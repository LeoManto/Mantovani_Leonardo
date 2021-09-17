%====================================================================================
% parkingservice description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "parkingArea/feedback").
context(ctxparkingservice, "localhost",  "TCP", "5683").
context(ctxtrolley, "127.0.0.1",  "TCP", "5685").
 qactor( trolley, ctxtrolley, "external").
  qactor( outsonar, ctxparkingservice, "sonarSimulator").
  qactor( outdoortimer, ctxparkingservice, "outdoorTimer").
  qactor( weightsensor, ctxparkingservice, "weightsensorSimulator").
  qactor( thermometer, ctxparkingservice, "thermometerSimulator").
  qactor( fan, ctxparkingservice, "fanSimulator").
  qactor( parkingmanagerservice, ctxparkingservice, "it.unibo.parkingmanagerservice.Parkingmanagerservice").
  qactor( sonarhandler, ctxparkingservice, "it.unibo.sonarhandler.Sonarhandler").
