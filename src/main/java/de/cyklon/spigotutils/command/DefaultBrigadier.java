package de.cyklon.spigotutils.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
abstract class DefaultBrigadier {

	protected static final Field CUSTOM_SUGGESTIONS_FIELD;

	protected static final Field COMMAND_EXECUTE_FUNCTION_FIELD;

	protected static final Field CHILDREN_FIELD;
	protected static final Field LITERALS_FIELD;
	protected static final Field ARGUMENTS_FIELD;

	protected static final Field[] CHILDREN_FIELDS;

	protected static final com.mojang.brigadier.Command<?> DUMMY_COMMAND;
	protected static final SuggestionProvider<?> DUMMY_SUGGESTION_PROVIDER;

	static {
		try {
			CUSTOM_SUGGESTIONS_FIELD = ArgumentCommandNode.class.getDeclaredField("customSuggestions");
			CUSTOM_SUGGESTIONS_FIELD.setAccessible(true);

			COMMAND_EXECUTE_FUNCTION_FIELD = CommandNode.class.getDeclaredField("command");
			COMMAND_EXECUTE_FUNCTION_FIELD.setAccessible(true);

			CHILDREN_FIELD = CommandNode.class.getDeclaredField("children");
			LITERALS_FIELD = CommandNode.class.getDeclaredField("literals");
			ARGUMENTS_FIELD = CommandNode.class.getDeclaredField("arguments");
			CHILDREN_FIELDS = new Field[]{CHILDREN_FIELD, LITERALS_FIELD, ARGUMENTS_FIELD};
			for (Field field : CHILDREN_FIELDS) {
				field.setAccessible(true);
			}

			// should never be called
			// if ReflectionBrigadier: bukkit handling should override
			// if PaperBrigadier: this is only sent to the client, not used for actual command handling
			DUMMY_COMMAND = (ctx) -> { throw new UnsupportedOperationException(); };
			// should never be called - only used in clientbound root node, and the server impl will pass anything through
			// SuggestionProviders#safelySwap (swap it for the ASK_SERVER provider) before sending
			DUMMY_SUGGESTION_PROVIDER = (context, builder) -> { throw new UnsupportedOperationException(); };

		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	protected final Plugin plugin;

	protected DefaultBrigadier(Plugin plugin) {
		this.plugin = plugin;
	}

	abstract void register(Command command, LiteralCommandNode<?> node, Predicate<? super Player> permissionTest);

	void register(Command command, LiteralArgumentBuilder<?> argumentBuilder, Predicate<? super Player> permissionTest) {
		Objects.requireNonNull(command, "command");
		Objects.requireNonNull(argumentBuilder, "argumentBuilder");
		Objects.requireNonNull(permissionTest, "permissionTest");
		register(command, argumentBuilder.build(), permissionTest);
	}

	void register(Command command, LiteralCommandNode<?> node) {
		Objects.requireNonNull(command, "command");
		Objects.requireNonNull(node, "node");
		register(command, node, command::testPermissionSilent);
	}

	void register(Command command, LiteralArgumentBuilder<?> argumentBuilder) {
		Objects.requireNonNull(command, "command");
		Objects.requireNonNull(argumentBuilder, "argumentBuilder");
		register(command, argumentBuilder.build());
	}

	abstract void register(LiteralCommandNode<?> node);

	void register(LiteralArgumentBuilder<?> argumentBuilder) {
		Objects.requireNonNull(argumentBuilder, "argumentBuilder");
		register(argumentBuilder.build());
	}

	public static DefaultBrigadier getBrigadier(Plugin plugin) {
		try {
			Class.forName("com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent");
			return new PaperBrigadier(plugin);
		} catch (ClassNotFoundException ignored) {}
		return new ReflectionBrigadier(plugin);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	protected static void removeChild(RootCommandNode root, String name) {
		try {
			for (Field field : CHILDREN_FIELDS) {
				Map<String, ?> children = (Map<String, ?>) field.get(root);
				children.remove(name);
			}
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	protected static void setRequiredHackyFieldsRecursively(CommandNode<?> node, SuggestionProvider<?> suggestionProvider) {
		// set command execution function so the server sets the executable flag on the command
		try {
			COMMAND_EXECUTE_FUNCTION_FIELD.set(node, DUMMY_COMMAND);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		if (suggestionProvider != null && node instanceof ArgumentCommandNode<?, ?> argumentNode) {

			// set the custom suggestion provider field so tab completions work
			try {
				CUSTOM_SUGGESTIONS_FIELD.set(argumentNode, suggestionProvider);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		for (CommandNode<?> child : node.getChildren()) {
			setRequiredHackyFieldsRecursively(child, suggestionProvider);
		}
	}

	protected static <S> LiteralCommandNode<S> renameLiteralNode(LiteralCommandNode<S> node, String newLiteral) {
		LiteralCommandNode<S> clone = new LiteralCommandNode<>(newLiteral, node.getCommand(), node.getRequirement(), node.getRedirect(), node.getRedirectModifier(), node.isFork());
		for (CommandNode<S> child : node.getChildren()) {
			clone.addChild(child);
		}
		return clone;
	}

	/**
	 * Gets the aliases known for the given command.
	 *
	 * <p>This will include the main label, as well as defined aliases, and
	 * aliases including the fallback prefix added by Bukkit.</p>
	 *
	 * @param command the command
	 * @return the aliases
	 */
	protected static Collection<String> getAliases(Command command) {
		Objects.requireNonNull(command, "command");

		Stream<String> aliasesStream = Stream.concat(
				Stream.of(command.getLabel()),
				command.getAliases().stream()
		);

		if (command instanceof PluginCommand) {
			String fallbackPrefix = ((PluginCommand) command).getPlugin().getName().toLowerCase().trim();
			aliasesStream = aliasesStream.flatMap(alias -> Stream.of(
					alias,
					fallbackPrefix + ":" + alias
			));
		}

		return aliasesStream.distinct().collect(Collectors.toList());
	}


}
