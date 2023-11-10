package de.cyklon.spigotutils.command.annotation.option;

import com.mojang.brigadier.arguments.StringArgumentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface StringOption {

    StringArgumentType.StringType type() default StringArgumentType.StringType.QUOTABLE_PHRASE;

    String name() default "";

    boolean required() default true;

}
