package dev.martinl.betterpartycrackers.manager;

import dev.martinl.betterpartycrackers.configuration.Config;
import dev.martinl.betterpartycrackers.data.PartyCracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartyCrackerManager {
    private Map<String, PartyCracker> partyCrackerTypes = new HashMap<>();

    public PartyCrackerManager() {}

    public void reloadData() {
        partyCrackerTypes.clear();
        List<Map<?, ?>> configList = Config.getInst().getCrackers().asMapList();
        for(int i=0;i<configList.size();i++) {
            Map<?, ?> crackerData = configList.get(i);
            try {
                PartyCracker partyCracker = PartyCracker.fromSerializedData(crackerData);
                partyCrackerTypes.put(partyCracker.getId(), partyCracker);
            } catch(Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Better Party Crackers - " +
                        "Failed to load PartyCracker number " + ChatColor.YELLOW + (i+1) + ChatColor.RED + ". " +
                        "The following error occurred: " + ChatColor.WHITE + e.getMessage().split("\n")[0]);
                e.printStackTrace();
            }
        }

        for(PartyCracker partyCracker : partyCrackerTypes.values()) {
            Bukkit.broadcastMessage("--- PARTY CRACKER---\n" +
                    "ID: " + partyCracker.getId() + "\n" +
                    "Name: " + partyCracker.getName() + "\n"+
                    "Material: " + partyCracker.getMaterial().toString() + "\n" +
                    "Sounds: " + partyCracker.getPossibleSounds().size() + "");
        }
    }
}
