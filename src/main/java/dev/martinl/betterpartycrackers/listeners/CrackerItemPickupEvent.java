package dev.martinl.betterpartycrackers.listeners;

import dev.martinl.betterpartycrackers.BetterPartyCrackers;
import dev.martinl.betterpartycrackers.manager.PartyCrackerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

public class CrackerItemPickupEvent implements Listener {
    public CrackerItemPickupEvent() {
        Bukkit.getServer().getPluginManager().registerEvents(this, BetterPartyCrackers.getPlugin());
    }


    @EventHandler
    public void onCrackerPickupByHopper(InventoryPickupItemEvent event) {
        if(BetterPartyCrackers.getPlugin().getCrackerManager().isCrackerEntity(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrackerPickupByEntity(EntityPickupItemEvent event) {
        if(BetterPartyCrackers.getPlugin().getCrackerManager().isCrackerEntity(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMerge(ItemMergeEvent event) {
        PartyCrackerManager manager = BetterPartyCrackers.getPlugin().getCrackerManager();
        if(manager.isCrackerEntity(event.getEntity()) || manager.isCrackerEntity(event.getTarget())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrackerPickupByEntity(ItemDespawnEvent event) {
        if(BetterPartyCrackers.getPlugin().getCrackerManager().isCrackerEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

}
