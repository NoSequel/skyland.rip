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

import java.util.Arrays;

@Getter
public class FishermanKit extends Kit {

    private boolean abilityKit;
    private String name, description;
    private ItemStack icon;
    private ItemStack[] armor, items;
    private PotionEffect[] potionEffects;
    private CC color;
    private boolean actualKit;

    public FishermanKit() {
        this.abilityKit = false;
        this.actualKit = true;
        this.name = "Fisherman";
        this.description = "Special fishing rod that:teleports players to you!";
        this.color = CC.PINK;
        icon = new ItemStack(Material.FISHING_ROD);

        ItemStack helmet = new ItemStack(Material.GOLD_HELMET), boots = new ItemStack(Material.GOLD_BOOTS);

        Arrays.asList(helmet, boots).forEach(item -> {
            item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
            item.addEnchantment(Enchantment.DURABILITY, 3);
        });

        this.armor = new ItemStack[] {
                boots,
                new ItemStack(Material.IRON_LEGGINGS),
                new ItemStack(Material.IRON_CHESTPLATE),
                helmet
        };

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);

        this.items = new ItemStack[] {
                sword,
                new ItemBuilder(Material.FISHING_ROD).displayName("&9Fishing Rod").create(),
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
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).create(),
        };

        this.potionEffects = new PotionEffect[] {
                new PotionEffect(PotionEffectType.SPEED, 200000000, 1)
        };

    }

}
