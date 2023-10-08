package de.cyklon.spigotutils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotUtils extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getOnlinePlayers().forEach(p -> new TestGui(p, this).show());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
