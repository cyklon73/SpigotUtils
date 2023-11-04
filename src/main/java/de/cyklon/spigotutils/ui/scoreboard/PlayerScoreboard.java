package de.cyklon.spigotutils.ui.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

final class PlayerScoreboard extends DefaultLegacyScoreboard implements PlayerScoreboardUI<String>, Listener {

	PlayerScoreboard(Plugin plugin, Player... players) {
		super(players);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (isDeleted()) return;
		Player player = event.getPlayer();
		if (contains(player)) show(player);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (isDeleted()) return;
		Player player = event.getPlayer();
		if (contains(player)) hide(player);
	}

	@Override
	public boolean addPlayer(Player player) {
		boolean f = super.addPlayer(player);
		if (f) show(player);
		return f;
	}

	@Override
	public boolean removePlayer(Player player) {
		boolean f = super.removePlayer(player);
		if (f) hide(player);
		return f;
	}

	@Override
	public List<Player> getPlayers() {
		return super.getPlayers();
	}
}
