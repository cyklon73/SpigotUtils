package de.cyklon.spigotutils.gui;

import de.cyklon.spigotutils.gui.component.GuiComponent;
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
import java.util.function.Function;

public abstract class Gui implements Listener {

    private final String id;
    private final int rows;
    private final BiFunction<Integer, Integer, String> title;
    private int currentPage = 1;
    private final List<GuiComponent[]> pages;
    private Inventory inv;
    private final Player owner;

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

    public void setCurrentPage(int page) {
        this.currentPage = page;
        update();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageAmount() {
        return pages.size();
    }

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

    public void show() {
        owner.openInventory(inv);
    }




}
