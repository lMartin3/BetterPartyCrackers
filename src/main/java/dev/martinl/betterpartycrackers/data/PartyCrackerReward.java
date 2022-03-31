package dev.martinl.betterpartycrackers.data;

import dev.martinl.betterpartycrackers.data.serialization.SerializeEnumAsString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartyCrackerReward {
    @SerializeEnumAsString(enumType = Material.class)
    public Material material;
    public int amount;
    public int chance;

}
