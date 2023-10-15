package de.cyklon.spigotutils.ui.textbox;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public final class TextBox {

    private final static String PAD = "";
    private BossBar[] bars;

    public TextBox(Plugin plugin, String id, BarColor color, BarStyle style) {
        this.bars = new BossBar[4];
        for (int i = 0; i < bars.length; i++) {
            NamespacedKey key = new NamespacedKey(plugin, "textbox-" + id + "-" + i);
            this.bars[i] = Bukkit.getBossBar(key);
            if (this.bars[i]==null) this.bars[i] = Bukkit.createBossBar(key, "", color, style);
        }
    }

    public void add(Player player) {
        for (BossBar bar : bars) bar.addPlayer(player);
    }

    public void addAll() {
        Bukkit.getOnlinePlayers().forEach(this::add);
    }

    public void remove(Player player) {
        for (BossBar bar : bars) bar.removePlayer(player);
    }

    public void removeAll() {
        for (BossBar bar : bars) bar.removeAll();
    }

    public void setVisible(boolean visible) {
        for (BossBar bar : bars) bar.setVisible(visible);
    }

    public boolean isVisible() {
        return bars[0].isVisible();
    }

    public void clear() {
        for (BossBar bar : bars) bar.setTitle("");
    }

    public void setText(String text) {
        for (BossBar bar : bars) bar.setTitle(text);
    }

    private void move() {
        for (int i = 0; i < bars.length; i++) {
            String text = bars[i].getTitle();
            bars[i].setTitle("");
            if (i==0) continue;
            bars[i-1].setTitle(text);
        }
    }

    private boolean sendToBlank(String msg) {
        for (BossBar bar : bars) {
            if (bar.getTitle().isBlank()) {
                bar.setTitle(msg);
                return true;
            }
        }
        return false;
    }

    public void send(String msg) {
        while (!sendToBlank(msg)) move();
    }

}
