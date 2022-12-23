package it.italiandudes.ccu.common.annotations;

import java.lang.annotation.Documented;

@Documented
public @interface LogicalClass {
    String value() default "";
}
