%====================================================================================
% sonaronrasp description   
%====================================================================================
context(ctxsonaronrasp, "localhost",  "TCP", "8028").
context(ctxparkingservice, "127.0.0.1",  "TCP", "5683").
 qactor( parkingmanagerservice, ctxparkingservice, "external").
  qactor( sonarsimulator, ctxsonaronrasp, "sonarSimulator").
  qactor( sonardatasource, ctxsonaronrasp, "sonarHCSR04Support2021").
  qactor( datalogger, ctxsonaronrasp, "dataLogger").
  qactor( datacleaner, ctxsonaronrasp, "dataCleaner").
  qactor( distancefilter, ctxsonaronrasp, "distanceFilter").
  qactor( sonar, ctxsonaronrasp, "it.unibo.sonar.Sonar").
msglogging.
