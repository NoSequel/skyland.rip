package rip.skyland.soup.kit.impl.ability;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.ItemBuilder;
import rip.skyland.soup.kit.Kit;

import java.util.Arrays;
import java.util.Collections;

@Getter
public class ChemistKit extends Kit {

    private boolean abilityKit;
    private String name, description;
    private ItemStack icon;
    private ItemStack[] armor, items;
    private PotionEffect[] potionEffects;
    private CC color;
    private boolean actualKit;

    public ChemistKit() {
        this.actualKit = true;
        abilityKit = true;

        this.name = "Chemist";
        this.description = "Slow down your enemies with poison or:take them down with damage pots that:refill upon kills.";
        this.icon = new ItemStack(Material.POTION, 1, (byte) 44);
        this.color = CC.PINK;

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.DURABILITY, 3);

        ItemStack armorHelm = new ItemStack(Material.CHAINMAIL_HELMET);
        ItemStack armorChest = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack armorLeggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemStack armorBoots = new ItemStack(Material.CHAINMAIL_BOOTS);

        Arrays.asList(armorHelm, armorChest, armorLeggings, armorBoots).forEach(item ->
            item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        );

        this.armor = new ItemStack[]{
                armorBoots,
                armorLeggings,
                armorChest,
                armorHelm
        };


        ItemStack harm = new ItemStack(373, 4, (byte) 16428);
        ItemStack poison = new ItemStack(373, 4, (byte) 16420);
        ItemStack egg = new ItemStack(Material.MONSTER_EGG);

        this.items = new ItemStack[]{
                sword,
                harm,
                poison,
                new ItemBuilder(egg).displayName("&9Smoke Bomb").create(),
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


        this.potionEffects = new PotionEffect[]{
                new PotionEffect(PotionEffectType.SPEED, 2000000000, 0),
        };
    }

}
