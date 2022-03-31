package dev.martinl.betterpartycrackers.util;

import org.bukkit.Color;

import java.util.concurrent.ThreadLocalRandom;

public class ColorUtil {
    public static Color[] colors = new Color[]{
            Color.AQUA,
            Color.BLACK,
            Color.BLUE,
            Color.FUCHSIA,
            Color.GRAY,
            Color.GREEN,
            Color.LIME,
            Color.MAROON,
            Color.NAVY,
            Color.OLIVE,
            Color.ORANGE,
            Color.PURPLE,
            Color.RED,
            Color.SILVER,
            Color.TEAL,
            Color.WHITE,
            Color.YELLOW,
    };

    public static Color getRandomColor() {
        return colors[ThreadLocalRandom.current().nextInt(colors.length)];
    }
}
