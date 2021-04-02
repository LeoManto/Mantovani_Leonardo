package it.unibo.cautiousExplorerActors.supports;

import it.unibo.cautiousExplorerActors.annotations.IssAnnotationUtil;
import it.unibo.cautiousExplorerActors.annotations.ProtocolInfo;

public class IssCommsSupportFactory {

    public static IssCommSupport create(Object obj){
        ProtocolInfo protocolinfo = IssAnnotationUtil.getProtocol(obj);
        String protocol = protocolinfo.getProtocol();
        String url = protocolinfo.getUrl();
        return create(protocol, url);
    }

    public static IssCommSupport create(String protocol, String url){
        System.out.println("        IssCommsFactory | create protocol=" + protocol  );
        switch(protocol){
            case "HTTP" : {
                return new IssHttpSupport(url);
            }
            case "WS" : {
                return new IssWsSupport(url);
            }
            default : return new IssHttpSupport(url);
        }
    }



}
