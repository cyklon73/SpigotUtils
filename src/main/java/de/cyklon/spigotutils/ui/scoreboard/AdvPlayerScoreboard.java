package de.cyklon.spigotutils.ui.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
final class AdvPlayerScoreboard extends DefaultAdvScoreboard implements PlayerScoreboardUI<Component>, Listener {

	AdvPlayerScoreboard(Plugin plugin, Player... players) {
		super(players);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (isDeleted()) return;
		Player player = event.getPlayer();
		if (getPlayers().stream().map(Entity::getUniqueId).toList().contains(player.getUniqueId())) show(player);
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
