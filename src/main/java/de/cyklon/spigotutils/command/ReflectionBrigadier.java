package de.cyklon.spigotutils.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("SpellCheckingInspection")
final class ReflectionBrigadier extends DefaultBrigadier {

	private static final String NM_PACKAGE = "net.minecraft";
	private static final String OBC_PACKAGE = "org.bukkit.craftbukkit";
	private static final String NMS_PACKAGE = NM_PACKAGE + ".server";

	private static final String VERSION_NAME = Bukkit.getServer().getClass().getPackage().getName().substring(OBC_PACKAGE.length() + 1);

	private static final boolean NMS_REPACKAGED = checkRepackage().isPresent();

	private static final Field CONSOLE_FIELD;

	private static final Method GET_COMMAND_DISPATCHER_METHOD;

	private static final Method GET_BRIGADIER_DISPATCHER_METHOD;

	private static final Constructor<?> COMMAND_WRAPPER_CONSTRUCTOR;

	static {
		try {
			final Class<?> minecraftServer = nmsClass("server", "MinecraftServer");
			final Class<?>commandDispatcher = nmsClass("commands", "CommandDispatcher");

			Class<?> craftServer = obcClass("CraftServer");
			CONSOLE_FIELD = craftServer.getDeclaredField("console");
			CONSOLE_FIELD.setAccessible(true);

			GET_COMMAND_DISPATCHER_METHOD = Arrays.stream(minecraftServer.getDeclaredMethods())
					.filter(method -> method.getParameterCount() == 0)
					.filter(method -> commandDispatcher.isAssignableFrom(method.getReturnType()))
					.findFirst().orElseThrow(NoSuchMethodException::new);
			GET_COMMAND_DISPATCHER_METHOD.setAccessible(true);

			GET_BRIGADIER_DISPATCHER_METHOD = Arrays.stream(commandDispatcher.getDeclaredMethods())
					.filter(method -> method.getParameterCount() == 0)
					.filter(method -> CommandDispatcher.class.isAssignableFrom(method.getReturnType()))
					.findFirst().orElseThrow(NoSuchMethodException::new);
			GET_BRIGADIER_DISPATCHER_METHOD.setAccessible(true);

			Class<?> commandWrapperClass = obcClass("command.BukkitCommandWrapper");
			COMMAND_WRAPPER_CONSTRUCTOR = commandWrapperClass.getConstructor(craftServer, Command.class);

		} catch (ReflectiveOperationException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private final List<LiteralCommandNode<?>> registeredNodes = new ArrayList<>();

	ReflectionBrigadier(Plugin plugin) {
		super(plugin);
		Bukkit.getPluginManager().registerEvents(new ServerReloadListener(this), plugin);
	}

	private CommandDispatcher<?> getDispatcher() {
		try {
			Object mcServerObject = CONSOLE_FIELD.get(Bukkit.getServer());
			Object commandDispatcherObject = GET_COMMAND_DISPATCHER_METHOD.invoke(mcServerObject);
			return (CommandDispatcher<?>) GET_BRIGADIER_DISPATCHER_METHOD.invoke(commandDispatcherObject);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void register(LiteralCommandNode<?> node) {
		Objects.requireNonNull(node, "node");

		CommandDispatcher dispatcher = getDispatcher();
		RootCommandNode root = dispatcher.getRoot();

		removeChild(root, node.getName());
		root.addChild(node);
		this.registeredNodes.add(node);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void register(Command command, LiteralCommandNode<?> node, Predicate<? super Player> permissionTest) {
		Objects.requireNonNull(command, "command");
		Objects.requireNonNull(node, "node");
		Objects.requireNonNull(permissionTest, "permissionTest");

		try {
			SuggestionProvider<?> wrapper = (SuggestionProvider<?>) COMMAND_WRAPPER_CONSTRUCTOR.newInstance(this.plugin.getServer(), command);
			setRequiredHackyFieldsRecursively(node, wrapper);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		Collection<String> aliases = getAliases(command);
		if (!aliases.contains(node.getLiteral())) {
			node = renameLiteralNode(node, command.getName());
		}

		for (String alias : aliases) {
			if (node.getLiteral().equals(alias)) {
				register(node);
			} else {
				register(LiteralArgumentBuilder.literal(alias).redirect((LiteralCommandNode<Object>) node).build());
			}
		}

		this.plugin.getServer().getPluginManager().registerEvents(new CommandDataSendListener(command, permissionTest), this.plugin);
	}


	/**
	 * Listens for server (re)loads, and re-adds all registered nodes to the dispatcher.
	 */
		private record ServerReloadListener(ReflectionBrigadier brigadier) implements Listener {

		@SuppressWarnings({"rawtypes", "unchecked"})
			@EventHandler
			public void onLoad(ServerLoadEvent e) {
				CommandDispatcher dispatcher = this.brigadier.getDispatcher();
				RootCommandNode root = dispatcher.getRoot();

				for (LiteralCommandNode<?> node : this.brigadier.registeredNodes) {
					removeChild(root, node.getName());
					root.addChild(node);
				}
			}
		}

	/**
	 * Removes minecraft namespaced argument data, & data for players without permission to view the
	 * corresponding commands.
	 */
	private static final class CommandDataSendListener implements Listener {
		private final Set<String> aliases;
		private final Set<String> minecraftPrefixedAliases;
		private final Predicate<? super Player> permissionTest;

		CommandDataSendListener(Command pluginCommand, Predicate<? super Player> permissionTest) {
			this.aliases = new HashSet<>(getAliases(pluginCommand));
			this.minecraftPrefixedAliases = this.aliases.stream().map(alias -> "minecraft:" + alias).collect(Collectors.toSet());
			this.permissionTest = permissionTest;
		}

		@EventHandler
		public void onCommandSend(PlayerCommandSendEvent e) {
			// always remove 'minecraft:' prefixed aliases added by craftbukkit.
			// this happens because bukkit thinks our injected commands are vanilla commands.
			e.getCommands().removeAll(this.minecraftPrefixedAliases);

			// remove the actual aliases if the player doesn't pass the permission test
			if (!this.permissionTest.test(e.getPlayer())) {
				e.getCommands().removeAll(this.aliases);
			}
		}
	}


	private static String nmsClassName(String post1_17package, String className) {
		if (NMS_REPACKAGED) {
			String classPackage = post1_17package == null ? NM_PACKAGE : NM_PACKAGE + '.' + post1_17package;
			return classPackage + '.' + className;
		}
		return NMS_PACKAGE + '.' + VERSION_NAME + '.' + className;
	}

	private static Optional<Class<?>> checkRepackage() {
		try {
			return Optional.of(Class.forName("net.minecraft.network.protocol.Packet"));
		} catch (ClassNotFoundException e) {
			return Optional.empty();
		}
	}

	private static Class<?> nmsClass(String post1_17package, String className) throws ClassNotFoundException {
		return Class.forName(nmsClassName(post1_17package, className));
	}

	private static String obcClassName(String className) {
		return OBC_PACKAGE + '.' + VERSION_NAME + '.' + className;
	}

	private static Class<?> obcClass(String className) throws ClassNotFoundException {
		return Class.forName(obcClassName(className));
	}

}
