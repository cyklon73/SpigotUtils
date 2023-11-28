package de.cyklon.spigotutils.item;

import de.cyklon.spigotutils.adventure.Formatter;
import de.cyklon.spigotutils.nbt.NBTItem;
import de.cyklon.spigotutils.version.Version;
import net.kyori.adventure.text.Component;
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
import java.util.UUID;
import java.util.function.Consumer;

/**
 * The ItemBuilder is used to create item stacks easily with chained method calls
 * @see ItemStack
 * @see ItemMeta
 */
public class ItemBuilder {

    private ItemMeta meta;
    private ItemStack item;

    /**
     * Create a ItemBuilder with a specific Material
     * @param material the material of the Item
     * @see Material
     */
    public ItemBuilder(@NotNull Material material) {
        this(material, 1);
    }

    /**
     * Create a ItemBuilder with a specific Material and item amount
     * @param material the material of the Item
     * @param amount the item amount of the stack
     * @see Material
     */
    public ItemBuilder(@NotNull Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    /**
     * Create a ItemBuilder from a existing ItemStack
     * @param stack the stack to create the builder
     * @see ItemStack
     */
    public ItemBuilder(@NotNull ItemStack stack) {
        this.item = stack;
        this.meta = item.getItemMeta();
    }

    /**
     * set the display name of the stack
     * <p>
     * if the plugin runs on a paper server, and the display name contains the Default Prefix (§), the given display name will be formatted by {@link Formatter#parseText(String)}
     * @param displayName the new display name
     */
    public ItemBuilder setDisplayName(@Nullable String displayName) {
        if (displayName!=null && Version.isPaper() && displayName.contains(Formatter.getDefaultPrefix())) setDisplayName(Formatter.parseText(displayName));
        else meta.setDisplayName(displayName);
        return this;
    }

    /**
     * set the display name of the stack
     * @param displayName the new display name
     * @see Component
     */
    public ItemBuilder setDisplayName(@Nullable Component displayName) {
        Version.requirePaper();
        meta.displayName(displayName);
        return this;
    }


    /**
     * set the localized name of the stack
     * @param localizedName the new localized name
     */
    public ItemBuilder setLocalizedName(@Nullable String localizedName) {
        meta.setLocalizedName(localizedName);
        return this;
    }

    /**
     * set the breakable state of the stack
     * @param breakable the breakable state
     */
    public ItemBuilder setUnbreakable(boolean breakable) {
        meta.setUnbreakable(breakable);
        return this;
    }

    /**
     * add ItemFlags to the stack
     * @param flags the new flags
     * @see ItemFlag
     */
    public ItemBuilder addItemFlags(@NotNull ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    /**
     * set the custom model data of the stack
     * @param data the Custom Model Data
     */
    public ItemBuilder setCustomModelData(@Nullable Integer data) {
        meta.setCustomModelData(data);
        return this;
    }

    /**
     * set the Lore of the ItemStack.
     * <p>
     * every List entry is a new Line
     * @param lore the new Lore
     * @see List
     */
    public ItemBuilder setLore(@Nullable List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    /**
     * set the Lore of the ItemStack.
     * <p>
     * every List entry is a new Line
     * @param lore the new Lore
     * @see List
     * @see Component
     */
    public ItemBuilder setAdventureLore(@Nullable List<? extends Component> lore) {
        Version.requirePaper();
        meta.lore(lore);
        return this;
    }

    /**
     * set the Lore of the ItemStack.
     * <p>
     * every Array entry is a new Line
     * @param lore the new Lore
     */
    public ItemBuilder setLore(@NotNull String... lore) {
        return setLore(Arrays.asList(lore));
    }


    /**
     * set the Lore of the ItemStack.
     * <p>
     * every Array entry is a new Line
     * @param lore the new Lore
     * @see Component
     */
    public ItemBuilder setLore(@NotNull Component... lore) {
        return setAdventureLore(Arrays.asList(lore));
    }


    /**
     * Adding lines to the lore without overwriting the old lore
     * <p>
     * every List entry is a new Line
     * @param lore the new Lore
     * @see List
     */
    public ItemBuilder addLore(@Nullable List<String> lore) {
        List<String> oldLore = meta.getLore();
        if (oldLore==null && lore==null) return setLore((List<String>) null);
        if (lore==null) return this;
        if (oldLore==null) oldLore = new ArrayList<>();
        oldLore.addAll(lore);
        return setLore(oldLore);
    }

    /**
     * Adding lines to the lore without overwriting the old lore
     * <p>
     * every List entry is a new Line
     * @param lore the new Lore
     * @see List
     * @see Component
     */
    public ItemBuilder addAdventureLore(@Nullable List<Component> lore) {
        List<Component> oldLore = meta.lore();
        if (oldLore==null && lore==null) return setAdventureLore(null);
        if (lore==null) return this;
        if (oldLore==null) oldLore = new ArrayList<>();
        oldLore.addAll(lore);
        return setAdventureLore(oldLore);
    }

    /**
     * Adding lines to the lore without overwriting the old lore
     * <p>
     * every Array entry is a new Line
     * @param lore the new Lore
     */
    public ItemBuilder addLore(@NotNull String... lore) {
        return addLore(Arrays.asList(lore));
    }

    /**
     * Adding lines to the lore without overwriting the old lore
     * <p>
     * every Array entry is a new Line
     * @param lore the new Lore
     * @see Component
     */
    public ItemBuilder addLore(@NotNull Component... lore) {
        return addAdventureLore(Arrays.asList(lore));
    }

    /**
     * removes all lines from the lore that match the regex
     * @param regex the regex to check
     * @see java.util.regex.Pattern
     */
    public ItemBuilder removeLore(String regex) {
        List<String> lore = this.meta.getLore();
        if(lore != null) this.meta.setLore(lore.stream().filter(s -> !s.matches(regex)).toList());
        return this;
    }

    /**
     * removes the lore line at the index
     * @param index the line index to remove
     */
    public ItemBuilder removeLore(int index) {
        if (Version.isPaper()) {
            List<Component> lore = this.meta.lore();
            if (lore!=null) lore.remove(index);
            this.meta.lore(lore);
        } else {
            List<String> lore = this.meta.getLore();
            if (lore!=null) lore.remove(index);
            this.meta.setLore(lore);
        }
        return this;
    }

    /**
     * add a Enchantment to the ItemStack
     * @param enchantment the enchantment to add
     * @param level the level of the enchantment
     * @see Enchantment
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, Integer level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * set the item amount of the stack
     * @param amount the new amount
     */
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * set a persistent data to the stack
     * @param key the key of the data
     * @param type the data type of the data
     * @param value the data to save
     * @param <T> the data type in which the data will be stored
     * @param <Z> the data type of the actual data
     * @see org.bukkit.persistence.PersistentDataContainer
     * @see NamespacedKey
     * @see PersistentDataType
     */
    public <T, Z> ItemBuilder setPersistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    private ItemBuilder processNBT(Consumer<NBTItem> consumer) {
        NBTItem nbt = new NBTItem(item);
        consumer.accept(nbt);
        item = nbt.getItem();
        return this;
    }

    public ItemBuilder setNBT(String key, int value) {
        return processNBT(nbt -> nbt.set(key, value));
    }

    public ItemBuilder setNBT(String key, byte value) {
        return processNBT(nbt -> nbt.set(key, value));
    }

    public ItemBuilder setNBT(String key, long value) {
        return processNBT(nbt -> nbt.set(key, value));
    }
    public ItemBuilder setNBT(String key, UUID value) {
        return processNBT(nbt -> nbt.set(key, value));
    }
    public ItemBuilder setNBT(String key, float value) {
        return processNBT(nbt -> nbt.set(key, value));
    }

    public ItemBuilder setNBT(String key, int[] value) {
        return processNBT(nbt -> nbt.set(key, value));
    }
    public ItemBuilder setNBT(String key, short value) {
        return processNBT(nbt -> nbt.set(key, value));
    }
    public ItemBuilder setNBT(String key, byte[] value) {
        return processNBT(nbt -> nbt.set(key, value));
    }

    public ItemBuilder setNBT(String key, double value) {
        return processNBT(nbt -> nbt.set(key, value));
    }

    public ItemBuilder setNBT(String key, long[] value) {
        return processNBT(nbt -> nbt.set(key, value));
    }
    public ItemBuilder setNBT(String key, String value) {
        return processNBT(nbt -> nbt.set(key, value));
    }

    public ItemBuilder setNBT(String key, boolean value) {
        return processNBT(nbt -> nbt.set(key, value));
    }





    /**
     * @return the builder as String
     */
    @Override
    public String toString() {
        return "ItemBuilder{" +
                "ItemMeta=" + meta +
                ", ItemStack=" + item +
                "}";
    }

    /**
     * builds the builder to the actual stack
     * @return the finished item stack
     */
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }


}
