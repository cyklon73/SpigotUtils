package de.cyklon.spigotutils.ui.component;

import de.cyklon.spigotutils.event.gui.GuiClickEvent;
import de.cyklon.spigotutils.ui.Gui;
import de.cyklon.spigotutils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class GuiComponent {

    private UUID id = null;

    private static class ComponentEvent {

        private final Gui gui;
        private final GuiComponent component;

        public ComponentEvent(Gui gui, GuiComponent component) {
            this.gui = gui;
            this.component = component;
        }

        public Gui getGui() {
            return gui;
        }

        public GuiComponent getComponent() {
            return component;
        }
    }

    /**
     * by default the event is cancelled but you can set it to false with setCancelled()
     * <p>
     * if the event is canceled the component item cannot be picked up from the gui
     */
    public static class ClickEvent extends ComponentEvent implements Cancellable {

        private boolean cancelled = true;
        private final Player player;

        public ClickEvent(Gui gui, GuiComponent component, Player player) {
            super(gui, component);
            this.player = player;
        }

        public Player getWhoClicked() {
            return player;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean b) {
            this.cancelled = b;
        }
    }

    public final ItemStack getItem() {
        if (id==null) id = UUID.randomUUID();
        return new ItemBuilder(createItem()).setLocalizedName(id.toString()).build();
    }

    /**
     * In this method you can build your component item and return it
     */
    protected abstract ItemStack createItem();

    /**
     * this method is called when the component is clicked on
     * @param event the click event
     */
    public abstract void onClick(ClickEvent event);

}
