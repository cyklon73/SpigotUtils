package de.cyklon.spigotutils.version;

import org.bukkit.Server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Version {

	private static final Pattern VERSION_PATTERN = Pattern.compile(".*\\(.*MC.\\s*([a-zA-z0-9\\-.]+).*");

	private Version() {}

	public static MinecraftVersion getVersion(Server server) {
		String s = server.getVersion();
		Matcher version = VERSION_PATTERN.matcher(s);

		if (version.matches() && version.group(1) != null) {
			s = version.group(1);
		} else throw new IllegalStateException("Cannot parse version '" + s + "'");
		s = "v" + s.replace(".", "_");
		return MinecraftVersion.valueOf(s);
	}

	private static boolean isPresent(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException ignored) {}
		return false;
	}

	public static boolean isPaper() {
		return isPresent("io.papermc.paper.adventure.PaperAdventure");
	}

	public static boolean isFolia() {
		return isPresent("io.papermc.paper.threadedregions.RegionizedServer");
	}

}
