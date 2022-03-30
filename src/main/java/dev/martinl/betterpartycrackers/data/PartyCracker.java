package dev.martinl.betterpartycrackers.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartyCracker {
    private String id;
    private String name;
    private Material material;
    private List<Sound> possibleSounds;



    public Map<String, Object> serialize() {
        Map<String, Object> mappedValues = new LinkedHashMap<>();
        mappedValues.put("id", id);
        mappedValues.put("name", name);
        mappedValues.put("material", material.toString());
        mappedValues.put("possible_sounds", possibleSounds.stream().map(Sound::toString).toList());
        return mappedValues;
    }

    @SuppressWarnings("unchecked")
    public static PartyCracker fromSerializedData(Map<?, ?> yamlMap) {
        String name = (String) yamlMap.get("name");
        String id = (String) yamlMap.get("id");
        Material material = Material.valueOf((String) yamlMap.get("material"));
        List<Sound> possibleSounds = ((List<String>) yamlMap.get("possible_sounds")).stream().map(Sound::valueOf).toList();

        return new PartyCracker(id, name, material, possibleSounds);
    }
}
