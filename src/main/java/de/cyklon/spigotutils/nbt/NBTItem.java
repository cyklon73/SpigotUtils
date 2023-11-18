package de.cyklon.spigotutils.nbt;

import org.bukkit.inventory.ItemStack;

public class NBTItem extends DefaultNBT {

	private final Object nmsStack;

	public NBTItem(ItemStack stack) {
		this(invokeStatic(AS_NMS_COPY, stack));
	}

	private NBTItem(Object stack) {
		super(invokeStatic(GET_OR_CREATE_NBT_TAG_COMPOUND, stack));
		this.nmsStack = stack;
	}

	@Override
	protected void onChange(Object nbt) {
		invoke(SET_NBT_TAG_COMPOUND, nmsStack, nbt);
	}

	public ItemStack getItem() {
		return (ItemStack) invoke(AS_CRAFT_MIRROR, nmsStack);
	}
}
