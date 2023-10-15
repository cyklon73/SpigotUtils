package de.cyklon.spigotutils.ui.textbox;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public final class TextBox {

    private BossBar bar;

    public TextBox(NamespacedKey key, BarColor color, BarStyle style) {
        this.bar = Bukkit.getBossBar(key);
        if (this.bar==null) this.bar = Bukkit.createBossBar(key, "", color, style);
    }

    public void add(Player player) {
        bar.addPlayer(player);
    }

    public void addAll() {
        Bukkit.getOnlinePlayers().forEach(this::add);
    }

    public void remove(Player player) {
        bar.removePlayer(player);
    }

    public void removeAll() {
        bar.removeAll();
    }

    public void setVisible(boolean visible) {
        bar.setVisible(visible);
    }

    public boolean isVisible() {
        return bar.isVisible();
    }

    public void clear() {
        bar.setTitle("");
    }

    public void setText(String text) {
        bar.setTitle(text);
    }

    public void send(String msg) {
        bar.setTitle(bar.getTitle() + "\n" + msg);
    }

}
