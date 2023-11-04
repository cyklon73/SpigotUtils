package de.cyklon.spigotutils.event.scoreboard;

import de.cyklon.spigotutils.ui.scoreboard.ScoreboardUI;
import org.jetbrains.annotations.NotNull;

public class ScoreboardCreateEvent extends ScoreboardEvent {

    public ScoreboardCreateEvent(@NotNull ScoreboardUI<?> scoreboard) {
        super(scoreboard);
    }
}
