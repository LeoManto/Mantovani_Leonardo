%====================================================================================
% parkingservice description   
%====================================================================================
context(ctxtrolley, "127.0.0.1",  "TCP", "5685").
context(ctxparkingservice, "localhost",  "TCP", "5683").
 qactor( parkingmanagerservice, ctxparkingservice, "external").
  qactor( trolley, ctxtrolley, "it.unibo.trolley.Trolley").
