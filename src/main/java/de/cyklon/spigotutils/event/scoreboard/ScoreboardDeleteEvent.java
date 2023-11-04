package de.cyklon.spigotutils.event.scoreboard;

import de.cyklon.spigotutils.ui.scoreboard.ScoreboardUI;
import org.jetbrains.annotations.NotNull;

public class ScoreboardDeleteEvent extends ScoreboardEvent {
    public ScoreboardDeleteEvent(@NotNull ScoreboardUI<?> scoreboard) {
        super(scoreboard);
    }
}
