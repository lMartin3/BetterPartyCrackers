package dev.martinl.betterpartycrackers.listeners;

import dev.martinl.betterpartycrackers.BetterPartyCrackers;
import dev.martinl.betterpartycrackers.data.PartyCracker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class CrackerUseListener implements Listener {
    public CrackerUseListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, BetterPartyCrackers.getPlugin());
    }


    @EventHandler
    public void onCrackerUse(PlayerInteractEvent event) {
        if(!Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK).contains(event.getAction())) return;
        ItemStack usedItem = event.getItem();
        if(usedItem==null||usedItem.getItemMeta()==null) return;
        String crackerTypeId = usedItem.getItemMeta().getPersistentDataContainer().get(BetterPartyCrackers.getPlugin().getNamespacedKey(), PersistentDataType.STRING);
        if(crackerTypeId==null) return;
        PartyCracker crackerType = BetterPartyCrackers.getPlugin().getCrackerManager().getPartyCracker(crackerTypeId);
        if(crackerType==null) return;
        Player player = event.getPlayer();
        event.setCancelled(true);
        usedItem.setAmount(usedItem.getAmount()-1);
        Location location = player.getEyeLocation().clone();

        BetterPartyCrackers.getPlugin().getCrackerManager().dropCracker(crackerType, location, location.getDirection());
    }
}
