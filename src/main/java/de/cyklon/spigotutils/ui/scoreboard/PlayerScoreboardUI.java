package de.cyklon.spigotutils.ui.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface PlayerScoreboardUI<T> extends ScoreboardUI<T> {

	static PlayerScoreboardUI<String> getLegacyPlayerScoreboard(Plugin plugin, Player... players) {
		return new PlayerScoreboard(plugin, players);
	}

	static PlayerScoreboardUI<Component> getAdventurePlayerScoreboard(Plugin plugin, Player... players) {
		return new AdvPlayerScoreboard(plugin, players);
	}

	public boolean addPlayer(Player player);

	public boolean removePlayer(Player player);

	public List<Player> getPlayers();

}
