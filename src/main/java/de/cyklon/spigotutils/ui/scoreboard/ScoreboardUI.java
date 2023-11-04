package de.cyklon.spigotutils.ui.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ScoreboardUI<T> {

	static @NotNull Collection<ScoreboardUI<?>> getScoreboards() {
		return new ArrayList<>(DefaultScoreboard.SCOREBOARDS);
	}

	static @NotNull ScoreboardUI<String> getLegacyServerScoreboard(@NotNull Plugin plugin) {
		return new ServerScoreboard(plugin);
	}

	static @NotNull ScoreboardUI<Component> getAdventureServerScoreboard(@NotNull Plugin plugin) {
		return new AdvServerScoreboard(plugin);
	}

	public void setTitle(@Nullable T title);

	public T getTitle();

	public void setLine(int score, @Nullable T text);

	public void setEmptyLine(int score);

	public T getLine(int line);

	public @NotNull List<T> getLines();

	public void removeLine(int score);

	public void clearLines();

	public void update();

	public @NotNull String getId();

	public boolean isDeleted();

	public int size();

	public void delete();

	public @NotNull Plugin getCreator();

}
