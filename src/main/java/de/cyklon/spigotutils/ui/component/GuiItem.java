package de.cyklon.spigotutils.ui.component;

import org.bukkit.inventory.ItemStack;

/**
 * This component puts the specified item into the gui which can also be removed by the user
 */
public class GuiItem extends GuiComponent {

    private final ItemStack stack;

    public GuiItem(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    protected ItemStack createItem() {
        return stack;
    }

    @Override
    public void onClick(ClickEvent event) {
        event.setCancelled(false);
    }
}
