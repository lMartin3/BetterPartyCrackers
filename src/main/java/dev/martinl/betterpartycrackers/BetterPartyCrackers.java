package dev.martinl.betterpartycrackers;

import dev.martinl.betterpartycrackers.commands.MainCommand;
import dev.martinl.betterpartycrackers.configuration.ConfigurationManager;
import dev.martinl.betterpartycrackers.manager.PartyCrackerManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterPartyCrackers extends JavaPlugin {
    private static BetterPartyCrackers instance;
    @Getter private PartyCrackerManager crackerManager;

    @Override
    public void onEnable() {
        instance = this;
        ConfigurationManager.reload();
        crackerManager = new PartyCrackerManager();
        crackerManager.reloadData();

        //noinspection ConstantConditions
        this.getCommand("bpc").setExecutor(new MainCommand());
    }

    @Override
    public void onDisable() {
    }

    public static BetterPartyCrackers getPlugin() {
        return instance;
    }
}
