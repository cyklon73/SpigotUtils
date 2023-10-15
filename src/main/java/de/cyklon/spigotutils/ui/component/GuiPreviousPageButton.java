package de.cyklon.spigotutils.ui.component;

import de.cyklon.spigotutils.item.ItemBuilder;
import de.cyklon.spigotutils.item.SkullManager;
import org.bukkit.Material;

/**
 * you can simply add this component to your gui. as soon as it is pressed, the gui automatically switches to the previous page. if there is no page before, nothing happens
 */
public class GuiPreviousPageButton extends GuiButton {

    public GuiPreviousPageButton() {
        super(new ItemBuilder(SkullManager.OAK_WOOD_ARROW_LEFT).setDisplayName("Zurück").build(), (gui, p) -> {
            if (gui.getCurrentPage()>1) gui.setCurrentPage(gui.getCurrentPage()-1);
        });
    }

}
