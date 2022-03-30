package dev.martinl.betterpartycrackers.configuration;


import lombok.Getter;

@Getter
public class Config {
    private static Config instance = null;

    @Option
    protected ConfigOption prefix = new ConfigOption("chatPrefix", "&8[&6B&eP&cC&8] ");

    public static Config getInst() {
        if(instance==null) {
            instance = new Config();
        }
        return instance;
    }
}
