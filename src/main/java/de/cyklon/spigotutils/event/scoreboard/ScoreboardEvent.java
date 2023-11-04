package de.cyklon.spigotutils.event.scoreboard;

import de.cyklon.spigotutils.ui.scoreboard.ScoreboardUI;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class ScoreboardEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final ScoreboardUI<?> scoreboard;

    public ScoreboardEvent(@NotNull ScoreboardUI<?> scoreboard) {
        this.scoreboard = scoreboard;
    }

    public @NotNull ScoreboardUI<?> getScoreboard() {
        return scoreboard;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }
}
