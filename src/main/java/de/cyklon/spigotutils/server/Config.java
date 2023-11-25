package de.cyklon.spigotutils.server;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

final class Config implements ServerConfiguration {

	static final ServerConfiguration CONFIG = new Config();

	private Config() {}

	public Properties getServerProperties() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("server.properties"));
		} catch (IOException ignored) {}
		return properties;
	}

	private YamlConfiguration getYML(String file) {
		return YamlConfiguration.loadConfiguration(new File(file));
	}

	public  YamlConfiguration getBukkitYML() {
		return getYML("bukkit.yml");
	}

	public YamlConfiguration getSpigotYML() {
		return getYML("spigot.yml");
	}

	public YamlConfiguration getCommandsYML() {
		return getYML("commands.yml");
	}

	public YamlConfiguration getHelpYML() {
		return getYML("help.yml");
	}

	public YamlConfiguration getPermissionsYML() {
		return getYML("permissions.yml");
	}

	public YamlConfiguration getWebifYML() {
		return getYML("wepif.yml");
	}

}
