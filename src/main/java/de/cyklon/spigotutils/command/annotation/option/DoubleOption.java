package de.cyklon.spigotutils.command.annotation.option;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface DoubleOption {

	double minValue() default Double.MIN_VALUE;

	double maxValue() default Double.MAX_VALUE;

	String name() default "";

	boolean required() default true;

}
