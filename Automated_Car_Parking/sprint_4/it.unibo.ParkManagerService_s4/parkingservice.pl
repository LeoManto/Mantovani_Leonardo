%====================================================================================
% parkingservice description   
%====================================================================================
context(ctxparkingservice, "localhost",  "TCP", "5683").
 qactor( outsonar, ctxparkingservice, "sonarSimulator").
  qactor( outdoortimer, ctxparkingservice, "outdoorTimer").
  qactor( weightsensor, ctxparkingservice, "weightsensorSimulator").
  qactor( guiupdater, ctxparkingservice, "guiUpdater").
  qactor( thermometer, ctxparkingservice, "thermometerSimulator").
  qactor( fan, ctxparkingservice, "fanSimulator").
  qactor( parkingmanagerservice, ctxparkingservice, "it.unibo.parkingmanagerservice.Parkingmanagerservice").
  qactor( sonarhandler, ctxparkingservice, "it.unibo.sonarhandler.Sonarhandler").
  qactor( trolleystopper, ctxparkingservice, "it.unibo.trolleystopper.Trolleystopper").
  qactor( trolley, ctxparkingservice, "it.unibo.trolley.Trolley").
