package de.cyklon.spigotutils.nbt;

import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static de.cyklon.spigotutils.nms.NMSReflection.nmsClass;
import static de.cyklon.spigotutils.nms.NMSReflection.obcClass;

abstract class DefaultNBT implements NBTHolder {

	protected final static Class<?> craftItemStackClass;
	protected final static Class<?> nmsItemStackClass;
	protected final static Class<?> nbtTagCompoundClass;

	/**
	 * {@link org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack#asNMSCopy(ItemStack)}
	 */
	protected final static MethodHandle AS_NMS_COPY;
	/**
	 * {@link net.minecraft.world.item.ItemStack#w()}
	 * <p>
	 * Get or create NBTTagCompound
	 */
	protected final static MethodHandle GET_OR_CREATE_NBT_TAG_COMPOUND;
	/**
	 * {@link org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack#asCraftMirror(net.minecraft.world.item.ItemStack)}
	 */
	protected final static MethodHandle AS_CRAFT_MIRROR;
	/**
	 * {@link net.minecraft.world.item.ItemStack#c(net.minecraft.nbt.NBTTagCompound)} )} )}
	 */
	protected final static MethodHandle SET_NBT_TAG_COMPOUND;


	/*
	 * ==================================================
	 * set methods in {@link NBTTagCompound}
	 * ==================================================
	 */

	protected final static MethodHandle SET_INT;
	protected final static MethodHandle SET_BYTE;
	protected final static MethodHandle SET_LONG;
	protected final static MethodHandle SET_UUID;
	protected final static MethodHandle SET_FLOAT;
	protected final static MethodHandle SET_INT_ARRAY;
	protected final static MethodHandle SET_SHORT;
	protected final static MethodHandle SET_BYTE_ARRAY;
	protected final static MethodHandle SET_DOUBLE;
	protected final static MethodHandle SET_LONG_ARRAY;
	protected final static MethodHandle SET_STRING;
	protected final static MethodHandle SET_BOOLEAN;
	protected final static MethodHandle SET_BYTE_LIST;
	protected final static MethodHandle SET_INT_LIST;
	protected final static MethodHandle SET_LONG_LIST;


	/*
	 * ==================================================
	 * get methods in {@link NBTTagCompound}
	 * ==================================================
	 */

	protected final static MethodHandle GET_BOOLEAN;
	protected final static MethodHandle GET_BYTE;
	protected final static MethodHandle GET_BYTE_ARRAY;
	protected final static MethodHandle GET_DOUBLE;
	protected final static MethodHandle GET_FLOAT;
	protected final static MethodHandle GET_INT;
	protected final static MethodHandle GET_INT_ARRAY;
	protected final static MethodHandle GET_LONG;
	protected final static MethodHandle GET_LONG_ARRAY;
	protected final static MethodHandle GET_SHORT;
	protected final static MethodHandle GET_STRING;
	protected final static MethodHandle GET_UUID;


	/*
	 * ==================================================
	 * other methods in {@link NBTTagCompound}
	 * ==================================================
	 */

	protected final static MethodHandle KEY_SET;
	protected final static MethodHandle SIZE;
	protected final static MethodHandle CONTAINS_KEY;
	protected final static MethodHandle IS_EMPTY;
	protected final static MethodHandle REMOVE;

	private static MethodHandle findSetter(Class<?> type, MethodHandles.Lookup lookup) throws NoSuchMethodException, IllegalAccessException {
		return findSetter(type, "a", lookup);
	}

	private static MethodHandle findSetter(Class<?> type, String methodName, MethodHandles.Lookup lookup) throws NoSuchMethodException, IllegalAccessException {
		return lookup.findVirtual(nbtTagCompoundClass, methodName, MethodType.methodType(void.class, String.class, type));
	}

	private static MethodHandle findGetter(Class<?> type, String methodName, MethodHandles.Lookup lookup) throws NoSuchMethodException, IllegalAccessException {
		return lookup.findVirtual(nbtTagCompoundClass, methodName, MethodType.methodType(type, String.class));
	}

	private static MethodHandle findMethod(String methodName, Class<?> returnType, MethodHandles.Lookup lookup) throws NoSuchMethodException, IllegalAccessException {
		return findMethod(methodName, returnType, null, lookup);
	}

	private static MethodHandle findMethod(String methodName, Class<?> returnType, Class<?> parameterType, MethodHandles.Lookup lookup) throws NoSuchMethodException, IllegalAccessException {
		return lookup.findVirtual(nbtTagCompoundClass, methodName, parameterType==null ? MethodType.methodType(returnType) : MethodType.methodType(returnType, parameterType));
	}

