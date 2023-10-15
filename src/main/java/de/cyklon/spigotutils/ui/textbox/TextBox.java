package de.cyklon.spigotutils.ui.textbox;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * for the TextBox it is recommended to use a ServerTexture pack in which one of the BossBar textures is completely transparent.
 */
public final class TextBox {

    private final static String PAD = "";
    private BossBar[] bars;

    /**
     * Using the ID, the text field is persistent,
     * meaning that if the server is reloaded or restarted,
     * the text field and the text in it are preserved and simply reloaded when this constructor is loaded with the appropriate ID.
     * <p>
     * With color and style you have to select the BossBar that was made transparent with the Texture Pack.
     * @param plugin the plugin which creates the TextBox
     * @param id an id to create and load the text box
     * @param color the color of the boss bar you made transparent in the texture pack
     * @param style the style of the boss bar you made transparent in the texture pack
     */
    public TextBox(Plugin plugin, String id, BarColor color, BarStyle style) {
        this.bars = new BossBar[4];
        for (int i = 0; i < bars.length; i++) {
            NamespacedKey key = new NamespacedKey(plugin, "textbox-" + id + "-" + i);
            this.bars[i] = Bukkit.getBossBar(key);
            if (this.bars[i]==null) this.bars[i] = Bukkit.createBossBar(key, "", color, style);
        }
    }

    /**
     * adds a player to the text box who can see it when it is visible
     * @param player the player to be added
     */
    public void add(Player player) {
        for (BossBar bar : bars) bar.addPlayer(player);
    }

    /**
     * adds all online players to the text box which can see it when it is visible
     */
    public void addAll() {
        Bukkit.getOnlinePlayers().forEach(this::add);
    }

    /**
     * removes a player from the text box, this player can then no longer see the text box when it is visible
     * @param player the player to be removed
     */
    public void remove(Player player) {
        for (BossBar bar : bars) bar.removePlayer(player);
    }

    /**
     * removes all players from the text box, so no player can see the text box anymore, even if it is visible
     */
    public void removeAll() {
        for (BossBar bar : bars) bar.removeAll();
    }

    /**
     * makes the textbox visible or invisible for all added players
     * @param visible the visibility state
     */
    public void setVisible(boolean visible) {
        for (BossBar bar : bars) bar.setVisible(visible);
    }

    /**
     * @return true if the text box is visible
     */
    public boolean isVisible() {
        return bars[0].isVisible();
    }

    /**
     * removes the complete text from the text box
     */
    public void clear() {
        for (BossBar bar : bars) bar.setTitle("");
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

    /**
     * sends a line of text to the text box
     * @param msg the text to be sent
     */
    public void send(String msg) {
        while (!sendToBlank(msg)) move();
    }

}
