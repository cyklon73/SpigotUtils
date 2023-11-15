package de.cyklon.spigotutils.nbt;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface NBTHolder {

	void set(String key, int value);
	void set(String key, byte value);
	void set(String key, long value);
	void set(String key, UUID value);
	void set(String key, float value);
	void set(String key, int[] value);
	void set(String key, short value);
	void set(String key, byte[] value);
	void set(String key, double value);
	void set(String key, long[] value);
	void set(String key,  String value);
	void set(String key,  boolean value);
	void setByteList(String key,  List<Byte> value);
	void setIntList(String key, List<Integer> value);
	void setLongList(String key, List<Long> value);

	byte getByte(String key);
	short getShort(String key);
	int getInt(String key);
	long getLong(String key);
	float getFloat(String key);
	double getDouble(String key);
	String getString(String key);
	byte[] getByteArray(String key);
	int[] getIntArray(String key);
	long[] getLongArray(String key);
	boolean getBoolean(String key);
	UUID getUUID(String key);

	Set<String> keySet();
	int size();
	boolean containsKey(String key);
	boolean isEmpty();
	void remove(String key);


}
