package de.cyklon.spigotutils.event.gui;

import de.cyklon.spigotutils.ui.Gui;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.Inventory;

public class GuiUpdateEvent extends GuiEvent implements Cancellable {
    private boolean cancel = false;

    private final Inventory before;
    private Inventory after;

    public GuiUpdateEvent(Gui gui, Inventory before, Inventory after) {
        super(gui);
        this.before = before;
        this.after = after;
    }

    public Inventory getBefore() {
        return before;
    }

    public Inventory getAfter() {
        return after;
    }

    public void setAfter(Inventory after) {
        this.after = after;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
