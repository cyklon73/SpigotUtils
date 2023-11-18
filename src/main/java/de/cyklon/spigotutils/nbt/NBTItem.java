package de.cyklon.spigotutils.nbt;

import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;

public class NBTItem extends DefaultNBT {

	private final Object nmsStack;

	@SneakyThrows
	public NBTItem(ItemStack stack) {
		this(AS_NMS_COPY.invoke(stack));
	}

	private NBTItem(Object stack) throws Throwable {
		super(GET_OR_CREATE_NBT_TAG_COMPOUND.invoke(stack));
		this.nmsStack = stack;
	}

	@SneakyThrows
	@Override
	protected void onChange(Object nbt) {
		SET_NBT_TAG_COMPOUND.invoke(nmsStack, nbt);
	}

	@SneakyThrows
	public ItemStack getItem() {
		return (ItemStack) AS_CRAFT_MIRROR.invoke(nmsStack);
	}
}
