package it.italiandudes.ccu.common.annotations;

import java.lang.annotation.Documented;

@Documented
public @interface ControllerClass {
    String value() default "";
}
