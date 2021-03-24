package it.unibo.resumableBoundaryWalker.annotations;

import java.lang.annotation.*;

@Target(value = { ElementType.METHOD   })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface InjectSupportSpec {
}
