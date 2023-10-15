package de.cyklon.spigotutils.ui.component;

import org.bukkit.inventory.ItemStack;

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
