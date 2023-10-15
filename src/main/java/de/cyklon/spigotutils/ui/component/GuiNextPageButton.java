package de.cyklon.spigotutils.ui.component;

import de.cyklon.spigotutils.item.ItemBuilder;
import org.bukkit.Material;

public class GuiNextPageButton extends GuiButton {

    public GuiNextPageButton() {
        super(new ItemBuilder(Material.LAVA_BUCKET).setDisplayName("Weiter").build(), (gui, p) -> {
            if (gui.getCurrentPage()<gui.getPageAmount()) gui.setCurrentPage(gui.getCurrentPage()+1);
        });
    }

}
