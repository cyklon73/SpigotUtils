package de.cyklon.spigotutils.adventure;

import de.cyklon.spigotutils.version.Version;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public final class Formatter {

    @SuppressWarnings("ConstantConditions")
    private static final Map<Character, TextColor> COLORS = Map.ofEntries(
            Map.entry('0', TextColor.fromHexString("#000000")),
            Map.entry('1', TextColor.fromHexString("#0000AA")),
            Map.entry('2', TextColor.fromHexString("#00AA00")),
            Map.entry('3', TextColor.fromHexString("#00AAAA")),
            Map.entry('4', TextColor.fromHexString("#AA0000")),
            Map.entry('5', TextColor.fromHexString("#AA00AA")),
            Map.entry('6', TextColor.fromHexString("#FFAA00")),
            Map.entry('7', TextColor.fromHexString("#AAAAAA")),
            Map.entry('8', TextColor.fromHexString("#555555")),
            Map.entry('9', TextColor.fromHexString("#5555FF")),
            Map.entry('a', TextColor.fromHexString("#55FF55")),
            Map.entry('b', TextColor.fromHexString("#55FFFF")),
            Map.entry('c', TextColor.fromHexString("#FF5555")),
            Map.entry('d', TextColor.fromHexString("#FF55FF")),
            Map.entry('e', TextColor.fromHexString("#FFFF55")),
            Map.entry('f', TextColor.fromHexString("#FFFFFF")),
            Map.entry('g', TextColor.fromHexString("#DDD605")),
            Map.entry('h', TextColor.fromHexString("#E3D4D1")),
            Map.entry('i', TextColor.fromHexString("#CECACA")),
            Map.entry('j', TextColor.fromHexString("#443A3B")),
            Map.entry('x', TextColor.fromHexString("#971607")),
            Map.entry('y', TextColor.fromHexString("#B4684D")),
            Map.entry('p', TextColor.fromHexString("#DEB12D")),
            Map.entry('q', TextColor.fromHexString("#47A036")),
            Map.entry('s', TextColor.fromHexString("#2CBAA8")),
            Map.entry('t', TextColor.fromHexString("#21497B")),
            Map.entry('u', TextColor.fromHexString("#9A5CC6"))
    );

    private static final Map<Character, TextDecoration> DECORATIONS = Map.of(
            'k', TextDecoration.OBFUSCATED,
            'l', TextDecoration.BOLD,
            'm', TextDecoration.STRIKETHROUGH,
            'n', TextDecoration.UNDERLINED,
            'o', TextDecoration.ITALIC
    );

    private static final Map<Character, TextColor> CUSTOM_COLORS = new HashMap<>();


    /**
     * this method uses § as prefix, like everywhere in Minecraft
     * <p>>
     * all formatting from <a href="https://minecraft.fandom.com/wiki/Formatting_codes">this table</a> (even those marked as Bedrock only) can be parsed
     * <p>
     * Since §m and §n are assigned twice, the assignments for the two colors have been changed.
     * <p>
     * color §m -> §x
     * <p>
     * color §n -> §y
     * @param text the text to parse
     * @return the advanture Component parsed from this text
     */
    public static Component parseText(String text) {
        return parseText("§", text);
    }


    /**
     * all formatting from <a href="https://minecraft.fandom.com/wiki/Formatting_codes">this table</a> (even those marked as Bedrock only) can be parsed
     * <p>
     * Since §m and §n are assigned twice, the assignments for the two colors have been changed.
     * <p>
     * color §m -> §x
     * <p>
     * color §n -> §y
     * @param text the text to parse
     * @param prefix the prefix for formattings
     * @return the advanture Component parsed from this text
     */
    public static Component parseText(String prefix, String text) {
        Version.requirePaper();
        Component component = null;
        String[] args = text.split(prefix);
        Map<TextDecoration, TextDecoration.State> decorations = null;
        TextColor colors = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.length() == 0) continue;
            char format = arg.charAt(0);
            if (i==0 && !text.startsWith(prefix)) format = ' ';
            TextColor color = COLORS.get(format);
            if (color==null) color = CUSTOM_COLORS.get(format);
            TextDecoration decoration = DECORATIONS.get(format);

            Component c;
            if (format != 'r' && color == null && decoration == null) {
                boolean f = false;
                if (i==0) {
                    if (text.startsWith(prefix)) f = true;
                } else f = true;
                c = Component.text((f ? prefix : "") + arg);
            } else {
                c = Component.text(arg.substring(1));

                if (decorations==null) decorations = new HashMap<>(c.decorations());
                if (format == 'r') {
                    colors = COLORS.get('f');
                    for (TextDecoration value : TextDecoration.values()) decorations.put(value, TextDecoration.State.FALSE);
                } else {
                    if (color != null) colors = color;
                    if (decoration != null) decorations.put(decoration, TextDecoration.State.TRUE);
                }
                if (colors!=null) c = c.color(colors);
                c = c.decorations(decorations);
            }

            if (component == null) component = c;
            else component = component.append(c);
        }
        if (component==null) component = Component.text(text);
        return component;
    }

    public static Map<Character, TextColor> getCustomFormattings() {
        return new HashMap<>(CUSTOM_COLORS);
    }

    public static void registerCustomFormatting(char format, TextColor color) {
        if (COLORS.containsKey(format) || DECORATIONS.containsKey(format)) throw new IllegalArgumentException("you can only use formats that are not yet used in the standard formattings");
        if (CUSTOM_COLORS.containsKey(format)) throw new IllegalArgumentException("Custom formatting has already been registered with the format '" + format + "'");
        CUSTOM_COLORS.put(format, color);
    }

    public static TextColor unregisterCustomFormatting(char format) {
        return CUSTOM_COLORS.remove(format);
    }

    public static Map<Character, TextColor> getColorFormattings() {
        return new HashMap<>(COLORS);
    }

    public static Map<Character, TextDecoration> getDecorationFormattings() {
        return new HashMap<>(DECORATIONS);
    }


    private Formatter() {}

}
