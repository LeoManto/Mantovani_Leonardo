%====================================================================================
% sonaronrasp description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "parkingArea/feedback").
context(ctxsonaronrasp, "localhost",  "TCP", "8028").
 qactor( sonarsimulator, ctxsonaronrasp, "sonarSimulator").
  qactor( sonardatasource, ctxsonaronrasp, "sonarHCSR04Support2021").
  qactor( datalogger, ctxsonaronrasp, "dataLogger").
  qactor( datacleaner, ctxsonaronrasp, "dataCleaner").
  qactor( sonar, ctxsonaronrasp, "it.unibo.sonar.Sonar").
msglogging.
