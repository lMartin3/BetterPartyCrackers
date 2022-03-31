package dev.martinl.betterpartycrackers.configuration;


import dev.martinl.betterpartycrackers.data.PartyCracker;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.Arrays;
import java.util.List;

@Getter
public class Config {
    private static Config instance = null;
    private static final PartyCracker examplePartyCracker = new PartyCracker(
            "example_cracker",
            "&cExample &6Party &eCracker",
            List.of("&eLore line 1", "&6Lore line 2"),
            Material.FIREWORK_ROCKET,
            true,
            3.5,
            Arrays.asList(Sound.ENTITY_CAT_HURT, Sound.BLOCK_ANVIL_HIT)
    );


    @Option protected ConfigOption prefix = new ConfigOption("chatPrefix", "&8[&6B&eP&cC&8] &r");
    @Option protected ConfigOption noPermissionMessage = new ConfigOption("noPermissionMessage", "&cInsufficient permissions");
    @Option protected ConfigOption crackers = new ConfigOption("crackers", Arrays.asList(examplePartyCracker.serialize()));

    public static Config getInst() {
        if(instance==null) {
            instance = new Config();
        }
        return instance;
    }
}
