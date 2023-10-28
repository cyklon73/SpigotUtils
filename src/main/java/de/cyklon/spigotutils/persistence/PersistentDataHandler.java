package de.cyklon.spigotutils.persistence;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Set;
import java.util.function.Function;

public final class PersistentDataHandler {

    private final PersistentDataContainer container;

    private PersistentDataHandler(@NotNull PersistentDataContainer container) {
        this.container = container;
    }

    public static PersistentDataHandler get(@NotNull PersistentDataContainer container) {
        return new PersistentDataHandler(container);
    }

    public static PersistentDataHandler get(@NotNull PersistentDataHolder holder) {
        return new PersistentDataHandler(holder.getPersistentDataContainer());
    }

    public static PersistentDataHandler get(@NotNull File file) {
        return new PersistentDataHandler(new PersistentDataFile(file));
    }

    public <T, Z> void set(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Z z) {
        container.set(namespacedKey, persistentDataType, z);
    }

    public void set(@NotNull NamespacedKey namespacedKey, @NotNull Byte value){
        set(namespacedKey, PersistentDataType.BYTE, value);
    }

    public void set(@NotNull NamespacedKey namespacedKey, @NotNull Short value){
        set(namespacedKey, PersistentDataType.SHORT, value);
    }

    public void set(@NotNull NamespacedKey namespacedKey, @NotNull Integer value){
        set(namespacedKey, PersistentDataType.INTEGER, value);
    }

    public void set(@NotNull NamespacedKey namespacedKey, @NotNull Long value){
        set(namespacedKey, PersistentDataType.LONG, value);
    }

    public void set(@NotNull NamespacedKey namespacedKey, @NotNull Float value){
        set(namespacedKey, PersistentDataType.FLOAT, value);
    }

    public void set(@NotNull NamespacedKey namespacedKey, @NotNull Double value){
        set(namespacedKey, PersistentDataType.DOUBLE, value);
    }

    public void set(@NotNull NamespacedKey namespacedKey, @NotNull Boolean value){
        set(namespacedKey, PersistentDataType.BOOLEAN, value);
    }

    public void set(@NotNull NamespacedKey namespacedKey, @NotNull String value){
        set(namespacedKey, PersistentDataType.STRING, value);
    }

    public void set(@NotNull NamespacedKey namespacedKey, byte[] value){
        set(namespacedKey, PersistentDataType.BYTE_ARRAY, value);
    }

    public void set(@NotNull NamespacedKey namespacedKey, int[] value){
        set(namespacedKey, PersistentDataType.INTEGER_ARRAY, value);
    }

    public void set(@NotNull NamespacedKey namespacedKey, long[] value){
        set(namespacedKey, PersistentDataType.LONG_ARRAY, value);
    }

    public void set(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataContainer[] value){
        set(namespacedKey, PersistentDataType.TAG_CONTAINER_ARRAY, value);
    }

    public void set(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataContainer value){
        set(namespacedKey, PersistentDataType.TAG_CONTAINER, value);
    }

    public <T, Z> Z revise(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Function<Z, Z> function) {
        Z val = function.apply(get(namespacedKey, persistentDataType));
        set(namespacedKey, persistentDataType, val);
        return val;
    }

    public Byte reviseByte(@NotNull NamespacedKey namespacedKey, @NotNull Function<Byte, Byte> function) {
        return revise(namespacedKey, PersistentDataType.BYTE, function);
    }

    public Short reviseShort(@NotNull NamespacedKey namespacedKey, @NotNull Function<Short, Short> function) {
        return revise(namespacedKey, PersistentDataType.SHORT, function);
    }

    public Integer reviseInt(@NotNull NamespacedKey namespacedKey, @NotNull Function<Integer, Integer> function) {
        return revise(namespacedKey, PersistentDataType.INTEGER, function);
    }

    public Long reviseLong(@NotNull NamespacedKey namespacedKey, @NotNull Function<Long, Long> function) {
        return revise(namespacedKey, PersistentDataType.LONG, function);
    }

    public Float reviseFloat(@NotNull NamespacedKey namespacedKey, @NotNull Function<Float, Float> function) {
        return revise(namespacedKey, PersistentDataType.FLOAT, function);
    }

    public Double reviseDouble(@NotNull NamespacedKey namespacedKey, @NotNull Function<Double, Double> function) {
        return revise(namespacedKey, PersistentDataType.DOUBLE, function);
    }

    public Boolean reviseBool(@NotNull NamespacedKey namespacedKey, @NotNull Function<Boolean, Boolean> function) {
        return revise(namespacedKey, PersistentDataType.BOOLEAN, function);
    }

    public String reviseString(@NotNull NamespacedKey namespacedKey, @NotNull Function<String, String> function) {
        return revise(namespacedKey, PersistentDataType.STRING, function);
    }

