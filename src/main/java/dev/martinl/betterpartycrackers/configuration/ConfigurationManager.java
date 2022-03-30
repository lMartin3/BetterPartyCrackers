package dev.martinl.betterpartycrackers.configuration;

import dev.martinl.betterpartycrackers.BetterPartyCrackers;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager {
    private static FileConfiguration configFile = BetterPartyCrackers.getPlugin().getConfig();

    public static void reload() {
        configFile = BetterPartyCrackers.getPlugin().getConfig();

        for (ConfigOption option : getOptions(Config.getInst())) {
            if (!configFile.contains(option.getKey())) {
                configFile.set(option.getKey(), option.getDefaultValue());
            }
        }
        save();
    }

    public static String optionAsString(ConfigOption option) {
        return configFile.getString(option.getKey());
    }

    public static int optionAsInt(ConfigOption option) {
        return configFile.getInt(option.getKey());
    }

    public static double optionAsDouble(ConfigOption option) {
        return configFile.getDouble(option.getKey());
    }

    public static boolean optionAsBoolean(ConfigOption option) {
        return configFile.getBoolean(option.getKey());
    }

    public static List<String> optionAsStringList(ConfigOption option) {
        return configFile.getStringList(option.getKey());
    }

    public static Location optionAsLocation(ConfigOption option) {
        String string = configFile.getString(option.getKey());
        if(string==null) return null;
        String[] split = string.split(",");
        Location loc = new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
        if(split.length>=6) {
            loc.setYaw(Float.parseFloat(split[4]));
            loc.setPitch(Float.parseFloat(split[5]));
        }
        return loc;
    }

    @NonNull
    public static List<ConfigOption> getOptions(Object obj) {
        List<ConfigOption> options = new ArrayList<>();
        Class<? extends Annotation> annotation = Option.class;
        Class<?> objClass = obj.getClass();
        for (Field field : objClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                try {
                    options.add((ConfigOption) field.get(obj));
                } catch (Exception ignored) {

                }
            }
        }
        return options;
    }

    public static void save() {
        BetterPartyCrackers.getPlugin().saveConfig();
    }
}

