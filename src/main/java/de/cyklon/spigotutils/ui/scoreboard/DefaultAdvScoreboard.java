package de.cyklon.spigotutils.ui.scoreboard;

import de.cyklon.spigotutils.version.Version;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

abstract class DefaultAdvScoreboard extends DefaultScoreboard<Component> {

	private static final MethodHandle COMPONENT_METHOD;
	private static final Object EMPTY_COMPONENT;
	private static final boolean ADVENTURE_SUPPORT;

	static {
		ADVENTURE_SUPPORT = Version.isPaper();
		MethodHandles.Lookup lookup = MethodHandles.lookup();

		try {
			if (ADVENTURE_SUPPORT) {
				Class<?> paperAdventure = Class.forName("io.papermc.paper.adventure.PaperAdventure");
				Method method = paperAdventure.getDeclaredMethod("asVanilla", net.kyori.adventure.text.Component.class);
				COMPONENT_METHOD = lookup.unreflect(method);
				EMPTY_COMPONENT = COMPONENT_METHOD.invoke(net.kyori.adventure.text.Component.empty());
			} else {
				Class<?> craftChatMessageClass = obcClass("util.CraftChatMessage");
				COMPONENT_METHOD = lookup.unreflect(craftChatMessageClass.getMethod("fromString", String.class));
				EMPTY_COMPONENT = Array.get(COMPONENT_METHOD.invoke(""), 0);
			}
		} catch (Throwable t) {
			throw new ExceptionInInitializerError(t);
		}
	}

	DefaultAdvScoreboard(Player... players) {
		super(players);
	}

	@Override
	protected void sendLineChange(int score, int index) throws Throwable {
		Component line = getLine(score);
		sendPacket(teamPacket(score, TeamMode.UPDATE, index, line, null));

	}

	@Override
	protected Object toMinecraftComponent(Component line) throws Throwable {
		if (line==null) return EMPTY_COMPONENT;
		if (!ADVENTURE_SUPPORT) {
			// If the server isn't running adventure natively, we convert the component to legacy text
			// and then to a Minecraft chat component
			String legacy = serializeLine(line);
			return Array.get(COMPONENT_METHOD.invoke(legacy), 0);
		}
		return COMPONENT_METHOD.invoke(line);
	}

	@Override
	protected String serializeLine(Component line) {
		return LegacyComponentSerializer.legacySection().serialize(line);
	}

	@Override
	protected Component emptyLine() {
		return Component.empty();
	}

}
