package de.cyklon.spigotutils.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class SkullManager {

    public static final ItemStack OAK_WOOD_ARROW_LEFT = getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==");
    public static final ItemStack OAK_WOOD_ARROW_RIGHT = getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19");

    /**
     * get the player head of a specific player
     * @param player the player
     * @return the player head
     */
    public static ItemStack getPlayerHead(@NotNull OfflinePlayer player) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        if (meta == null) return stack;
        meta.setOwningPlayer(player);
        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * get a custom player head from a head url
     * @param url the head url
     * @return the custom head
     */
    public static ItemStack getCustomHeadFromUrl(String url) {
        return getCustomHead(new String(Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes())));
    }

    /**
     * get a custom player head from the texture value
     * <p>
     * you can get the value from different websites, as example: https://minecraft-heads.com/custom-heads/
     * @param base64 the texture value
     * @return the custom player head
     */
    public static ItemStack getCustomHead(@NotNull String base64) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);

        if (base64.isBlank()) return stack;

        ItemMeta meta = stack.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", base64));
        Field profileField = null;

        try {
            profileField = meta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        profileField.setAccessible(true);

        try {
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        stack.setItemMeta(meta);
        return stack;
    }

}
