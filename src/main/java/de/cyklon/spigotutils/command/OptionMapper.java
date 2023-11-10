package de.cyklon.spigotutils.command;

import com.mojang.brigadier.arguments.*;
import de.cyklon.spigotutils.command.annotation.option.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

class OptionMapper {

    //primitives and java.lang types
    static class String extends OptionMapping<java.lang.String> {

        @Override
        public java.lang.String map(java.lang.String argument) {
            return argument;
        }

        @Override
        public ArgumentType<java.lang.String> getArgumentType(Annotation annotation) {
            if (annotation instanceof StringOption opt) return switch (opt.type()) {
                case SINGLE_WORD -> StringArgumentType.word();
                case QUOTABLE_PHRASE -> StringArgumentType.string();
                case GREEDY_PHRASE -> StringArgumentType.greedyString();
            };
            return StringArgumentType.string();
        }
    }

    static class Byte extends OptionMapping<java.lang.Byte> {

        @Override
        public java.lang.Byte map(java.lang.String argument) {
            return java.lang.Byte.parseByte(argument);
        }
    }

    static class Short extends OptionMapping<java.lang.Short> {

        @Override
        public java.lang.Short map(java.lang.String argument) {
            return java.lang.Short.parseShort(argument);
        }
    }

    static class Integer extends OptionMapping<java.lang.Integer> {

        @Override
        public java.lang.Integer map(java.lang.String argument) {
            return java.lang.Integer.parseInt(argument);
        }

        @Override
        public ArgumentType<java.lang.Integer> getArgumentType(Annotation annotation) {
            if (annotation instanceof IntegerOption opt) return IntegerArgumentType.integer(opt.minValue(), opt.maxValue());
            return IntegerArgumentType.integer();
        }
    }

    static class Long extends OptionMapping<java.lang.Long> {

        @Override
        public java.lang.Long map(java.lang.String argument) {
            return java.lang.Long.parseLong(argument);
        }

        @Override
        public ArgumentType<java.lang.Long> getArgumentType(Annotation annotation) {
            if (annotation instanceof LongOption opt) return LongArgumentType.longArg(opt.minValue(), opt.maxValue());
            return LongArgumentType.longArg();
        }
    }

    static class Float extends OptionMapping<java.lang.Float> {

        @Override
        public java.lang.Float map(java.lang.String argument) {
            return java.lang.Float.parseFloat(argument);
        }

        @Override
        public ArgumentType<java.lang.Float> getArgumentType(Annotation annotation) {
            if (annotation instanceof FloatOption opt) return FloatArgumentType.floatArg(opt.minValue(), opt.maxValue());
            return FloatArgumentType.floatArg();
        }
    }

    static class Double extends OptionMapping<java.lang.Double> {

        @Override
        public java.lang.Double map(java.lang.String argument) {
            return java.lang.Double.parseDouble(argument);
        }

        @Override
        public ArgumentType<java.lang.Double> getArgumentType(Annotation annotation) {
            if (annotation instanceof DoubleOption opt) return DoubleArgumentType.doubleArg(opt.minValue(), opt.maxValue());
            return DoubleArgumentType.doubleArg();
        }
    }

    static class Character extends OptionMapping<java.lang.Character> {
        @Override
        public java.lang.Character map(java.lang.String argument) {
            if ((argument.length()!=1 && argument.length()!=3) || argument.isBlank()) throw new ClassCastException();
            if (argument.length()==1) return argument.charAt(0);
            if (!argument.startsWith("'") || !argument.endsWith("'")) throw new ClassCastException();
            return argument.charAt(1);
        }
    }

    static class Boolean extends OptionMapping<java.lang.Boolean> {

        @Override
        public java.lang.Boolean map(java.lang.String argument) {
            return java.lang.Boolean.parseBoolean(argument);
        }

        @Override
        @NotNull
        public List<java.lang.String> tabComplete(@Nullable CommandSender sender) {
            return List.of("true", "false");
        }

        @Override
        public ArgumentType<java.lang.Boolean> getArgumentType(Annotation annotation) {
            return BoolArgumentType.bool();
        }
    }

    static class Enum<T extends java.lang.Enum<T>> extends OptionMapping<T> {

        private final Class<T> enumClass;

        public Enum(Class<T> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        public T map(java.lang.String argument) {
            return java.lang.Enum.valueOf(enumClass, argument);
        }

        @Override
        @NotNull
        public List<java.lang.String> tabComplete(@Nullable CommandSender sender) {
            return Arrays.stream(enumClass.getEnumConstants()).map(java.lang.Enum::toString).toList();
        }

        @Override
        @SuppressWarnings("deprecation")
        public java.lang.String exceptionMessage(java.lang.String argument, Exception e) {
            if (e instanceof IllegalArgumentException) return ChatColor.RED + "Constant \"" + argument + "\" is Invalid";
            return null;
        }
    }


    //Spigot types
    static class Player extends OptionMapping<org.bukkit.entity.Player> {

        @Override
        public org.bukkit.entity.Player map(java.lang.String argument) {
            return Bukkit.getPlayer(argument);
        }

        @Override
        @NotNull
        public List<java.lang.String> tabComplete(@Nullable CommandSender sender) {
            return Bukkit.getOnlinePlayers().stream().map(org.bukkit.entity.Player::getName).toList();
        }

        @Override
        @SuppressWarnings("deprecation")
        public java.lang.String nullMessage(java.lang.String argument) {
            return ChatColor.RED + "no Player \"" + argument +"\" found!";
        }
    }

    static class OfflinePlayer extends OptionMapping<org.bukkit.OfflinePlayer> {

        @Override
        public org.bukkit.OfflinePlayer map(java.lang.String argument) {
            return Bukkit.getOfflinePlayer(argument);
        }

        @Override
        @NotNull
        public List<java.lang.String> tabComplete(@Nullable CommandSender sender) {
            return Arrays.stream(Bukkit.getOfflinePlayers()).map(org.bukkit.OfflinePlayer::getName).toList();
        }
    }

    static class World extends OptionMapping<org.bukkit.World> {

        @Override
        public org.bukkit.World map(java.lang.String argument) {
            return Bukkit.getWorld(argument);
        }

        @Override
        @NotNull
        public List<java.lang.String> tabComplete(@Nullable CommandSender sender) {
            return Bukkit.getWorlds().stream().map(org.bukkit.World::getName).toList();
        }

        @Override
        @SuppressWarnings("deprecation")
        public java.lang.String nullMessage(java.lang.String argument) {
            return ChatColor.RED + "no World \"" + argument +"\" found!";
        }
    }


}
