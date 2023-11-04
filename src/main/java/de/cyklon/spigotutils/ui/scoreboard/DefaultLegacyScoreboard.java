package de.cyklon.spigotutils.ui.scoreboard;

import de.cyklon.spigotutils.tuple.Pair;
import de.cyklon.spigotutils.version.MinecraftVersion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.util.Map;

abstract class DefaultLegacyScoreboard extends DefaultScoreboard<String> {

	private static final MethodHandle MESSAGE_FROM_STRING;
	private static final Object EMPTY_MESSAGE;

	static {
		try {
			MethodHandles.Lookup lookup = MethodHandles.lookup();
			Class<?> craftChatMessageClass = obcClass("util.CraftChatMessage");
			MESSAGE_FROM_STRING = lookup.unreflect(craftChatMessageClass.getMethod("fromString", String.class));
			EMPTY_MESSAGE = Array.get(MESSAGE_FROM_STRING.invoke(""), 0);
		} catch (Throwable t) {
			throw new ExceptionInInitializerError(t);
		}
	}

	DefaultLegacyScoreboard(Plugin plugin, Player... players) {
		super(plugin, players);
	}

	@Override
	public void setTitle(@Nullable String title) {
		if (title!=null && !MinecraftVersion.v1_13.isHigherOrEqual(VERSION) && title.length() > 32) throw new IllegalArgumentException("Title is longer than 32 chars");
		super.setTitle(title);
	}

	@Override
	protected synchronized void updateLines(Map<Integer, Pair<String, Integer>> lines) {
		if (MinecraftVersion.v1_13.isHigherOrEqual(VERSION)) {
			for (Map.Entry<Integer, Pair<String, Integer>> value : lines.entrySet()) {
				if (value.getValue().first()!=null && value.getValue().first().length() > 30) throw new IllegalArgumentException("Line with the score " + value.getKey() + " is longer than 30 chars");
			}
		}
		super.updateLines(lines);
	}

	@Override
	protected void sendLineChange(int score, int index) throws Throwable {
		int maxLength = !MinecraftVersion.v1_13.isHigherOrEqual(VERSION) ? 16 : 1024;
		String line = getLine(score);
		String prefix;
		String suffix = "";

		if (line==null || line.isEmpty()) prefix = COLOR_CODES[index] + ChatColor.RESET;
		else if (line.length() <= maxLength) prefix = line;
		else {
			int i = line.charAt(maxLength - 1) == ChatColor.COLOR_CHAR
					? (maxLength - 1) : maxLength;
			prefix = line.substring(0, i);
			String suffixTmp = line.substring(i);
			ChatColor chatColor = null;

			if (suffixTmp.length() >= 2 && suffixTmp.charAt(0) == ChatColor.COLOR_CHAR) {
				chatColor = ChatColor.getByChar(suffixTmp.charAt(1));
			}

			String color = ChatColor.getLastColors(prefix);
			boolean addColor = chatColor == null || chatColor.isFormat();

			suffix = (addColor ? (color.isEmpty() ? ChatColor.RESET.toString() : color) : "") + suffixTmp;
		}

		if (prefix.length() > maxLength || suffix.length() > maxLength) {
			// Something went wrong, just cut to prevent client crash/kick
			prefix = prefix.substring(0, maxLength);
			suffix = suffix.substring(0, maxLength);
		}

		sendPacket(teamPacket(score, TeamMode.UPDATE, index, prefix, suffix));
	}

	@Override
	protected Object toMinecraftComponent(String line) throws Throwable {
		if (line==null || line.isEmpty()) return EMPTY_MESSAGE;
		return Array.get(MESSAGE_FROM_STRING.invoke(line), 0);
	}

	@Override
	protected String serializeLine(String line) {
		return line;
	}

	@Override
	protected String emptyLine() {
		return "";
	}
}