	static {
		try {
			MethodHandles.Lookup lookup = MethodHandles.lookup();
			craftItemStackClass = obcClass("inventory.CraftItemStack");
			nmsItemStackClass = nmsClass("world.item", "ItemStack");
			nbtTagCompoundClass = nmsClass("nbt", "NBTTagCompound");

			AS_NMS_COPY = lookup.findStatic(craftItemStackClass, "asNMSCopy", MethodType.methodType(nmsItemStackClass, ItemStack.class));
			AS_CRAFT_MIRROR = lookup.findStatic(craftItemStackClass, "asCraftMirror", MethodType.methodType(craftItemStackClass, nmsItemStackClass));
			GET_OR_CREATE_NBT_TAG_COMPOUND = lookup.findVirtual(nmsItemStackClass, "w", MethodType.methodType(nbtTagCompoundClass));
			SET_NBT_TAG_COMPOUND = lookup.findVirtual(nmsItemStackClass, "c", MethodType.methodType(void.class, nbtTagCompoundClass));

			SET_INT = findSetter(int.class, lookup);
			SET_BYTE = findSetter(byte.class, lookup);
			SET_LONG = findSetter(long.class, lookup);
			SET_UUID = findSetter(UUID.class, lookup);
			SET_FLOAT = findSetter(float.class, lookup);
			SET_INT_ARRAY = findSetter(int[].class, lookup);
			SET_SHORT = findSetter(short.class, lookup);
			SET_BYTE_ARRAY = findSetter(byte[].class, lookup);
			SET_DOUBLE = findSetter(double.class, lookup);
			SET_LONG_ARRAY = findSetter(long[].class, lookup);
			SET_STRING = findSetter(String.class, lookup);
			SET_BOOLEAN = findSetter(boolean.class, lookup);

			SET_BYTE_LIST = findSetter(List.class, "a", lookup);
			SET_INT_LIST = findSetter(List.class, "b", lookup);
			SET_LONG_LIST = findSetter(List.class, "c", lookup);


			GET_BOOLEAN = findGetter(boolean.class, "q", lookup);
			GET_BYTE = findGetter(byte.class, "f", lookup);
			GET_BYTE_ARRAY = findGetter(byte[].class, "m", lookup);
			GET_DOUBLE = findGetter(double.class, "k", lookup);
			GET_FLOAT = findGetter(float.class, "j", lookup);
			GET_INT = findGetter(int.class, "h", lookup);
			GET_INT_ARRAY = findGetter(int[].class, "n", lookup);
			GET_LONG = findGetter(long.class, "i", lookup);
			GET_LONG_ARRAY = findGetter(long[].class, "o", lookup);
			GET_SHORT = findGetter(short.class, "g", lookup);
			GET_STRING = findGetter(String.class, "l", lookup);
			GET_UUID = findGetter(UUID.class, "a", lookup);


			KEY_SET = findMethod("e", Set.class, lookup);
			SIZE = findMethod("f", int.class, lookup);
			CONTAINS_KEY = findMethod("e", boolean.class, String.class, lookup);
			IS_EMPTY = findMethod("g", boolean.class, lookup);
			REMOVE = findMethod("r", void.class, String.class, lookup);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	@SneakyThrows
	private static Object invoke(MethodHandle handle, Object obj, Object... parameters) {
		return handle.invoke(obj, parameters);
	}

	private final Object nbt;

	protected DefaultNBT(Object nbt) {
		this.nbt = nbt;
	}

	protected abstract void onChange(Object nbt);

	@Override
	public int hashCode() {
		return nbt.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DefaultNBT dnbt) return nbt.equals(dnbt.nbt);
		return false;
	}

	@Override
	public String toString() {
		return nbt.toString();
	}

	@Override
	public void set(String key, int value) {
		invoke(SET_INT, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, byte value) {
		invoke(SET_BYTE, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, long value) {
		invoke(SET_LONG, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, UUID value) {
		invoke(SET_UUID, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, float value) {
		invoke(SET_FLOAT, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, int[] value) {
		invoke(SET_INT_ARRAY, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, short value) {
		invoke(SET_SHORT, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, byte[] value) {
		invoke(SET_BYTE_ARRAY, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, double value) {
		invoke(SET_DOUBLE, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, long[] value) {
		invoke(SET_LONG_ARRAY, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, String value) {
		invoke(SET_STRING, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, boolean value) {
		invoke(SET_BOOLEAN, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void setByteList(String key, List<Byte> value) {
		invoke(SET_BYTE_LIST, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void setIntList(String key, List<Integer> value) {
		invoke(SET_INT_LIST, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public void setLongList(String key, List<Long> value) {
		invoke(SET_LONG_LIST, nbt, key, value);
		onChange(nbt);
	}

	@Override
	public boolean getBoolean(String key) {
		return (boolean) invoke(GET_BOOLEAN, nbt, key);
	}

	@Override
	public byte getByte(String key) {
		return (byte) invoke(GET_BYTE, nbt, key);
	}

	@Override
	public byte[] getByteArray(String key) {
		return (byte[]) invoke(GET_BYTE_ARRAY, nbt, key);
	}

	@Override
	public double getDouble(String key) {
		return (double) invoke(GET_DOUBLE, nbt, key);
	}

	@Override
	public float getFloat(String key) {
		return (float) invoke(GET_FLOAT, nbt, key);
	}

	@Override
	public int getInt(String key) {
		return (int) invoke(GET_INT, nbt, key);
	}

	@Override
	public int[] getIntArray(String key) {
		return (int[]) invoke(GET_INT_ARRAY, nbt, key);
	}

	@Override
	public long getLong(String key) {
		return (long) invoke(GET_LONG, nbt, key);
	}

	@Override
	public long[] getLongArray(String key) {
		return (long[]) invoke(GET_LONG_ARRAY, nbt, key);
	}

	@Override
	public short getShort(String key) {
		return (short) invoke(GET_SHORT, nbt, key);
	}

	@Override
	public String getString(String key) {
		return (String) invoke(GET_STRING, nbt, key);
	}

	@Override
	public UUID getUUID(String key) {
		return (UUID) invoke(GET_UUID, nbt, key);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<String> keySet() {
		return (Set<String>) invoke(KEY_SET, nbt);
	}

	@Override
	public int size() {
		return (int) invoke(SIZE, nbt);
	}

	@Override
	public boolean containsKey(String key) {
		return (boolean) invoke(CONTAINS_KEY, nbt, key);
	}

	@Override
	public boolean isEmpty() {
		return (boolean) invoke(IS_EMPTY, nbt);
	}

	@Override
	public void remove(String key) {
		invoke(REMOVE, nbt, key);
		onChange(nbt);
	}
}
