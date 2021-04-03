package it.unibo.cautiousExplorerActors.supports;

import it.unibo.cautiousExplorerActors.annotations.ArilRobotSpec;
import it.unibo.cautiousExplorerActors.annotations.IssAnnotationUtil;
import it.unibo.cautiousExplorerActors.annotations.ProtocolInfo;
import it.unibo.cautiousExplorerActors.annotations.VirtualRobotSpec;
import it.unibo.cautiousExplorerActors.interaction.IssOperations;

import java.lang.annotation.Annotation;

public class RobotApplicationStarter {

    private static final String protocolConfigFName = "IssProtocolConfig.txt";
    private static final String robotConfigFName   = "IssRobotConfig.txt";

    public static Object createInstance(Class<?> clazz){
        try{

            System.out.println("RobotApplicationStarter | createInstance " + clazz.getName());
            Annotation[] annotations = clazz.getAnnotations();

            for(Annotation annot : annotations){
                if(annot instanceof VirtualRobotSpec){
                    ProtocolInfo p = IssAnnotationUtil.checkProtocolConfigFile(protocolConfigFName);
                    IssOperations rs = IssCommsSupportFactory.create(p.getProtocol(), p.getUrl());
                    System.out.println("RobotApplicationStarter | commSupport=" + rs);
                    Object obj = clazz.getDeclaredConstructor(IssOperations.class).newInstance(rs);
                    return obj;
                }
                if(annot instanceof ArilRobotSpec){
                    ProtocolInfo p = IssAnnotationUtil.checkProtocolConfigFile(protocolConfigFName);
                    IssCommSupport commSupport = IssCommsSupportFactory.create(p.getProtocol(), p.getUrl());
                    System.out.println("RobotApplicationStarter | commSupport=" + commSupport);
                    IssCommSupport rs = new IssArilRobotSupport(robotConfigFName, commSupport);
                    Object obj = clazz.getDeclaredConstructor(IssOperations.class).newInstance(rs);
                    return obj;
                }
            }

            return null;

        }catch(Exception e){
            System.out.println("RobotApplicationStarter | createInstance ERROR: " + e.getMessage()  );
            return null;
        }

    }

}
