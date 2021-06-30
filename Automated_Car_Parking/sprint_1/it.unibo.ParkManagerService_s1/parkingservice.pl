%====================================================================================
% parkingservice description   
%====================================================================================
context(ctxparkingservice, "localhost",  "TCP", "8052").
 qactor( outsonar, ctxparkingservice, "sonarSimulator").
  qactor( weightsensor, ctxparkingservice, "weightsensorSimulator").
  qactor( client, ctxparkingservice, "it.unibo.client.Client").
  qactor( parkingmanagerservice, ctxparkingservice, "it.unibo.parkingmanagerservice.Parkingmanagerservice").
