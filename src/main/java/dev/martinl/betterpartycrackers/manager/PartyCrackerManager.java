package dev.martinl.betterpartycrackers.manager;

import dev.martinl.betterpartycrackers.BetterPartyCrackers;
import dev.martinl.betterpartycrackers.configuration.Config;
import dev.martinl.betterpartycrackers.data.PartyCracker;
import dev.martinl.betterpartycrackers.util.ColorUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("FieldMayBeFinal")
public class PartyCrackerManager {
    private Map<String, PartyCracker> partyCrackerTypes = new HashMap<>();
    private Map<Item, CrackerItemEntityData> partyCrackerEntities = new HashMap<>();
    private List<Integer> failedCrackers = new ArrayList<>();

    public PartyCrackerManager() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(BetterPartyCrackers.getPlugin(), this::loopItemEntities, 20, Config.getInst().getTickFrequency().asInt());
    }

    public void reloadData() {
        partyCrackerTypes.clear();
        failedCrackers.clear();
        List<Map<?, ?>> configList = Config.getInst().getCrackers().asMapList();
        for (int i = 0; i < configList.size(); i++) {
            Map<?, ?> crackerData = configList.get(i);
            try {
                PartyCracker partyCracker = PartyCracker.fromSerializedData(crackerData);
                if(!partyCrackerTypes.containsKey(partyCracker.getId())) {
                    partyCrackerTypes.put(partyCracker.getId(), partyCracker);
                } else {
                    Bukkit.getConsoleSender().sendMessage(Config.getInst().getPrefix().asFormattedString() + ChatColor.YELLOW + "Warning: duplicate cracker types with id " + partyCracker.getId());
                }
            } catch (Exception e) {
                failedCrackers.add(i + 1);
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Better Party Crackers - " +
                        "Failed to load PartyCracker number " + ChatColor.YELLOW + (i + 1) + ChatColor.RED + ". " +
                        "The following error occurred: " + ChatColor.WHITE + e.getMessage().split("\n")[0]);
                e.printStackTrace();
            }
        }
        Bukkit.getConsoleSender().sendMessage(Config.getInst().getPrefix().asFormattedString() + ChatColor.GREEN +
                "Reload complete, loaded a total of " + ChatColor.YELLOW + partyCrackerTypes.size() + ChatColor.GREEN + " party cracker types.");
        if (failedCrackers.size() > 0) {
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
        if (location.getWorld() == null) return;
        Item itemEntity = location.getWorld().dropItem(location, partyCracker.buildItem());
        itemEntity.setPickupDelay(Integer.MAX_VALUE);
        itemEntity.setVelocity(direction.multiply(0.5));
        itemEntity.setCustomNameVisible(true);
        partyCrackerEntities.put(itemEntity, new CrackerItemEntityData(partyCracker, System.currentTimeMillis()));

    }

    private void explodeCracker(PartyCracker partyCracker, Location location) {
        assert location.getWorld() != null;
        List<Sound> possibleSounds = partyCracker.getExplosionSounds();
        Sound sound = null;
        if(possibleSounds.size()>0) {
            sound = possibleSounds.get(ThreadLocalRandom.current().nextInt(possibleSounds.size()));
        }

        List<Particle> possibleEffects = partyCracker.getParticleEffects();
        Particle particleEffect = null;
        if(possibleEffects.size()>0) {
            particleEffect = possibleEffects.get(ThreadLocalRandom.current().nextInt(possibleEffects.size()));
        }

        Firework firework = null;
        if(partyCracker.isSpawnFirework()) {
            firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
            FireworkMeta meta = firework.getFireworkMeta();
            meta.setPower(127);
            meta.addEffect(FireworkEffect.builder().withColor(ColorUtil.getRandomColor()).build());
            firework.setFireworkMeta(meta);
        }

        if(sound!=null) location.getWorld().playSound(location, sound, 1f, 1f);
        if(particleEffect!=null) location.getWorld().spawnParticle(particleEffect, location, 5);
        if(firework!=null) firework.detonate();

        ItemStack reward = partyCracker.getRandomReward();
        if(reward!=null) {
            location.getWorld().dropItem(location, reward);
        }
    }

    private void loopItemEntities() {
        ArrayList<Item> toRemove = new ArrayList<>();
        for (Map.Entry<Item, CrackerItemEntityData> entry : partyCrackerEntities.entrySet()) {
            Item itemEntity = entry.getKey();
            PartyCracker crackerType = entry.getValue().getCracker();
            long droppedTime = entry.getValue().getDroppedOn();
            long timeRemaining = droppedTime + crackerType.getDetonationTime() - System.currentTimeMillis();
            if (timeRemaining<=0||itemEntity==null||itemEntity.isDead()) {
                explodeCracker(crackerType, itemEntity.getLocation());
                toRemove.add(itemEntity);
            } else {
                if(Config.getInst().getShowTimeRemaining().asBoolean()) {
                    double secondsRemaining = timeRemaining / 1000D;
                    itemEntity.setCustomName(ChatColor.YELLOW + String.format("%.1f0s", secondsRemaining));
                }
                if(crackerType.getTickSound()!=null) {
                    itemEntity.getWorld().playSound(itemEntity.getLocation(), crackerType.getTickSound(), 1f, 1f);
                }
            }
        }
        toRemove.forEach(ent->{
           partyCrackerEntities.remove(ent);
           ent.remove();
        });
    }

    public boolean isCrackerEntity(Item item) {
        return partyCrackerEntities.containsKey(item);
    }

    public void removeAllCrackers() {
        for(Item entity : partyCrackerEntities.keySet()) {
            entity.remove();
        }
        partyCrackerEntities.clear();
    }

    @Data
    @AllArgsConstructor
    class CrackerItemEntityData {
        private final PartyCracker cracker;
        private final long droppedOn;
    }
}