    public byte[] reviseByteArray(@NotNull NamespacedKey namespacedKey, @NotNull Function<byte[], byte[]> function) {
        return revise(namespacedKey, PersistentDataType.BYTE_ARRAY, function);
    }

    public int[] reviseIntArray(@NotNull NamespacedKey namespacedKey, @NotNull Function<int[], int[]> function) {
        return revise(namespacedKey, PersistentDataType.INTEGER_ARRAY, function);
    }

    public long[] reviseLongArray(@NotNull NamespacedKey namespacedKey, @NotNull Function<long[], long[]> function) {
        return revise(namespacedKey, PersistentDataType.LONG_ARRAY, function);
    }

    public PersistentDataContainer[] reviseTagContainerArray(@NotNull NamespacedKey namespacedKey, @NotNull Function<PersistentDataContainer[], PersistentDataContainer[]> function) {
        return revise(namespacedKey, PersistentDataType.TAG_CONTAINER_ARRAY, function);
    }

    public PersistentDataContainer reviseTagContainer(@NotNull NamespacedKey namespacedKey, @NotNull Function<PersistentDataContainer, PersistentDataContainer> function) {
        return revise(namespacedKey, PersistentDataType.TAG_CONTAINER, function);
    }

    public <T, Z> Z reviseWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Function<Z, Z> function, @NotNull Z def) {
        Z val = function.apply(getOrDefault(namespacedKey, persistentDataType, def));
        set(namespacedKey, persistentDataType, val);
        return val;
    }

    public Byte reviseByteWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Byte, Byte> function, @NotNull Byte def) {
        return reviseWithDefault(namespacedKey, PersistentDataType.BYTE, function, def);
    }

    public Short reviseShortWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Short, Short> function, @NotNull Short def) {
        return reviseWithDefault(namespacedKey, PersistentDataType.SHORT, function, def);
    }

    public Integer reviseIntWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Integer, Integer> function, @NotNull Integer def) {
        return reviseWithDefault(namespacedKey, PersistentDataType.INTEGER, function, def);
    }

    public Long reviseLongWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Long, Long> function, @NotNull Long def) {
        return reviseWithDefault(namespacedKey, PersistentDataType.LONG, function, def);
    }

    public Float reviseFloatWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Float, Float> function, @NotNull Float def) {
        return reviseWithDefault(namespacedKey, PersistentDataType.FLOAT, function, def);
    }

    public Double reviseDoubleWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Double, Double> function, @NotNull Double def) {
        return reviseWithDefault(namespacedKey, PersistentDataType.DOUBLE, function, def);
    }

    public Boolean reviseBoolWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Boolean, Boolean> function, @NotNull Boolean def) {
        return reviseWithDefault(namespacedKey, PersistentDataType.BOOLEAN, function, def);
    }

    public String reviseStringWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<String, String> function, @NotNull String def) {
        return reviseWithDefault(namespacedKey, PersistentDataType.STRING, function, def);
    }

    public byte[] reviseByteArrayWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<byte[], byte[]> function, byte @NotNull [] def) {
        return reviseWithDefault(namespacedKey, PersistentDataType.BYTE_ARRAY, function, def);
    }

    public int[] reviseIntArrayWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<int[], int[]> function, int @NotNull [] def) {
        return reviseWithDefault(namespacedKey, PersistentDataType.INTEGER_ARRAY, function, def);
    }

    public long[] reviseLongArrayWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<long[], long[]> function, long @NotNull [] def) {
        return reviseWithDefault(namespacedKey, PersistentDataType.LONG_ARRAY, function, def);
    }

    public PersistentDataContainer[] reviseTagContainerArrayWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<PersistentDataContainer[], PersistentDataContainer[]> function, @NotNull PersistentDataContainer[] def) {
        return reviseWithDefault(namespacedKey, PersistentDataType.TAG_CONTAINER_ARRAY, function, def);
    }

    public PersistentDataContainer reviseTagContainerWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<PersistentDataContainer, PersistentDataContainer> function, @NotNull PersistentDataContainer def) {
        return reviseWithDefault(namespacedKey, PersistentDataType.TAG_CONTAINER, function, def);
    }

    public <T, Z> boolean has(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType) {
        return container.has(namespacedKey, persistentDataType);
    }

    public boolean contains(@NotNull NamespacedKey namespacedKey) {
        return getKeys().contains(namespacedKey);
    }

    @Nullable
    public <T, Z> Z get(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType) {
        return container.get(namespacedKey, persistentDataType);
    }

    @Nullable
    public Byte getByte(@NotNull NamespacedKey namespacedKey) {
        return get(namespacedKey, PersistentDataType.BYTE);
    }

    @Nullable
    public Short getShort(@NotNull NamespacedKey namespacedKey) {
        return get(namespacedKey, PersistentDataType.SHORT);
    }

    @Nullable
    public Integer getInt(@NotNull NamespacedKey namespacedKey) {
        return get(namespacedKey, PersistentDataType.INTEGER);
    }

    @Nullable
    public Long getLong(@NotNull NamespacedKey namespacedKey) {
        return get(namespacedKey, PersistentDataType.LONG);
    }

    @Nullable
    public Float getFloat(@NotNull NamespacedKey namespacedKey) {
        return get(namespacedKey, PersistentDataType.FLOAT);
    }

    @Nullable
    public Double getDouble(@NotNull NamespacedKey namespacedKey) {
        return get(namespacedKey, PersistentDataType.DOUBLE);
    }

    @Nullable
    public Boolean getBool(@NotNull NamespacedKey namespacedKey) {
        return get(namespacedKey, PersistentDataType.BOOLEAN);
    }

    @Nullable
    public String getString(@NotNull NamespacedKey namespacedKey) {
        return get(namespacedKey, PersistentDataType.STRING);
    }

    public byte @Nullable [] getByteArray(@NotNull NamespacedKey namespacedKey) {
        return get(namespacedKey, PersistentDataType.BYTE_ARRAY);
    }

    public int @Nullable [] getIntArray(@NotNull NamespacedKey namespacedKey) {
        return get(namespacedKey, PersistentDataType.INTEGER_ARRAY);
    }

    public long @Nullable [] getLongArray(@NotNull NamespacedKey namespacedKey) {
        return get(namespacedKey, PersistentDataType.LONG_ARRAY);
    }

    @Nullable
    public PersistentDataContainer[] getTagContainerArray(@NotNull NamespacedKey namespacedKey) {
        return get(namespacedKey, PersistentDataType.TAG_CONTAINER_ARRAY);
    }

    @Nullable
    public PersistentDataContainer getTagContainer(@NotNull NamespacedKey namespacedKey) {
        return get(namespacedKey, PersistentDataType.TAG_CONTAINER);
    }

    @NotNull
    public <T, Z> Z getOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Z z) {
        return container.getOrDefault(namespacedKey, persistentDataType, z);
    }

    @NotNull
    public Byte getByteOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull Byte def) {
        return getOrDefault(namespacedKey, PersistentDataType.BYTE, def);
    }

    @NotNull
    public Short getShortOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull Short def) {
        return getOrDefault(namespacedKey, PersistentDataType.SHORT, def);
    }

    @NotNull
    public Integer getIntOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull Integer def) {
        return getOrDefault(namespacedKey, PersistentDataType.INTEGER, def);
    }

    @NotNull
    public Long getLongOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull Long def) {
        return getOrDefault(namespacedKey, PersistentDataType.LONG, def);
    }

    @NotNull
    public Float getFloatOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull Float def) {
        return getOrDefault(namespacedKey, PersistentDataType.FLOAT, def);
    }

    @NotNull
    public Double getDoubleOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull Double def) {
        return getOrDefault(namespacedKey, PersistentDataType.DOUBLE, def);
    }

    @NotNull
    public Boolean getBoolOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull Boolean def) {
        return getOrDefault(namespacedKey, PersistentDataType.BOOLEAN, def);
    }

    @NotNull
    public String getStringOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull String def) {
        return getOrDefault(namespacedKey, PersistentDataType.STRING, def);
    }


    public byte @NotNull [] getByteArrayOrDefault(@NotNull NamespacedKey namespacedKey, byte @NotNull [] def) {
        return getOrDefault(namespacedKey, PersistentDataType.BYTE_ARRAY, def);
    }

    public int @NotNull [] getIntArrayOrDefault(@NotNull NamespacedKey namespacedKey, int @NotNull [] def) {
        return getOrDefault(namespacedKey, PersistentDataType.INTEGER_ARRAY, def);
    }

    public long @NotNull [] getLongArrayOrDefault(@NotNull NamespacedKey namespacedKey, long @NotNull [] def) {
        return getOrDefault(namespacedKey, PersistentDataType.LONG_ARRAY, def);
    }

    @NotNull
    public PersistentDataContainer[] getTagContainerArrayOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataContainer[] def) {
        return getOrDefault(namespacedKey, PersistentDataType.TAG_CONTAINER_ARRAY, def);
    }

    @NotNull
    public PersistentDataContainer getTagContainerOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataContainer def) {
        return getOrDefault(namespacedKey, PersistentDataType.TAG_CONTAINER, def);
    }

    @NotNull
    public Set<NamespacedKey> getKeys() {
        return container.getKeys();
    }

    public void remove(@NotNull NamespacedKey namespacedKey) {
        container.remove(namespacedKey);
    }

    public boolean isEmpty() {
        return container.isEmpty();
    }

    @NotNull
    public PersistentDataAdapterContext getAdapterContext() {
        return container.getAdapterContext();
    }

    @NotNull
    public PersistentDataContainer getContainer() {
        return container;
    }
}
