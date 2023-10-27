package de.cyklon.spigotutils.persistence;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Function;

public final class PersistentDataHandler {

    private final PersistentDataContainer container;

    public PersistentDataHandler(@NotNull PersistentDataContainer container) {
        this.container = container;
    }

    public PersistentDataHandler(PersistentDataHolder holder) {
        this(holder.getPersistentDataContainer());
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

    public <T, Z> void revise(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Function<Z, Z> function) {
        set(namespacedKey, persistentDataType, function.apply(get(namespacedKey, persistentDataType)));
    }

    public void reviseByte(@NotNull NamespacedKey namespacedKey, @NotNull Function<Byte, Byte> function) {
        revise(namespacedKey, PersistentDataType.BYTE, function);
    }

    public void reviseShort(@NotNull NamespacedKey namespacedKey, @NotNull Function<Short, Short> function) {
        revise(namespacedKey, PersistentDataType.SHORT, function);
    }

    public void reviseInt(@NotNull NamespacedKey namespacedKey, @NotNull Function<Integer, Integer> function) {
        revise(namespacedKey, PersistentDataType.INTEGER, function);
    }

    public void reviseLong(@NotNull NamespacedKey namespacedKey, @NotNull Function<Long, Long> function) {
        revise(namespacedKey, PersistentDataType.LONG, function);
    }

    public void reviseFloat(@NotNull NamespacedKey namespacedKey, @NotNull Function<Float, Float> function) {
        revise(namespacedKey, PersistentDataType.FLOAT, function);
    }

    public void reviseDouble(@NotNull NamespacedKey namespacedKey, @NotNull Function<Double, Double> function) {
        revise(namespacedKey, PersistentDataType.DOUBLE, function);
    }

    public void reviseBool(@NotNull NamespacedKey namespacedKey, @NotNull Function<Boolean, Boolean> function) {
        revise(namespacedKey, PersistentDataType.BOOLEAN, function);
    }

    public void reviseString(@NotNull NamespacedKey namespacedKey, @NotNull Function<String, String> function) {
        revise(namespacedKey, PersistentDataType.STRING, function);
    }

    public void reviseByteArray(@NotNull NamespacedKey namespacedKey, @NotNull Function<byte[], byte[]> function) {
        revise(namespacedKey, PersistentDataType.BYTE_ARRAY, function);
    }

    public void reviseIntArray(@NotNull NamespacedKey namespacedKey, @NotNull Function<int[], int[]> function) {
        revise(namespacedKey, PersistentDataType.INTEGER_ARRAY, function);
    }

    public void reviseLongArray(@NotNull NamespacedKey namespacedKey, @NotNull Function<long[], long[]> function) {
        revise(namespacedKey, PersistentDataType.LONG_ARRAY, function);
    }

    public void reviseTagContainerArray(@NotNull NamespacedKey namespacedKey, @NotNull Function<PersistentDataContainer[], PersistentDataContainer[]> function) {
        revise(namespacedKey, PersistentDataType.TAG_CONTAINER_ARRAY, function);
    }

    public void reviseTagContainer(@NotNull NamespacedKey namespacedKey, @NotNull Function<PersistentDataContainer, PersistentDataContainer> function) {
        revise(namespacedKey, PersistentDataType.TAG_CONTAINER, function);
    }

    public <T, Z> void reviseWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Function<Z, Z> function, @NotNull Z def) {
        set(namespacedKey, persistentDataType, function.apply(getOrDefault(namespacedKey, persistentDataType, def)));
    }

    public void reviseByteWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Byte, Byte> function, @NotNull Byte def) {
        reviseWithDefault(namespacedKey, PersistentDataType.BYTE, function, def);
    }

    public void reviseShortWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Short, Short> function, @NotNull Short def) {
        reviseWithDefault(namespacedKey, PersistentDataType.SHORT, function, def);
    }

    public void reviseIntWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Integer, Integer> function, @NotNull Integer def) {
        reviseWithDefault(namespacedKey, PersistentDataType.INTEGER, function, def);
    }

    public void reviseLongWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Long, Long> function, @NotNull Long def) {
        reviseWithDefault(namespacedKey, PersistentDataType.LONG, function, def);
    }

    public void reviseFloatWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Float, Float> function, @NotNull Float def) {
        reviseWithDefault(namespacedKey, PersistentDataType.FLOAT, function, def);
    }

    public void reviseDoubleWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Double, Double> function, @NotNull Double def) {
        reviseWithDefault(namespacedKey, PersistentDataType.DOUBLE, function, def);
    }

    public void reviseBoolWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<Boolean, Boolean> function, @NotNull Boolean def) {
        reviseWithDefault(namespacedKey, PersistentDataType.BOOLEAN, function, def);
    }

    public void reviseStringWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<String, String> function, @NotNull String def) {
        reviseWithDefault(namespacedKey, PersistentDataType.STRING, function, def);
    }

    public void reviseByteArrayWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<byte[], byte[]> function, byte @NotNull [] def) {
        reviseWithDefault(namespacedKey, PersistentDataType.BYTE_ARRAY, function, def);
    }

    public void reviseIntArrayWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<int[], int[]> function, int @NotNull [] def) {
        reviseWithDefault(namespacedKey, PersistentDataType.INTEGER_ARRAY, function, def);
    }

    public void reviseLongArrayWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<long[], long[]> function, long @NotNull [] def) {
        reviseWithDefault(namespacedKey, PersistentDataType.LONG_ARRAY, function, def);
    }

    public void reviseTagContainerArrayWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<PersistentDataContainer[], PersistentDataContainer[]> function, @NotNull PersistentDataContainer[] def) {
        reviseWithDefault(namespacedKey, PersistentDataType.TAG_CONTAINER_ARRAY, function, def);
    }

    public void reviseTagContainerWithDefault(@NotNull NamespacedKey namespacedKey, @NotNull Function<PersistentDataContainer, PersistentDataContainer> function, @NotNull PersistentDataContainer def) {
        reviseWithDefault(namespacedKey, PersistentDataType.TAG_CONTAINER, function, def);
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
