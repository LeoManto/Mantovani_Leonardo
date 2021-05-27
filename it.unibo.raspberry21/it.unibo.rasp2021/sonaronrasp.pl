%====================================================================================
% sonaronrasp description   
%====================================================================================
mqttBroker("localhost", "1883", "sonar/data").
context(ctxsonaronrasp, "localhost",  "TCP", "8068").
 qactor( sonarsimulator, ctxsonaronrasp, "sonarSimulator").
  qactor( sonardatasource, ctxsonaronrasp, "sonarHCSR04Support2021").
  qactor( datacleaner, ctxsonaronrasp, "dataCleaner").
  qactor( sonar, ctxsonaronrasp, "it.unibo.sonar.Sonar").
msglogging.
