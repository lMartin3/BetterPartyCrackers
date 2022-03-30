package dev.martinl.betterpartycrackers.manager;

import dev.martinl.betterpartycrackers.configuration.Config;
import dev.martinl.betterpartycrackers.data.PartyCracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("FieldMayBeFinal")
public class PartyCrackerManager {
    private Map<String, PartyCracker> partyCrackerTypes = new HashMap<>();
    private List<Integer> failedCrackers = new ArrayList<>();

    public PartyCrackerManager() {}

    public void reloadData() {
        partyCrackerTypes.clear();
        failedCrackers.clear();
        List<Map<?, ?>> configList = Config.getInst().getCrackers().asMapList();
        for(int i=0;i<configList.size();i++) {
            Map<?, ?> crackerData = configList.get(i);
            try {
                PartyCracker partyCracker = PartyCracker.fromSerializedData(crackerData);
                partyCrackerTypes.put(partyCracker.getId(), partyCracker);
            } catch(Exception e) {
                failedCrackers.add(i+1);
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Better Party Crackers - " +
                        "Failed to load PartyCracker number " + ChatColor.YELLOW + (i+1) + ChatColor.RED + ". " +
                        "The following error occurred: " + ChatColor.WHITE + e.getMessage().split("\n")[0]);
                e.printStackTrace();
            }
        }
        Bukkit.getConsoleSender().sendMessage(Config.getInst().getPrefix().asFormattedString() + ChatColor.GREEN +
                "Reload complete, loaded a total of " + ChatColor.YELLOW + partyCrackerTypes.size() + ChatColor.GREEN + " party cracker types.");
        if(failedCrackers.size()>0) {
            Bukkit.getConsoleSender().sendMessage(Config.getInst().getPrefix().asFormattedString() + ChatColor.GOLD +
                    "Failed to load " + ChatColor.YELLOW + failedCrackers.size() + ChatColor.GOLD + " types: "
            + ChatColor.YELLOW + failedCrackers);
        }
    }
}
