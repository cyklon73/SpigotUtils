package de.cyklon.spigotutils.serial;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class InventorySerializer {

	private InventorySerializer() {}

	public static void saveInv(File file, Inventory inv) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file); BukkitObjectOutputStream data = new BukkitObjectOutputStream(fos)) {
			data.writeInt(inv.getSize());
			for (int i = 0; i < inv.getSize(); i++) {
				data.writeObject(inv.getItem(i));
			}

		}
	}

	public static <T extends Inventory> T loadInv(File file, T inv) throws IOException, ClassNotFoundException {
		try (FileInputStream fis = new FileInputStream(file); BukkitObjectInputStream data = new BukkitObjectInputStream(fis)) {
			int size = data.readInt();

			for (int i = 0; i < size; i++) {
				inv.setItem(i, (ItemStack) data.readObject());
			}
		}
		return inv;
	}

}
