package it.unibo.cautiousExplorerActors.annotations;

import javax.imageio.stream.FileImageInputStream;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IssAnnotationUtil {

    public static ProtocolInfo getProtocol(Object element) {
        Class<?> clazz = element.getClass();
        Annotation[] annotations = clazz.getAnnotations();
        ProtocolInfo protocolInfo = null;
        for(Annotation annot : annotations) {
            IssProtocolSpec info = (IssProtocolSpec) annot;
            protocolInfo = checkProtocolConfigFile(info.configFile());
            if(protocolInfo == null){
                protocolInfo = new ProtocolInfo(info.protocol(), info.url());
            }
        }
        return protocolInfo;
    }

    public static ProtocolInfo checkProtocolConfigFile(String configFileName) {
        try {
            System.out.println("IssAnnotationUtil | checkProtocolConfigFile configFileName=" + configFileName);
            FileInputStream fis = new FileInputStream(configFileName);
            Scanner sc = new Scanner(fis);
            String line = sc.nextLine();
            String[] items = line.split(",");

            String protocol = IssAnnotationUtil.getprotocolConfigInfo("protocol", items[0]);
            String url = IssAnnotationUtil.getprotocolConfigInfo("url", items[1]);
            
            ProtocolInfo protInfo = new ProtocolInfo(protocol, url);
            return protInfo;
            
        } catch (Exception e){
            System.out.println("IssAnnotationUtil | WARNING:" + e.getMessage());
            return null;
        }
    }

    public static String getprotocolConfigInfo(String functor, String line) {
        Pattern pattern = Pattern.compile(functor);
        Matcher matcher = pattern.matcher(line);
        String content = null;
        if(matcher.find()){
            int end = matcher.end();
            content = line.substring(end, line.indexOf(")"))
                    .replace("\"", "")
                    .replace("(", "")
                    .trim();
        }
        
        return content;
    }
    /*
-------------------------------------------------------------------------------
RELATED TO ROBOT MOVES
-------------------------------------------------------------------------------
 */
    
    public static void getMoveTimes(Object obj, HashMap<String, Integer> mvtimeMap){
        Class<?> clazz = obj.getClass();
        Annotation[] annotations = clazz.getAnnotations();
        fillMap(mvtimeMap, annotations);
    }

    public static void fillMap(HashMap<String, Integer> mvtimeMap, Annotation[] annots) {
        for(Annotation annotation : annots) {
            if(annotation instanceof RobotMoveTimeSpec){
                RobotMoveTimeSpec info = (RobotMoveTimeSpec) annotation;
                if(!checkRobotConfigFile(info.configFile(), mvtimeMap)){
                    mvtimeMap.put("w", info.wtime());
                    mvtimeMap.put("s", info.stime());
                    mvtimeMap.put("l", info.ltime());
                    mvtimeMap.put("r", info.rtime());
                    mvtimeMap.put("h", info.htime());
                }
            }
        }
    }

    public static boolean checkRobotConfigFile(String configFileName, HashMap<String, Integer> mvtimeMap) {
       try{
           FileInputStream fis = new FileInputStream(configFileName);
           Scanner sc = new Scanner(fis);
           String line = sc.nextLine();
           String[] items = line.split(",");

           mvtimeMap.put("h", getRobotConfigInfo("htime", items[0]));
           mvtimeMap.put("l", getRobotConfigInfo("ltime", items[1]));
           mvtimeMap.put("r", getRobotConfigInfo("rtime", items[2]));
           mvtimeMap.put("w", getRobotConfigInfo("wtime", items[3]));
           mvtimeMap.put("s", getRobotConfigInfo("stime", items[4]));

           return true;
       }catch(Exception e){
           System.out.println("IssAnnotationUtil | WARNING:" + e.getMessage());
           return false;
       }
    }

    protected static Integer getRobotConfigInfo(String functor, String line) {
        Pattern pattern = Pattern.compile(functor);
        Matcher matcher = pattern.matcher(line);
        String content = "0";

        if(matcher.find()){
            int end = matcher.end();
            content = line.substring(end, line.indexOf(")"))
                    .replace("\"","")
                    .replace("(","")
                    .trim();
        }

        return Integer.parseInt(content);
    }

}
