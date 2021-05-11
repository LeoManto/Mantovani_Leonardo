%====================================================================================
% demo description   
%====================================================================================
context(ctxdemo, "localhost",  "TCP", "8085").
 qactor( qa0, ctxdemo, "it.unibo.qa0.Qa0").
  qactor( caller, ctxdemo, "it.unibo.caller.Caller").
