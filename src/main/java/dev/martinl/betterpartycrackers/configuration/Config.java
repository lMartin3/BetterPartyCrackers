package dev.martinl.betterpartycrackers.configuration;


import dev.martinl.betterpartycrackers.data.PartyCracker;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.Arrays;

@Getter
public class Config {
    private static Config instance = null;
    private static final PartyCracker examplePartyCracker = new PartyCracker(
            "&cExample &6Party &eCracker",
            "example_cracker",
            Material.FIREWORK_ROCKET,
            Arrays.asList(Sound.ENTITY_CAT_HURT, Sound.BLOCK_ANVIL_HIT)
    );


    @Option protected ConfigOption prefix = new ConfigOption("chatPrefix", "&8[&6B&eP&cC&8] &r");
    @Option protected ConfigOption crackers = new ConfigOption("crackers", Arrays.asList(examplePartyCracker.serialize()));

    public static Config getInst() {
        if(instance==null) {
            instance = new Config();
        }
        return instance;
    }
}
