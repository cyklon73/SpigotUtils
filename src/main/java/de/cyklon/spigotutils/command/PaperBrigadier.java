package de.cyklon.spigotutils.command;

import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
final class PaperBrigadier extends DefaultBrigadier implements Listener {

	private final List<BrigadierCommand> commands = new ArrayList<>();

	PaperBrigadier(Plugin plugin) {
		super(plugin);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public void register(LiteralCommandNode<?> node) {
		Objects.requireNonNull(node, "node");
		this.commands.add(new BrigadierCommand(node, null));
	}

	@Override
	@SuppressWarnings("unchecked")
	public void register(Command command, LiteralCommandNode<?> node, Predicate<? super Player> permissionTest) {
		Objects.requireNonNull(command, "command");
		Objects.requireNonNull(node, "node");
		Objects.requireNonNull(permissionTest, "permissionTest");

		try {
			setRequiredHackyFieldsRecursively(node, DUMMY_SUGGESTION_PROVIDER);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		Collection<String> aliases = getAliases(command);
		if (!aliases.contains(node.getLiteral())) {
			node = renameLiteralNode(node, command.getName());
		}

		for (String alias : aliases) {
			if (node.getLiteral().equals(alias)) {
				this.commands.add(new BrigadierCommand(node, permissionTest));
			} else {
				LiteralCommandNode<Object> redirectNode = LiteralArgumentBuilder.literal(alias)
						.redirect((LiteralCommandNode<Object>) node)
						.build();
				this.commands.add(new BrigadierCommand(redirectNode, permissionTest));
			}
		}
	}

	@EventHandler
	@SuppressWarnings("UnstableApiUsage")
	public void onPlayerSendCommandsEvent(AsyncPlayerSendCommandsEvent<?> event) {
		if (event.isAsynchronous() || !event.hasFiredAsync()) {
			for (BrigadierCommand command : this.commands) {
				command.apply(event.getPlayer(), event.getCommandNode());
			}
		}
	}

	private record BrigadierCommand(LiteralCommandNode<?> node, Predicate<? super Player> permissionTest) {

		@SuppressWarnings({"unchecked", "rawtypes"})
			public void apply(Player player, RootCommandNode<?> root) {
				if (this.permissionTest != null && !this.permissionTest.test(player)) {
					return;
				}
				removeChild(root, this.node.getName());
				root.addChild((CommandNode) this.node);
			}
		}
}
