package dev.martinl.betterpartycrackers.data;

import dev.martinl.betterpartycrackers.BetterPartyCrackers;
import dev.martinl.betterpartycrackers.util.ItemBuilder;
import dev.martinl.betterpartycrackers.util.SerializerUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
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
    public Material material;
    public boolean shiny;
    public double detonationSeconds;
    @SerializeEnumListAsStringList(enumType = Sound.class)
    public List<Sound> possibleSounds = new ArrayList<>();

    public static PartyCracker fromSerializedData(Map<?, ?> data) {
        return SerializerUtil.createPartyCrackerFromSerializedData(data);
    }

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

    public Map<String, Object> serialize() {
        return SerializerUtil.serializePartyCracker(this);
    }
}
