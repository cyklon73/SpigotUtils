package de.cyklon.spigotutils.persistence;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Set;

class HandlePersistentDataContainer implements PersistentDataContainer {

	private final PersistentDataContainer container;
	private final Runnable handler;

	HandlePersistentDataContainer(@NotNull PersistentDataContainer container, @NotNull Runnable handler) {
		this.container = container;
		this.handler = handler;
	}

	@Override
	public <T, Z> void set(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z value) {
		container.set(key, type, value);
		handler.run();
	}

	@Override
	public <T, Z> boolean has(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type) {
		return container.has(key, type);
	}

	@Override
	public <T, Z> @Nullable Z get(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type) {
		return container.get(key, type);
	}

	@Override
	public <T, Z> @NotNull Z getOrDefault(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z defaultValue) {
		return container.getOrDefault(key, type, defaultValue);
	}

	@Override
	public @NotNull Set<NamespacedKey> getKeys() {
		return container.getKeys();
	}

	@Override
	public void remove(@NotNull NamespacedKey key) {
		container.remove(key);
		handler.run();
	}

	@Override
	public boolean isEmpty() {
		return container.isEmpty();
	}

	@Override
	public @NotNull PersistentDataAdapterContext getAdapterContext() {
		return container.getAdapterContext();
	}

	@Override
	public boolean has(@NotNull NamespacedKey key) {
		return container.has(key);
	}

	@Override
	public byte @NotNull [] serializeToBytes() throws IOException {
		return container.serializeToBytes();
	}

	@Override
	public void readFromBytes(byte @NotNull [] bytes, boolean clear) throws IOException {
		container.readFromBytes(bytes, clear);
	}

	@Override
	public void readFromBytes(byte @NotNull [] bytes) throws IOException {
		container.readFromBytes(bytes);
	}
}
