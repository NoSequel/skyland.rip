package rip.skyland.soup.kit.impl;

import rip.skyland.core.util.CC;
import rip.skyland.core.util.ItemBuilder;
import rip.skyland.soup.kit.Kit;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ArcherKit extends Kit {

    private boolean abilityKit;
    private String name, description;
    private ItemStack icon;
    private ItemStack[] armor, items;
    private PotionEffect[] potionEffects;
    private CC color;
    private boolean actualKit;

    public ArcherKit() {
        this.actualKit = true;

        abilityKit = false;
        this.name = "Archer";
        this.description = "Special bow that deals scaling :damage based on distance!:Gain +10 armor durability per kill!";
        this.icon = new ItemStack(Material.BOW);
        this.color = CC.PINK;

        this.armor = getArmor();
        this.items = getItems();

        this.potionEffects = new PotionEffect[] {
                new PotionEffect(PotionEffectType.SPEED, 10000000,1),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000000, 0),
        };

    }
    public ItemStack[] getArmor() {
        ItemStack helm = new ItemBuilder(Material.LEATHER_HELMET).create();
        ItemStack chest = new ItemBuilder(Material.LEATHER_CHESTPLATE).create();
        ItemStack leg = new ItemBuilder(Material.LEATHER_LEGGINGS).create();
        ItemStack boot = new ItemBuilder(Material.LEATHER_BOOTS).create();

        Map<Enchantment, Integer> encs = new HashMap<>();
        encs.put(Enchantment.DURABILITY, 3);
        encs.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        encs.keySet().forEach(enc -> {
            helm.addEnchantment(enc, encs.get(enc));
            chest.addEnchantment(enc, encs.get(enc));
            leg.addEnchantment(enc, encs.get(enc));
            boot.addEnchantment(enc, encs.get(enc));
        });

        return new ItemStack[] {
            boot, leg, chest, helm,
        };
    }
    public ItemStack[] getItems() {
        ItemStack sword = new ItemBuilder(Material.WOOD_SWORD).create();
        ItemStack bow = new ItemStack(Material.BOW);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addEnchantment(Enchantment.DURABILITY, 3);


        return new ItemStack[] {
                sword,
                bow,
                new ItemStack(Material.ENDER_PEARL, 3),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.ARROW).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
        };
    }
}
