package it.unibo.cautiousExplorerActors.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE   })
@Retention(RetentionPolicy.RUNTIME)
//@Inherited
public @interface RobotMoveTimeSpec {

    int ltime() default 200;
    int rtime() default 200;
    int wtime() default 150;
    int stime() default 150;
    int htime() default 100;

    String configFile() default "IssRobotConfig.txt";
}
