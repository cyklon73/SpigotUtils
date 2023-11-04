package de.cyklon.spigotutils.event.gui;

import de.cyklon.spigotutils.ui.Gui;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuiEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Gui gui;

    public GuiEvent(Gui gui) {
        this.gui = gui;
    }

    public Gui getGui() {
        return gui;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }
}
