package dev.martinl.betterpartycrackers.util;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ItemBuilder {
    private static final List<Material> LEATHER_ITEMS = List.of(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS);
    Material material;
    ItemMeta itemMeta;
    String name = " ";
    List<String> lore = new ArrayList<>();
    int amount = 1;
    int durability = 0;
    boolean unbreakable = false;
    int modelData = 0;
    List<Enchantment> enchantments = new ArrayList<>();
    Color color;
    boolean shiny = false;

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public ItemBuilder(ItemStack is) {
        this.material = is.getType();
        this.itemMeta = is.getItemMeta();
        this.durability = (is.getItemMeta() instanceof Damageable ? ((Damageable) is.getItemMeta()).getDamage() : 0);
        if (itemMeta.hasDisplayName()) {
            itemMeta.getDisplayName();
            this.name = itemMeta.getDisplayName();
        }
        if (itemMeta.hasLore() && itemMeta.getLore() != null) {
            this.lore = itemMeta.getLore();
        }
    }

    public static ItemBuilder with(Material material) {
        return new ItemBuilder(material);
    }


    public ItemBuilder setFlags(ItemFlag... flags) {
        if (itemMeta == null) itemMeta = new ItemStack(material).getItemMeta();
        if (itemMeta == null) return this;
        this.itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment) {
        if (itemMeta == null) itemMeta = new ItemStack(material).getItemMeta();
        if (itemMeta == null) return this;
        this.itemMeta.addEnchant(enchantment, 1, true);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        if (itemMeta == null) itemMeta = new ItemStack(material).getItemMeta();
        if (itemMeta == null) return this;
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemBuilder setMaterial(Material m) {
        this.material = m;
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder setNameFormatted(String name) {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        this.lore.add(line);
        return this;
    }

    public ItemBuilder addLoreLineFormatted(String line) {
        this.lore.add(ChatColor.translateAlternateColorCodes('&', line));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder setLoreFormatted(List<String> lore) {
        this.lore = lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).toList();
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setMeta(ItemMeta im) {
        this.itemMeta = im;
        return this;
    }

    public ItemBuilder setDurability(int durability) {
        this.durability = durability;
        return this;
    }

    public ItemBuilder setModelData(int modelData) {
        this.modelData = modelData;
        return this;
    }

    public ItemBuilder setDyeColor(Color color) {
        this.color = color;
        return this;
    }

    public ItemBuilder setShiny(boolean shiny) {
        this.shiny = shiny;
        return this;
    }

    public ItemStack build() {
        ItemStack result = new ItemStack(material);
        result.setAmount(amount);
        if (this.itemMeta != null) {
            result.setItemMeta(itemMeta);
        }
        ItemMeta resultMeta = result.getItemMeta();
        if (resultMeta != null) {
            if (resultMeta instanceof Damageable dmgMeta) {
                dmgMeta.setDamage(durability);
            }
            resultMeta.setDisplayName(name);
            resultMeta.setLore(lore);
            resultMeta.setUnbreakable(unbreakable);
            if (modelData != 0) resultMeta.setCustomModelData(modelData);
            if (LEATHER_ITEMS.contains(material)) {
                LeatherArmorMeta lam = (LeatherArmorMeta) resultMeta;
                lam.setColor(color);
            }
            if (shiny) {
                resultMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                resultMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            result.setItemMeta(resultMeta);
        }
        return result;
    }

    public Map<String, Object> serialized() {
        return build().serialize();
    }
}