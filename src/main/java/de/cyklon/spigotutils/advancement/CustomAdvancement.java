package de.cyklon.spigotutils.advancement;

import org.bukkit.NamespacedKey;

public final class CustomAdvancement implements AdvancementType {

	private final NamespacedKey key;

	CustomAdvancement(String namespace, String key) {
		this(new NamespacedKey(namespace, key));
	}

	public CustomAdvancement(NamespacedKey key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key.getKey();
	}

	@Override
	public String getNamespace() {
		return key.getNamespace();
	}

	@Override
	public Category getCategory() {
		return Category.CUSTOM;
	}

	@Override
	public NamespacedKey getNamespacedKey() {
		return key;
	}
}
