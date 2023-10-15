package de.cyklon.spigotutils.ui;

import de.cyklon.spigotutils.ui.component.GuiComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public abstract class Gui implements Listener {

    private final String id;
    private final int rows;
    private final BiFunction<Integer, Integer, String> title;
    private int currentPage = 1;
    private final List<GuiComponent[]> pages;
    private Inventory inv;
    private final Player owner;

    /**
     * call this in your super class
     * @param owner the player to whom the GUI should be shown
     * @param plugin the plugin with which the GUI is created
     * @param id an id to identify the gui
     * @param pages the number of pages the gui should have
     * @param rows the number of rows a page should have
     * @param title a function to generate a title for a page. the first integer is the current page and the second is the number of all pages.
     */
    public Gui(Player owner, Plugin plugin, String id, int pages, int rows, BiFunction<Integer, Integer, String> title) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.owner = owner;
        this.id = id;
        this.rows = rows;
        this.title = title;
        this.inv = Bukkit.createInventory(owner, rows*9, title.apply(currentPage, pages));

        this.pages = new ArrayList<>();
        for (int i = 0; i < pages; i++) {
            this.pages.add(new GuiComponent[rows*9]);
        }
    }

    /**
     * set the current page index
     * <p>
     * It is recommended not to use this method but to use the components "GuiNextPageButton" and "GuiPreviousPageButton".
     * @param page the page index
     */
    public void setCurrentPage(int page) {
        this.currentPage = page;
        update();
    }

    /**
     * @return the current page index
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @return die anzahl aller seiten zurück
     */
    public int getPageAmount() {
        return pages.size();
    }

    /**
     * sets a component to a specific position
     * @param page the page index of the page on which the component should be placed
     * @param index the slot index on which the component is to be set. the index starts at the top left, then goes to the right, then goes down one row, from left to right again, and so on.
     * @param component the component to be set
     */
    protected void setComponent(int page, int index, GuiComponent component) {
        GuiComponent[] c = pages.get(page-1);
        c[index] = component;
        pages.set(page-1, c);
        update();
    }

    private void update() {
        inv = Bukkit.createInventory(owner, rows*9, title.apply(currentPage, pages.size()));
        GuiComponent[] components = pages.get(currentPage-1);
        for (int i = 0; i < components.length; i++) {
            if (components[i]==null) continue;
            inv.setItem(i, components[i].getItem());
        }
        owner.openInventory(inv);
    }

    private GuiComponent getComponent(ItemStack stack) {
        for (GuiComponent component : pages.get(currentPage-1)) {
            if (component==null || stack==null) continue;
            ItemMeta meta1 = stack.getItemMeta(), meta2 = component.getItem().getItemMeta();
            if (meta1!=null && meta2!=null && meta1.getLocalizedName().equals(meta2.getLocalizedName())) return component;
        }
        return null;
    }

    private boolean checkInventory(InventoryInteractEvent event) {
        return event.getWhoClicked().getUniqueId().equals(owner.getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!checkInventory(event)) return;
        GuiComponent component = getComponent(event.getCurrentItem());
        if (component!=null) {
            GuiComponent.ClickEvent ce = new GuiComponent.ClickEvent(this, component, event.getWhoClicked());
            component.onClick(ce);
            event.setCancelled(ce.isCancelled());
        }
    }

    /**
     * call this to show the gui
     */
    public void show() {
        owner.openInventory(inv);
    }




}
