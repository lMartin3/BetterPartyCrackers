package dev.martinl.betterpartycrackers.configuration;


import dev.martinl.betterpartycrackers.data.PartyCracker;
import dev.martinl.betterpartycrackers.data.PartyCrackerReward;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.Arrays;
import java.util.List;

@Getter
public class Config {
    private static final PartyCracker examplePartyCracker = new PartyCracker(
            "example_cracker",
            "&cExample &6Party &eCracker",
            List.of("&eLore line 1", "&6Lore line 2"),
            Material.FIREWORK_ROCKET,
            true,
            3500,
            Sound.BLOCK_NOTE_BLOCK_BASS,
            Arrays.asList(Sound.ENTITY_CAT_HURT, Sound.BLOCK_AMETHYST_BLOCK_CHIME),
            Arrays.asList(Particle.EXPLOSION_LARGE, Particle.FIREWORKS_SPARK),
            true,
            Arrays.asList(new PartyCrackerReward(Material.DIAMOND, 1, 20))
    );
    private static Config instance = null;
    @Option
    protected ConfigOption prefix = new ConfigOption("chat_prefix", "&8[&6B&eP&cC&8] &r");
    @Option
    protected ConfigOption noPermissionMessage = new ConfigOption("no_permission_message", "&cInsufficient permissions");
    @Option
    protected ConfigOption tickFrequency = new ConfigOption("tick_frequency", 2);
    @Option
    protected ConfigOption showTimeRemaining = new ConfigOption("show_time_remaining", true);
    @Option
    protected ConfigOption crackers = new ConfigOption("crackers", Arrays.asList(examplePartyCracker.serialize()));

    public static Config getInst() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }
}
