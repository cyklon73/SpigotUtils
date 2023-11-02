package de.cyklon.spigotutils.ui.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ScoreboardUI<T> {

	static Collection<ScoreboardUI<?>> getScoreboards() {
		return new ArrayList<>(DefaultScoreboard.SCOREBOARDS);
	}

	static ScoreboardUI<String> getLegacyServerScoreboard(Plugin plugin) {
		return new ServerScoreboard(plugin);
	}

	static ScoreboardUI<Component> getAdventureServerScoreboard(Plugin plugin) {
		return new AdvServerScoreboard(plugin);
	}

	public void setTitle(T title);

	public T getTitle();

	public void setLine(int score, T text);

	public void setEmptyLine(int score);

	public T getLine(int line);

	public List<T> getLines();

	public void removeLine(int score);

	public void clearLines();

	public void update();

	public String getId();

	public boolean isDeleted();

	public int size();

	public void delete();

}
