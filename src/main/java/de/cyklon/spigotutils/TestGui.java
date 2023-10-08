package de.cyklon.spigotutils;

import de.cyklon.spigotutils.gui.Gui;
import de.cyklon.spigotutils.gui.component.GuiButton;
import de.cyklon.spigotutils.gui.component.GuiNextPageButton;
import de.cyklon.spigotutils.gui.component.GuiPreviousPageButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class TestGui extends Gui {
    public TestGui(Player owner, Plugin plugin) {
        super(owner, plugin, "test.gui", 10, 3, "Shop [%s/%s]"::formatted);
        setComponent(1, 2, new GuiButton(new ItemStack(Material.ANVIL), (gui, p) -> p.sendMessage("Button clicked!")));
        setComponent(5, 2, new GuiButton(new ItemStack(Material.ANVIL), (gui, p) -> p.sendMessage("Button clicked! on page 5")));
        for (int i = 1; i <= getPageAmount(); i++) {
            if (i==1) setComponent(i, 26, new GuiNextPageButton());
            else if (i==getPageAmount()) setComponent(i, 18, new GuiPreviousPageButton());
            else {
                setComponent(i, 18, new GuiPreviousPageButton());
                setComponent(i, 26, new GuiNextPageButton());
            }
        }
    }
}
