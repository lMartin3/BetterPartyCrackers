package dev.martinl.betterpartycrackers;

import dev.martinl.betterpartycrackers.commands.MainCommand;
import dev.martinl.betterpartycrackers.configuration.ConfigurationManager;
import dev.martinl.betterpartycrackers.listeners.CrackerUseListener;
import dev.martinl.betterpartycrackers.manager.PartyCrackerManager;
import dev.martinl.betterpartycrackers.util.StringUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterPartyCrackers extends JavaPlugin {
    private static BetterPartyCrackers instance;
    @Getter private PartyCrackerManager crackerManager;
    @Getter private NamespacedKey namespacedKey;

    @Override
    public void onEnable() {
        instance = this;
        ConfigurationManager.reload();
        crackerManager = new PartyCrackerManager();
        crackerManager.reloadData();
        new MainCommand();
        new CrackerUseListener();

        namespacedKey = new NamespacedKey(BetterPartyCrackers.getPlugin(), "party_cracker");
    }

    @Override
    public void onDisable() {
    }

    public static BetterPartyCrackers getPlugin() {
        return instance;
    }
}
