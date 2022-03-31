package dev.martinl.betterpartycrackers.manager;

import dev.martinl.betterpartycrackers.configuration.Config;
import dev.martinl.betterpartycrackers.data.PartyCracker;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;

import java.util.*;

@SuppressWarnings("FieldMayBeFinal")
public class PartyCrackerManager {
    private Map<String, PartyCracker> partyCrackerTypes = new HashMap<>();
    private Map<Item, CrackerItemEntityData> partyCrackerEntities = new HashMap<>();
    private List<Integer> failedCrackers = new ArrayList<>();

    public PartyCrackerManager() {

    }

    public void reloadData() {
        partyCrackerTypes.clear();
        failedCrackers.clear();
        List<Map<?, ?>> configList = Config.getInst().getCrackers().asMapList();
        for(int i=0;i<configList.size();i++) {
            Map<?, ?> crackerData = configList.get(i);
            try {
                PartyCracker partyCracker = PartyCracker.fromSerializedData(crackerData);
                partyCrackerTypes.put(partyCracker.getId(), partyCracker);
                Bukkit.broadcastMessage("Sounds: " + partyCracker.getPossibleSounds());
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

    public PartyCracker getPartyCracker(String id) {
        return partyCrackerTypes.get(id);
    }

    public List<PartyCracker> getPartyCrackerTypeList() {
        return new ArrayList<>(partyCrackerTypes.values());
    }

    public void dropCracker(PartyCracker partyCracker, Location location, Vector direction) {
        if(location.getWorld()==null) return;
        Item itemEntity = location.getWorld().dropItem(location, partyCracker.buildItem());
        itemEntity.setPickupDelay(Integer.MAX_VALUE);
        itemEntity.setVelocity(direction.multiply(0.5));
        partyCrackerEntities.put(itemEntity, new CrackerItemEntityData(partyCracker, System.currentTimeMillis()));

    }

    private void explodeCracker(PartyCracker partyCracker, Location location) {

    }

    private void loopItemEntities() {
        ArrayList<Item> toRemove = new ArrayList<>();
        for(Map.Entry<Item, CrackerItemEntityData> entry : partyCrackerEntities.entrySet()) {
            Item itemEntity = entry.getKey();
            PartyCracker crackerType = entry.getValue().getCracker();
            long droppedTime = entry.getValue().getDroppedOn();

            if(droppedTime>0) {

            }
        }
    }

    @Data @AllArgsConstructor
    class CrackerItemEntityData {
        private final PartyCracker cracker;
        private final long droppedOn;
    }
}
