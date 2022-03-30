package dev.martinl.betterpartycrackers;

import dev.martinl.betterpartycrackers.configuration.ConfigurationManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterPartyCrackers extends JavaPlugin {
    private static BetterPartyCrackers instance;

    @Override
    public void onEnable() {
        instance = this;
        ConfigurationManager.reload();
    }

    @Override
    public void onDisable() {
    }

    public static BetterPartyCrackers getPlugin() {
        return instance;
    }
}
