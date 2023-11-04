package de.cyklon.spigotutils.event.gui;

import de.cyklon.spigotutils.ui.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class GuiOpenEvent extends GuiEvent implements Cancellable {

    private boolean cancel = false;

    private final Player player;

    public GuiOpenEvent(Gui gui, Player player) {
        super(gui);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
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
