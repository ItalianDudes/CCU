package it.italiandudes.ccu.client.annotations;

import java.lang.annotation.Documented;

@Documented
public @interface LogicalClass {
    String value() default "";
}
