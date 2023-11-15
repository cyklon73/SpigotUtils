package de.cyklon.spigotutils.nbt;

import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Set;
import java.util.UUID;

abstract class DefaultNBT implements NBTHolder {

	private final NBTTagCompound nbt;

	protected DefaultNBT(NBTTagCompound nbt) {
		this.nbt = nbt;
	}

	protected abstract void onChange(NBTTagCompound nbt);

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
		nbt.a(key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, byte value) {
		nbt.a(key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, long value) {
		nbt.a(key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, UUID value) {
		nbt.a(key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, float value) {
		nbt.a(key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, int[] value) {
		nbt.a(key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, short value) {
		nbt.a(key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, byte[] value) {
		nbt.a(key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, double value) {
		nbt.a(key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, long[] value) {
		nbt.a(key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, String value) {
		nbt.a(key, value);
		onChange(nbt);
	}

	@Override
	public void set(String key, boolean value) {
		nbt.a(key, value);
		onChange(nbt);
	}

	@Override
	public void setByteList(String key, List<Byte> value) {
		nbt.a(key, value);
		onChange(nbt);
	}

	@Override
	public void setIntList(String key, List<Integer> value) {
		nbt.b(key, value);
		onChange(nbt);
	}

	@Override
	public void setLongList(String key, List<Long> value) {
		nbt.c(key, value);
		onChange(nbt);
	}

	@Override
	public boolean getBoolean(String key) {
		return nbt.q(key);
	}

	@Override
	public byte getByte(String key) {
		return nbt.f(key);
	}

	@Override
	public byte[] getByteArray(String key) {
		return nbt.m(key);
	}

	@Override
	public double getDouble(String key) {
		return nbt.k(key);
	}

	@Override
	public float getFloat(String key) {
		return nbt.j(key);
	}

	@Override
	public int getInt(String key) {
		return nbt.h(key);
	}

	@Override
	public int[] getIntArray(String key) {
		return nbt.n(key);
	}

	@Override
	public long getLong(String key) {
		return nbt.i(key);
	}

	@Override
	public long[] getLongArray(String key) {
		return nbt.o(key);
	}

	@Override
	public short getShort(String key) {
		return nbt.g(key);
	}

	@Override
	public String getString(String key) {
		return nbt.l(key);
	}

	@Override
	public UUID getUUID(String key) {
		return nbt.a(key);
	}

	@Override
	public Set<String> keySet() {
		return nbt.e();
	}

	@Override
	public int size() {
		return nbt.f();
	}

	@Override
	public boolean containsKey(String key) {
		return nbt.e(key);
	}

	@Override
	public boolean isEmpty() {
		return nbt.g();
	}

	@Override
	public void remove(String key) {
		nbt.r(key);
		onChange(nbt);
	}
}
