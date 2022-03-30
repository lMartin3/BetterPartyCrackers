package dev.martinl.betterpartycrackers.configuration;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.List;

/**
 * A ConfigOption is a setting that will be saved in the config.yml file inside the plugin folder. When the plugin is enabled,
 * all configuration options in {@link Config} with the annotation {@link Option} above will be read from the file, if the setting does not exist,
 * the default value will be written.
 */
public class ConfigOption {
    private final String key;
    private final Object defaultValue;

    /**
     * @param key          The name of the setting in the config. Use a unique name without spaces.
     * @param defaultValue The default value of the setting. This will be saved in the config if the setting is not set. This can be any object that is supported by spigots configuration API.
     */
    public ConfigOption(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * @return The value of the option as a boolean.
     */
    public boolean asBoolean() {
        return ConfigurationManager.optionAsBoolean(this);
    }

    /**
     * @return The value of the option as a double.
     */
    public double asDouble() {
        return ConfigurationManager.optionAsDouble(this);
    }

    /**
     * @return The value of the option as an integer.
     */
    public int asInt() {
        return ConfigurationManager.optionAsInt(this);
    }

    /**
     * @return The value of the option as a string.
     */
    public String asString() {
        return ConfigurationManager.optionAsString(this);
    }

    /**
     * @return The value of the option as a string formatted with chat color.
     */
    public String asFormattedString() {
        return ChatColor.translateAlternateColorCodes('&', ConfigurationManager.optionAsString(this));
    }

    /**
     * @return The value of the option as a list of strings.
     */
    public List<String> asStringList() {
        return ConfigurationManager.optionAsStringList(this);
    }

    /**
     * @return The value of the option as a chat color.
     */
    public ChatColor asChatColor() {
        return ChatColor.valueOf(ConfigurationManager.optionAsString(this));
    }

    /**
     * @return The value of the option as a location.
     */
    public Location asLocation() {
        return ConfigurationManager.optionAsLocation(this);
    }


}
