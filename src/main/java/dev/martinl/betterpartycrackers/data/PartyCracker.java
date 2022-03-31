package dev.martinl.betterpartycrackers.data;

import dev.martinl.betterpartycrackers.BetterPartyCrackers;
import dev.martinl.betterpartycrackers.util.ItemBuilder;
import dev.martinl.betterpartycrackers.util.SerializerUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartyCracker {
    public String id = "example_cracker";
    public String name;
    public List<String> lore;
    @SerializeEnumAsString(enumType = Material.class)
    public Material material = Material.FIREWORK_ROCKET;
    public boolean shiny;
    public int detonationTime = 3500;
    @SerializeEnumAsString(enumType = Sound.class)
    public Sound tickSound;
    @SerializeEnumListAsStringList(enumType = Sound.class)
    public List<Sound> explosionSounds = new ArrayList<>();
    @SerializeEnumListAsStringList(enumType = Particle.class)
    public List<Particle> particleEffects = new ArrayList<>();
    public boolean spawnFirework = false;
    @RewardList
    public List<PartyCrackerReward> rewards = new ArrayList<>();


    public ItemStack buildItem() {
        return buildItem(1);
    }

    public ItemStack buildItem(int amount) {
        ItemStack result = ItemBuilder.with(material).setNameFormatted(name).setLoreFormatted(lore).setShiny(shiny).setAmount(amount).build();
        ItemMeta itemMeta = result.getItemMeta();
        assert itemMeta != null;
        itemMeta.getPersistentDataContainer().set(BetterPartyCrackers.getPlugin().getNamespacedKey(), PersistentDataType.STRING, id);
        result.setItemMeta(itemMeta);
        return result;
    }

    public ItemStack getRandomReward() {
        if(rewards.size()==0) return null;
        double totalWeight = 0.0;
        for (PartyCrackerReward reward : rewards) {
            totalWeight += reward.getChance();
        }
        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < rewards.size() - 1; ++idx) {
            r -= rewards.get(idx).getChance();
            if (r <= 0.0) break;
        }
        PartyCrackerReward chosenReward = rewards.get(idx);
        return new ItemBuilder(chosenReward.getMaterial()).setAmount(chosenReward.getAmount()).build();
    }

    public static PartyCracker fromSerializedData(Map<?, ?> data) {
        return SerializerUtil.deserialize(new PartyCracker(), data);
    }

    public Map<String, Object> serialize() {
        return SerializerUtil.serialize(this);
    }
}
