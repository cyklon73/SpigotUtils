package de.cyklon.spigotutils.ui.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public interface PlayerScoreboardUI<T> extends ScoreboardUI<T> {

	static PlayerScoreboardUI<String> getLegacyPlayerScoreboard(Player... players) {
		return new PlayerScoreboard(players);
	}

	static PlayerScoreboardUI<Component> getAdventurePlayerScoreboard(Player... players) {
		return new AdvPlayerScoreboard(players);
	}

	public void addPlayer(Player player);

	public void removePlayer(Player player);

	public List<Player> getPlayers();

}
