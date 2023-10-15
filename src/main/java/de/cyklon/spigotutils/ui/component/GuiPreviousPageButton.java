package de.cyklon.spigotutils.ui.component;

import de.cyklon.spigotutils.item.ItemBuilder;
import org.bukkit.Material;

public class GuiPreviousPageButton extends GuiButton {

    public GuiPreviousPageButton() {
        super(new ItemBuilder(Material.LAVA_BUCKET).setDisplayName("Zurück").build(), (gui, p) -> {
            if (gui.getCurrentPage()>1) gui.setCurrentPage(gui.getCurrentPage()-1);
        });
    }

}
