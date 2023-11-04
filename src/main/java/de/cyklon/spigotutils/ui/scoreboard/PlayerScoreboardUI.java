package de.cyklon.spigotutils.ui.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PlayerScoreboardUI<T> extends ScoreboardUI<T> {

	static @NotNull PlayerScoreboardUI<String> getLegacyPlayerScoreboard(@NotNull Plugin plugin, @NotNull Player... players) {
		return new PlayerScoreboard(plugin, players);
	}

	static @NotNull PlayerScoreboardUI<Component> getAdventurePlayerScoreboard(@NotNull Plugin plugin, @NotNull Player... players) {
		return new AdvPlayerScoreboard(plugin, players);
	}

	public boolean addPlayer(@NotNull Player player);

	public boolean removePlayer(Player player);

	public @NotNull List<Player> getPlayers();

}
