package de.cyklon.spigotutils.ui.component;

import de.cyklon.spigotutils.ui.Gui;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class GuiButton extends GuiComponent {

    private final ItemStack icon;
    private final BiConsumer<Gui, Player> listener;

    /**
     * The GuiButton is passed an ItemStack as an icon and a BiConsumer which is used as an onClick event.
     * @param icon the icon is an ItemStack and is displayed in the GUI
     * @param listener the listener is a BiConsumer. the Gui is the gui in which the button is located and the HumanEntity is the player who clicked on the button.
     */
    public GuiButton(ItemStack icon, BiConsumer<Gui, Player> listener) {
        this.icon = icon;
        this.listener = listener;
    }

    @Override
    protected ItemStack createItem() {
        return icon;
    }

    @Override
    public void onClick(ClickEvent event) {
        listener.accept(event.getGui(), event.getWhoClicked());
    }
}
