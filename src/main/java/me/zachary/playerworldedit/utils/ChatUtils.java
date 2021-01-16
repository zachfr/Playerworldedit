package me.zachary.playerworldedit.utils;

import net.md_5.bungee.api.ChatColor;
import xyz.theprogramsrc.supercoreapi.spigot.utils.ReflectionUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {
    public static String colorCode(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static final Pattern regex = Pattern.compile("&#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3}).*?");

    public static String parseHexColors(String message) {
        final Matcher matcher = regex.matcher(message);
        while (matcher.find()) {
            final String color = matcher.group();
            final String hexcolor = color.substring(1);
            net.md_5.bungee.api.ChatColor c = null;
            try {
                c = net.md_5.bungee.api.ChatColor.of(hexcolor);
            } catch (final Exception ignored) {
            }
            if (c != null) {
                message = message.replaceAll(color, c.toString());
            }
        }
        return message;
    }

    public static String color(String message){
        if(ReflectionUtils.getVersion().contains("1_16")){
            message = parseHexColors(colorCode(message));
        }else{
            message = colorCode(message);
        }
        return message;
    }
}
