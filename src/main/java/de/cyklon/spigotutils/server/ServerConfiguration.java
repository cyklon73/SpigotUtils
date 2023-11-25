package de.cyklon.spigotutils.server;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Properties;

public interface ServerConfiguration {

    public Properties getServerProperties();

    public YamlConfiguration getBukkitYML();

    public YamlConfiguration getSpigotYML();

    public YamlConfiguration getCommandsYML();

    public YamlConfiguration getHelpYML();

    public YamlConfiguration getPermissionsYML();

    public YamlConfiguration getWebifYML();
}
