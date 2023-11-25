package de.cyklon.spigotutils.nms;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static de.cyklon.spigotutils.nms.Classes.net.minecraft.nbt.NBTBase;
import static de.cyklon.spigotutils.nms.Classes.net.minecraft.nbt.NBTTagCompound;
import static de.cyklon.spigotutils.nms.Classes.net.minecraft.world.item;
import static de.cyklon.spigotutils.nms.Classes.org.bukkit.craftbukkit.inventory.CraftItemStack;

public final class Methods {


	private Methods() {}

	public static final StaticMethod<?> CraftItemStack_AS_NMS_COPY = getStaticMethod(CraftItemStack, "asNMSCopy", item.ItemStack, ItemStack.class);
	public static final StaticMethod<?> CraftItemStack_AS_CRAFT_MIRROR = getStaticMethod(CraftItemStack, "asCraftMirror", CraftItemStack, item.ItemStack);
	public static final StaticMethod<ItemStack> CraftItemStack_AS_BUKKIT_MIRROR = getStaticMethod(CraftItemStack, "asCraftMirror", ItemStack.class, item.ItemStack);



	public static final Method<?> nmsItemStack_GET_OR_CREATE_NBT_TAG_COMPOUND = getMethod(item.ItemStack, "w", NBTTagCompound);
	public static final Method<?> nmsItemStack_SET_NBT_TAG_COMPOUND = getMethod(item.ItemStack, "c", void.class, NBTTagCompound);


	public static final Method<Void> NBTTagCompound_SET_INT = getMethod(NBTTagCompound, "a", void.class, int.class);
	public static final Method<Void> NBTTagCompound_SET_BYTE = getMethod(NBTTagCompound, "a", void.class, byte.class);
	public static final Method<Void> NBTTagCompound_SET_LONG = getMethod(NBTTagCompound, "a", void.class, long.class);
	public static final Method<Void> NBTTagCompound_SET_UUID = getMethod(NBTTagCompound, "a", void.class, UUID.class);
	public static final Method<Void> NBTTagCompound_SET_FLOAT = getMethod(NBTTagCompound, "a", void.class, float.class);
	public static final Method<Void> NBTTagCompound_SET_INT_ARRAY = getMethod(NBTTagCompound, "a", void.class, int[].class);
	public static final Method<Void> NBTTagCompound_SET_SHORT = getMethod(NBTTagCompound, "a", void.class, short.class);
	public static final Method<Void> NBTTagCompound_SET_BYTE_ARRAY = getMethod(NBTTagCompound, "a", void.class, byte[].class);
	public static final Method<Void> NBTTagCompound_SET_DOUBLE = getMethod(NBTTagCompound, "a", void.class, double.class);
	public static final Method<Void> NBTTagCompound_SET_LONG_ARRAY = getMethod(NBTTagCompound, "a", void.class, long[].class);
	public static final Method<Void> NBTTagCompound_SET_STRING = getMethod(NBTTagCompound, "a", void.class, String.class);
	public static final Method<Void> NBTTagCompound_SET_BOOLEAN = getMethod(NBTTagCompound, "a", void.class, boolean.class);
	public static final Method<Void> NBTTagCompound_SET_NBT = getMethod(NBTTagCompound, "a", void.class, NBTBase);

	public static final Method<Void> NBTTagCompound_SET_BYTE_LIST = getMethod(NBTTagCompound, "a", void.class, List.class);
	public static final Method<Void> NBTTagCompound_SET_INT_LIST = getMethod(NBTTagCompound, "b", void.class, List.class);
	public static final Method<Void> NBTTagCompound_SET_LONG_LIST = getMethod(NBTTagCompound, "c", void.class, List.class);


	public static final Method<Boolean> NBTTagCompound_GET_BOOLEAN = getMethod(NBTTagCompound, "q", boolean.class);
	public static final Method<Byte> NBTTagCompound_GET_BYTE = getMethod(NBTTagCompound, "f", byte.class);
	public static final Method<byte[]> NBTTagCompound_GET_BYTE_ARRAY = getMethod(NBTTagCompound, "m", byte[].class);
	public static final Method<Double> NBTTagCompound_GET_DOUBLE = getMethod(NBTTagCompound, "k", double.class);
	public static final Method<Float> NBTTagCompound_GET_FLOAT = getMethod(NBTTagCompound, "j", float.class);
	public static final Method<Integer> NBTTagCompound_GET_INT = getMethod(NBTTagCompound, "h", int.class);
	public static final Method<int[]> NBTTagCompound_GET_INT_ARRAY = getMethod(NBTTagCompound, "n", int[].class);
	public static final Method<Long> NBTTagCompound_GET_LONG = getMethod(NBTTagCompound, "i", long.class);
	public static final Method<long[]> NBTTagCompound_GET_LONG_ARRAY = getMethod(NBTTagCompound, "o", long[].class);
	public static final Method<Short> NBTTagCompound_GET_SHORT = getMethod(NBTTagCompound, "g", short.class);
	public static final Method<String> NBTTagCompound_GET_STRING = getMethod(NBTTagCompound, "l", String.class);
	public static final Method<UUID> NBTTagCompound_GET_UUID = getMethod(NBTTagCompound, "a", UUID.class);
	public static final Method<?> NBTTagCompound_GET_NBT_TAG_COMPOUND = getMethod(NBTTagCompound, "p", NBTTagCompound);


	public static final Method<Set> NBTTagCompound_KEY_SET = getMethod(NBTTagCompound, "e", Set.class);
	public static final Method<Integer> NBTTagCompound_SIZE = getMethod(NBTTagCompound, "f", int.class);
	public static final Method<Boolean> NBTTagCompound_CONTAINS_KEY = getMethod(NBTTagCompound, "e", boolean.class, String.class);
	public static final Method<Boolean> NBTTagCompound_IS_EMPTY = getMethod(NBTTagCompound, "g", boolean.class);
	public static final Method<Void> NBTTagCompound_REMOVE = getMethod(NBTTagCompound, "r", void.class, String.class);



	private static <R> Method<R> getMethod(Class<?> declaringClass, String methodName, Class<R> returnType, Class<?>... paramTypes) {
		final ReflectMethod<?, R> method = NMSReflection.getMethod(declaringClass, methodName, returnType, paramTypes);
		return (obj, params) -> NMSReflection.invokeMethodWithInconstantClass(method, obj, params);
	}

	private static <R> StaticMethod<R> getStaticMethod(Class<?> declaringClass, String methodName, Class<R> returnType, Class<?>... paramTypes) {
		final ReflectMethod<?, R> method = NMSReflection.getMethod(declaringClass, methodName, returnType, paramTypes);
		return (params) -> NMSReflection.invokeStaticMethod(method, params);
	}

	public static interface Method<R> {
		R invoke(Object obj, Object... params);
	}

	public static interface StaticMethod<R> {
		R invoke(Object... params);
	}

}
