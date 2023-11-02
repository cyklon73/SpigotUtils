package de.cyklon.spigotutils.ui.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;
final class AdvPlayerScoreboard extends DefaultAdvScoreboard implements PlayerScoreboardUI<Component> {

	AdvPlayerScoreboard(Player... players) {
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
