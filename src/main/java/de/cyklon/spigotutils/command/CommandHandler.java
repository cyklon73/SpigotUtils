package de.cyklon.spigotutils.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import de.cyklon.spigotutils.command.annotation.Command;
import de.cyklon.spigotutils.version.MinecraftVersion;
import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

class CommandHandler implements CommandManager {

    //static final boolean USE_BRIGADIER = MinecraftVersion.v1_13.isHigherOrEqual(Bukkit.getServer());
    //deactivated because it does not yet work
    static final boolean USE_BRIGADIER = false;

    private static final Map<Class<?>, OptionMapping<?>> OPTION_MAP = new HashMap<>();

    static {
        OPTION_MAP.put(String.class, new OptionMapper.String());
        OPTION_MAP.put(Byte.class, new OptionMapper.Byte());
        OPTION_MAP.put(Short.class, new OptionMapper.Short());
        OPTION_MAP.put(Integer.class, new OptionMapper.Integer());
        OPTION_MAP.put(Long.class, new OptionMapper.Long());
        OPTION_MAP.put(Float.class, new OptionMapper.Float());
        OPTION_MAP.put(Double.class, new OptionMapper.Double());
        OPTION_MAP.put(Character.class, new OptionMapper.Character());
        OPTION_MAP.put(Boolean.class, new OptionMapper.Boolean());

        OPTION_MAP.put(Player.class, new OptionMapper.Player());
        OPTION_MAP.put(OfflinePlayer.class, new OptionMapper.OfflinePlayer());
        OPTION_MAP.put(World.class, new OptionMapper.World());
    }

    static CommandManager manager() {
        return new CommandHandler();
    }

    @Override
    public @NotNull Collection<org.bukkit.command.Command> register(@NotNull Plugin plugin, @NotNull Object obj) {
        Method[] publicMethods = obj.getClass().getMethods();
        Method[] privateMethods = obj.getClass().getDeclaredMethods();
        Set<Method> methods = new HashSet<>(publicMethods.length + privateMethods.length, 1.0f);
        methods.addAll(Arrays.asList(publicMethods));
        methods.addAll(Arrays.asList(privateMethods));

        Collection<org.bukkit.command.Command> commands = new ArrayList<>();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Command.class)) continue;
            // Do not register bridge or synthetic methods to avoid command duplication
            if (method.isBridge() || method.isSynthetic()) continue;
            commands.add(register(plugin, method, obj));
        }
        return commands;
    }

    @Override
    public <T> void putOptionMapping(Class<T> type, OptionMapping<T> mapping) {
        OPTION_MAP.put(type, mapping);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static OptionMapping<?> getMapping(Class<?> clazz) {
        OptionMapping<?> mapping = clazz.isEnum() ? new OptionMapper.Enum<>((Class<? extends Enum>)clazz) : OPTION_MAP.get(clazz);
        if (mapping==null) {
            for (Class<?> map : OPTION_MAP.keySet()) if (clazz.isAssignableFrom(map)) mapping = OPTION_MAP.get(map);
            if (mapping==null) throw new IllegalArgumentException("No mapping for argument type " + clazz + " registered");
        }
        return mapping;
    }

    @SuppressWarnings({"deprecation", "SpellCheckingInspection"})
    private org.bukkit.command.Command register(Plugin plugin, Method method, Object obj) {
        Command a = method.getAnnotation(Command.class);
        if (a==null) throw new IllegalArgumentException("cannot register method without " + Command.class + " annotation");
        SpigotCommand scommand = new SpigotCommand(plugin, a.name(), a.prefix().isBlank() ? plugin.getName() : a.prefix(), a.description(), a.usage(), Arrays.asList(a.aliases()), a.permission(), method, obj, (provider, sender, commandLabel, args) -> {
            Class<? extends CommandSender> senderType = provider.getSenderType();
            if (senderType != null && !senderType.isAssignableFrom(sender.getClass())) return true;
            Class<?>[] pTypes = provider.getTypes();
            Object[] parameters = new Object[pTypes.length];
            if (senderType == null) {
                if (parameters.length != args.length) return false;
            } else if (parameters.length - 1 != args.length) return false;
            OptionMapping<?>[] mappings = provider.getMappings();
            int i1 = 0;
            for (int i = 0; i < pTypes.length; i++) {
                Class<?> t = pTypes[i];
                if (provider.isSP(t) && senderType != null) parameters[i] = senderType.cast(sender);
                else {
                    OptionMapping<?> mapping = mappings[i1];
                    try {
                        parameters[i] = mapping.map(args[i1++]);
                        if (parameters[i] == null) {
                            String msg = mapping.nullMessage(args[--i1]);
                            if (msg != null) {
                                sender.sendMessage(msg);
                                return true;
                            }
                        }
                    } catch (ClassCastException | NumberFormatException e) {
                        sender.sendMessage(String.format("%sThe value \"%s\" is invalid.\nAn %s was expected.", ChatColor.RED, args[--i1], t.getSimpleName()));
                        return true;
                    } catch (Exception e) {
                        String msg = mapping.exceptionMessage(args[--i1], e);
                        if (msg != null) sender.sendMessage(msg);
                        return true;
                    }
                }
            }

            Object result;
            try {
                result = provider.getMethod().invoke(provider.getInstance(), parameters);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            if (provider.isReturning()) return (boolean) result;
            return true;
        }, (sender, alias, args, mappings) -> {
            if (USE_BRIGADIER) return Collections.emptyList();
            int i = args.length-1;
            OptionMapping<?> mapping = i<mappings.length ? mappings[i] : null;
            List<String> list = new ArrayList<>();
            if (mapping!=null) list = new ArrayList<>(mapping.tabComplete(sender));
            list.removeIf(p -> !p.startsWith(args[args.length-1]));
            return list;
        });
        register(scommand);
        return scommand;
    }

    private void register(SpigotCommand command) {
        getCommandMap(Bukkit.getServer()).register(command.getLabel(), command);
    }

    @SneakyThrows
    private CommandMap getCommandMap(Server server) {
        final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        bukkitCommandMap.setAccessible(true);
        return (CommandMap) bukkitCommandMap.get(server);
    }
}
