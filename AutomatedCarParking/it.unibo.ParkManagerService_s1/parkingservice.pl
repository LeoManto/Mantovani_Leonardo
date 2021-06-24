%====================================================================================
% parkingservice description   
%====================================================================================
context(ctxparkingservice, "localhost",  "TCP", "8052").
 qactor( outdoortimer, ctxparkingservice, "timerOutdoor").
  qactor( pickupsimulator, ctxparkingservice, "pickupSimulator").
  qactor( outsonar, ctxparkingservice, "sonarSimulator").
  qactor( thermometer, ctxparkingservice, "thermometerSimulator").
  qactor( outdoor, ctxparkingservice, "it.unibo.outdoor.Outdoor").
  qactor( clientsimulator, ctxparkingservice, "it.unibo.clientsimulator.Clientsimulator").
  qactor( parkingmanager, ctxparkingservice, "it.unibo.parkingmanager.Parkingmanager").
