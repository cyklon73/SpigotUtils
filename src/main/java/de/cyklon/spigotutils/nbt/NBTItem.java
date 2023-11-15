package de.cyklon.spigotutils.nbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;

public class NBTItem extends DefaultNBT {

	private final ItemStack nmsStack;

	public NBTItem(org.bukkit.inventory.ItemStack stack) {
		this(CraftItemStack.asNMSCopy(stack));
	}

	private NBTItem(ItemStack stack) {
		super(stack.w());
		this.nmsStack = stack;
	}

	@Override
	protected void onChange(NBTTagCompound nbt) {
		this.nmsStack.c(nbt);
	}

	public org.bukkit.inventory.ItemStack getItem() {
		return nmsStack.asBukkitMirror();
	}
}
