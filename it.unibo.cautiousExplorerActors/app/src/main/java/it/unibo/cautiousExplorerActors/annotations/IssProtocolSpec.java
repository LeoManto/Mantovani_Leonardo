package it.unibo.cautiousExplorerActors.annotations;

import java.lang.annotation.*;

@Target(value = { ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IssProtocolSpec {

    public enum issProtocol {UDP, TCP, HTTP, MQTT, COAP, WS};
    issProtocol protocol() default issProtocol.TCP;
    String url() default "unknown";
    String configFile() default "IssProtoclConfig.txt";

}
