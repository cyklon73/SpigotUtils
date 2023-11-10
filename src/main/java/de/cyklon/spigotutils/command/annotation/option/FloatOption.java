package de.cyklon.spigotutils.command.annotation.option;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface FloatOption {

	float minValue() default Float.MIN_VALUE;

	float maxValue() default Float.MAX_VALUE;

	String name() default "";

	boolean required() default true;

}
