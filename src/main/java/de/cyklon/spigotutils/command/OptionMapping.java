package de.cyklon.spigotutils.command;

import com.mojang.brigadier.arguments.ArgumentType;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public abstract class OptionMapping<T> {

    abstract T map(String argument);

    @NotNull List<String> tabComplete(@Nullable CommandSender sender) {
        return Collections.emptyList();
    }

    String nullMessage(String argument) {
        return null;
    }

    String exceptionMessage(String argument, Exception e) {
        return null;
    }

    ArgumentType<T> getArgumentType(Annotation annotation) {
        return null;
    }

}
