package de.cyklon.spigotutils.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private ItemMeta meta;
    private ItemStack item;

    public ItemBuilder(@NotNull Material material) {
        this(material, 1);
    }

    public ItemBuilder(@NotNull Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(@NotNull ItemStack stack) {
        item = stack;
        meta = item.getItemMeta();
    }

    public ItemBuilder setDisplayName(@Nullable String displayName) {
        meta.setDisplayName(displayName);
        return this;
    }

    public ItemBuilder setLocalizedName(@Nullable String localizedName) {
        meta.setLocalizedName(localizedName);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean breakable) {
        meta.setUnbreakable(breakable);
        return this;
    }

    public ItemBuilder addItemFlags(@NotNull ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder setCustomModelData(@Nullable Integer data) {
        meta.setCustomModelData(data);
        return this;
    }

    public ItemBuilder setLore(@Nullable List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    public ItemBuilder setLore(@NotNull String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder addLore(@Nullable List<String> lore) {
        List<String> oldLore = meta.getLore();
        if (oldLore==null && lore==null) return setLore((List<String>) null);
        if (lore==null) return this;
        if (oldLore==null) oldLore = new ArrayList<>();
        oldLore.addAll(lore);
        return setLore(oldLore);
    }

    public ItemBuilder addLore(@NotNull String... lore) {
        return addLore(Arrays.asList(lore));
    }

    public ItemBuilder removeLore(String regex) {
        List<String> lore = this.meta.getLore();
        if(lore != null) this.meta.setLore(lore.stream().filter(s -> !s.matches(regex)).toList());
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, Integer level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public <T, Z> ItemBuilder setPersistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    @Override
    public String toString() {
        return "ItemBuilder{" +
                "ItemMeta=" + meta +
                ", ItemStack=" + item +
                "}";
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }


}
