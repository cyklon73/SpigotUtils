package de.cyklon.spigotutils.nms;

@FunctionalInterface
public interface PacketConstructor {
	Object invoke() throws Throwable;
}
