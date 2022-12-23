package it.italiandudes.ccu.client.annotations;

import java.lang.annotation.Documented;

@Documented
public @interface LogicalOperation {
    String value() default "";
}
