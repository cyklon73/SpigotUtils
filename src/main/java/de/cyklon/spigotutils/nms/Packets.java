package de.cyklon.spigotutils.nms;

import java.lang.invoke.MethodHandles;

import de.cyklon.spigotutils.nms.Classes.net.minecraft.network.protocol.game;
import static de.cyklon.spigotutils.nms.NMSReflection.*;

public final class Packets {

	public static final PacketConstructor PacketPlayOutScoreboardObjective;
	public static final PacketConstructor PacketPlayOutScoreboardDisplayObjective;
	public static final PacketConstructor PacketPlayOutScoreboardScore;
	public static final PacketConstructor PacketPlayOutScoreboardTeam;

	static {
		try {
			MethodHandles.Lookup lookup = MethodHandles.lookup();
			PacketPlayOutScoreboardObjective = findPacketConstructor(game.PacketPlayOutScoreboardObjective, lookup);
			PacketPlayOutScoreboardDisplayObjective = findPacketConstructor(game.PacketPlayOutScoreboardDisplayObjective, lookup);
			PacketPlayOutScoreboardScore = findPacketConstructor(game.PacketPlayOutScoreboardScore, lookup);
			PacketPlayOutScoreboardTeam = findPacketConstructor(game.PacketPlayOutScoreboardTeam, lookup);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

}
