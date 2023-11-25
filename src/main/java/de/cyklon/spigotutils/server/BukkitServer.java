package de.cyklon.spigotutils.server;

import de.cyklon.spigotutils.nms.NMSReflection;
import de.cyklon.spigotutils.nms.ReflectField;

import static de.cyklon.spigotutils.nms.Classes.net.minecraft.server.MinecraftServer;

public final class BukkitServer {

	private static final ReflectField<?, Integer> CURRENT_TICK = NMSReflection.getField(MinecraftServer, "currentTick", Integer.class);
	private static final ReflectField<?, Long> CURRENT_TICK_LONG = NMSReflection.getField(MinecraftServer, "currentTickLong", Long.class);
	private static final ReflectField<?, double[]> RECENT_TPS = NMSReflection.getField(MinecraftServer, "recentTps", double[].class);

	public static ServerConfiguration getConfig() {
		return Config.CONFIG;
	}

	public static int getCurrentTick() {
		return NMSReflection.getStaticValue(CURRENT_TICK);
	}

	public static long getCurrentTickLong() {
		return NMSReflection.getStaticValue(CURRENT_TICK_LONG);
	}

	public static TPS getTPS() {
		return tps(NMSReflection.getStaticValue(RECENT_TPS));
	}


	private static TPS tps(double[] tps) {
		return new TPS() {
			@Override
			public double getTPS_1s() {
				return tps[0];
			}

			@Override
			public double getTPS_5s() {
				return tps[1];
			}

			@Override
			public double getTPS_15s() {
				return tps[2];
			}
		};
	}

}
