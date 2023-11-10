package de.cyklon.spigotutils.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.cyklon.spigotutils.command.annotation.option.Option;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

class SpigotCommand extends BukkitCommand {

    private final Plugin plugin;
    private final Method method;
    private final Object object;
    private final boolean returns;
    private final Class<? extends CommandSender> senderType;
    private final Class<?>[] types;
    private final OptionMapping<?>[] mappings;
    private final Executor executor;
    private final Completer tabCompleter;

    @SuppressWarnings("unchecked")
    SpigotCommand(@NotNull Plugin plugin, @NotNull String name, String lable, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases, @NotNull String permission, @NotNull Method method, @NotNull Object obj, @NotNull Executor executor, @NotNull Completer tabCompleter) {
        super(name, description, usageMessage, aliases);
        this.plugin = plugin;
        if (lable!=null) setLabel(lable);
        this.setPermission(permission.isBlank() ? null : permission);
        this.method = method;
        this.object = obj;
        this.returns = List.of(boolean.class, Boolean.class).contains(method.getReturnType());
        Class<?>[] pTypes = method.getParameterTypes();
        this.types = new Class<?>[pTypes.length];
        Class<? extends CommandSender> sender = null;
        for (int i = 0; i < pTypes.length; i++) {
            Class<?> t = pTypes[i];
            if (CommandSender.class.isAssignableFrom(t) && sender==null) {
                sender = (Class<? extends CommandSender>) t;
                types[i] = SenderPlaceholder.class;
            } else types[i] = t;
        }
        this.mappings = new OptionMapping<?>[types.length-(sender==null?0:1)];
        if (sender==null) for (int i = 0; i < mappings.length; i++) mappings[i] = CommandHandler.getMapping(types[i]);
        else {
            int i = 0;
            for (Class<?> type : types) if (!type.equals(SenderPlaceholder.class)) mappings[i++] = CommandHandler.getMapping(type);
        }
        this.senderType = sender;
        this.executor = executor;
        this.tabCompleter = tabCompleter;
        DefaultBrigadier brigadier = CommandHandler.USE_BRIGADIER ? DefaultBrigadier.getBrigadier(plugin) : null;
        if (brigadier !=null) {
            int i = 0;
            Annotation[][] annotations = method.getParameterAnnotations();
            LiteralArgumentBuilder<?> builder = LiteralArgumentBuilder.literal(name);
            for (OptionMapping<?> mapping : mappings) {
                Class<?> type = types[i];
                if (!type.equals(SenderPlaceholder.class)) {
                    for (Annotation annotation : annotations[i]) {
                        if (checkAnnotation(annotation)) {
                            ArgumentType<?> argumentType = mapping.getArgumentType(annotation);
                            if (argumentType!=null) {
                                builder.then(RequiredArgumentBuilder.argument(getName(annotation), argumentType));
                            } else {
                                for (String s : mapping.tabComplete(null)) {
                                    builder.then(LiteralArgumentBuilder.literal(s));
                                }
                            }
                        }
                    }
                }
                i++;
            }
            brigadier.register(this, builder);
        }
    }

    @SuppressWarnings("rawtypes")
    private String getName(Annotation annotation) {
        try {
            Method method = annotation.getClass().getMethod("name", (Class[]) null);
            return (String) method.invoke(annotation, (Object[]) null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    private boolean checkAnnotation(Annotation annotation) {
        return annotation.getClass().getPackageName().equals(Option.class.getPackageName());
    }

    @Override
    @SuppressWarnings({"nullable"})
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return tabCompleter.complete(sender, alias, args, mappings);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return executor.execute(new ExecutorDataProvider() {
            @Override
            public boolean isReturning() {
                return returns;
            }

            @Override
            public @Nullable Class<? extends CommandSender> getSenderType() {
                return senderType;
            }

            @Override
            public Class<?>[] getTypes() {
                return types;
            }

            @Override
            public OptionMapping<?>[] getMappings() {
                return mappings;
            }

            @Override
            public Method getMethod() {
                return method;
            }

            @Override
            public Object getInstance() {
                return object;
            }

            @Override
            public boolean isSP(Class<?> clazz) {
                return clazz.equals(SenderPlaceholder.class);
            }
        }, sender, commandLabel, args);
    }

    private interface SenderPlaceholder {}

    interface ExecutorDataProvider {
        boolean isReturning();

        @Nullable
        Class<? extends CommandSender> getSenderType();

        Class<?>[] getTypes();
        OptionMapping<?>[] getMappings();

        Method getMethod();

        Object getInstance();

        boolean isSP(Class<?> clazz);
    }

    interface Executor {
        boolean execute(@NotNull ExecutorDataProvider provider, @NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args);
    }

    interface Completer {
        @NotNull List<String> complete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args, @NotNull OptionMapping<?>[] mappings);
    }
}
