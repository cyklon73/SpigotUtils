package de.cyklon.spigotutils.command;

import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@SuppressWarnings("unused")
public interface CommandManager {

    //brigadier support
    // /execute run ... support
    //optional parameters

    static CommandManager getCommandManager() {
        return CommandHandler.manager();
    }

    @NotNull Collection<Command> register(@NotNull Plugin plugin, @NotNull Object obj);

    <T> void putOptionMapping(Class<T> type, OptionMapping<T> mapping);

}
