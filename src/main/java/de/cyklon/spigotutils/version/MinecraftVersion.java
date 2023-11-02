package de.cyklon.spigotutils.version;

import org.bukkit.Server;

public enum MinecraftVersion {

	v1_20_2,
	v1_20_1,
	v1_20,
	v1_19_4,
	v1_19_3,
	v1_19_2,
	v1_19_1,
	v1_19,
	v1_18_2,
	v1_18_1,
	v1_18,
	v1_17_1,
	v1_17,
	v1_16_5,
	v1_16_4,
	v1_16_3,
	v1_16_2,
	v1_16_1,
	v1_16,
	v1_15_2,
	v1_15_1,
	v1_15,
	v1_14_4,
	v1_14_3,
	v1_14_2,
	v1_14_1,
	v1_14,
	v1_13_2,
	v1_13_1,
	v1_13,
	v1_12_2,
	v1_12_1,
	v1_12,
	v1_11_2,
	v1_11_1,
	v1_11,
	v1_10_2,
	v1_10_1,
	v1_10,
	v1_9_4,
	v1_9_3,
	v1_9_2,
	v1_9_1,
	v1_9,
	v1_8_9,
	v1_8_8,
	v1_8_7,
	v1_8_6,
	v1_8_5,
	v1_8_4,
	v1_8_3,
	v1_8_2,
	v1_8_1,
	v1_8,
	v1_7_10,
	v1_7_9,
	v1_7_8,
	v1_7_7,
	v1_7_6,
	v1_7_5,
	v1_7_4,
	v1_7_2,
	v1_6_4,
	v1_6_2,
	v1_6_1,
	v1_5_2,
	v1_5_1,
	v1_5,
	v1_4_7,
	v1_4_6,
	v1_4_5,
	v1_4_4,
	v1_4_2,
	v1_3_2,
	v1_3_1,
	v1_2_5,
	v1_2_4,
	v1_2_3,
	v1_2_2,
	v1_2_1,
	v1_1,
	v1_0_1,
	v1_0_0;

	public boolean isHigherOrEqual(Server server) {
		return isHigherOrEqual(Version.getVersion(server));
	}

	public boolean isLowerOrEqual(Server server) {
		return isLowerOrEqual(Version.getVersion(server));
	}

	public boolean isHigher(Server server) {
		return isHigher(Version.getVersion(server));
	}

	public boolean isLower(Server server) {
		return isLower(Version.getVersion(server));
	}

	public boolean isHigherOrEqual(MinecraftVersion version) {
		return version.ordinal() <= ordinal();
	}

	public boolean isLowerOrEqual(MinecraftVersion version) {
		return version.ordinal() >= ordinal();
	}

	public boolean isHigher(MinecraftVersion version) {
		return version.ordinal() < ordinal();
	}

	public boolean isLower(MinecraftVersion version) {
		return version.ordinal() > ordinal();
	}

}
