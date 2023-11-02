package de.cyklon.spigotutils.ui.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;

final class PlayerScoreboard extends DefaultLegacyScoreboard implements PlayerScoreboardUI<String> {

	PlayerScoreboard(Player... players) {
		super(players);
	}

	@Override
	public void addPlayer(Player player) {
		super.addPlayer(player);
	}

	@Override
	public void removePlayer(Player player) {
		super.removePlayer(player);
	}

	@Override
	public List<Player> getPlayers() {
		return super.getPlayers();
	}
}
