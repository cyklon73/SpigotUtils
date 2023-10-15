package de.cyklon.spigotutils.ui.component;

import de.cyklon.spigotutils.ui.Gui;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class GuiButton extends GuiComponent {

    private final ItemStack icon;
    private final BiConsumer<Gui, HumanEntity> listener;

    public GuiButton(ItemStack icon, BiConsumer<Gui, HumanEntity> listener) {
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
