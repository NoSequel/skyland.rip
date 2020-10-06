package rip.skyland.soup.kit.impl.ability;

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
public class WarriorKit extends Kit {

    private boolean abilityKit;
    private String name, description;
    private ItemStack icon;
    private ItemStack[] armor, items;
    private PotionEffect[] potionEffects;
    private CC color;
    private boolean actualKit;

    public WarriorKit() {
        this.actualKit = true;

        abilityKit = true;
        this.name = "Warrior";
        this.description = "Fast and stealthy with a:lightning strike ability.";
        this.icon = new ItemStack(Material.GLOWSTONE_DUST);
        this.color = CC.PINK;

        this.armor = getArmor();

        ItemStack sword = new ItemStack(Material.WOOD_SWORD);
        sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
        sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        ItemStack armorHelm = new ItemStack(Material.LEATHER_HELMET);
        ItemStack armorChest = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack armorLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack armorBoots = new ItemStack(Material.LEATHER_BOOTS);

        Arrays.asList(armorHelm, armorChest, armorLeggings, armorBoots).forEach(item -> {
            item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 20);
        });

        this.armor = new ItemStack[] {
                armorBoots,
                armorLeggings,
                armorChest,
                armorHelm
        };

        this.items = new ItemStack[] {
                sword,
                new ItemBuilder(Material.GLOWSTONE_DUST).displayName("&9Lightning Strike").lore(Arrays.asList("&6Right-click to call down lightning.", "&6launching your enemies into! the air")).create(),
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
                new ItemBuilder(Material.MUSHROOM_SOUP).create()
        };

        this.potionEffects = new PotionEffect[] {
                new PotionEffect(PotionEffectType.SPEED, 2000000000, 1),
                new PotionEffect(PotionEffectType.FAST_DIGGING, 200000000, 1),
                new PotionEffect(PotionEffectType.INVISIBILITY, 200000000, 0),
        };
    }


}
