package it.italiandudes.ccu.client.annotations;

import java.lang.annotation.Documented;

@Documented
public @interface GraphicalOperation {
    String value() default "";
}
