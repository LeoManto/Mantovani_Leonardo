%====================================================================================
% resourcealone description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "resource/input").
context(ctxresource, "localhost",  "TCP", "8048").
 qactor( resource, ctxresource, "it.unibo.resource.Resource").
tracing.
msglogging.
