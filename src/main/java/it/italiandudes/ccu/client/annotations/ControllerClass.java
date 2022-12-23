package it.italiandudes.ccu.client.annotations;

import java.lang.annotation.Documented;

@Documented
public @interface ControllerClass {
    String value() default "";
}
