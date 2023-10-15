package de.cyklon.spigotutils.ui.component;

import de.cyklon.spigotutils.item.ItemBuilder;
import org.bukkit.Material;

/**
 * you can simply add this component to your gui. as soon as it is pressed, the gui automatically switches to the next page. if there is no other page, nothing happens
 */
public class GuiNextPageButton extends GuiButton {

    public GuiNextPageButton() {
        super(new ItemBuilder(Material.LAVA_BUCKET).setDisplayName("Weiter").build(), (gui, p) -> {
            if (gui.getCurrentPage()<gui.getPageAmount()) gui.setCurrentPage(gui.getCurrentPage()+1);
        });
    }

}
