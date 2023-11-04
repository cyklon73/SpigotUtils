package de.cyklon.spigotutils.event.gui;

import de.cyklon.spigotutils.ui.Gui;
import de.cyklon.spigotutils.ui.component.GuiComponent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class GuiClickEvent extends GuiEvent implements Cancellable {

    private boolean cancel = false;

    private final GuiComponent clicked;
    private final Player whoClicked;

    public GuiClickEvent(Gui gui, GuiComponent clicked, Player whoClicked) {
        super(gui);
        this.clicked = clicked;
        this.whoClicked = whoClicked;
    }

    public GuiComponent getClicked() {
        return clicked;
    }

    public Player getWhoClicked() {
        return whoClicked;
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
