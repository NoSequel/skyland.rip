package rip.skyland.soup.kit.impl.ability;

import rip.skyland.core.util.CC;
import rip.skyland.core.util.ItemBuilder;
import rip.skyland.soup.kit.Kit;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;

@Getter
public class KidnapperKit extends Kit {

    private boolean abilityKit;
    private String name, description;
    private ItemStack icon;
    private ItemStack[] armor, items;
    private PotionEffect[] potionEffects;
    private CC color;
    private boolean actualKit;

    public KidnapperKit() {
        this.actualKit = true;

        abilityKit = true;
        this.name = "Kidnapper";
        this.description = "Create a sphere of ice around you:and get strength two for 5 seconds:Gain +10 armor durability per kill!";
        this.icon = new ItemStack(Material.TRIPWIRE_HOOK);
        this.color = CC.PINK;

        this.armor = getArmor();

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.DURABILITY, 3);

        ItemStack armorHelm = new ItemStack(Material.LEATHER_HELMET);
        ItemStack armorChest = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack armorLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack armorBoots = new ItemStack(Material.LEATHER_BOOTS);

        Arrays.asList(armorHelm, armorChest, armorLeggings, armorBoots).forEach(item -> {
            item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
            ItemMeta itemMeta = item.getItemMeta();

            ((LeatherArmorMeta) itemMeta).setColor(Color.fromRGB(0x800080));

            item.setItemMeta(itemMeta);
        });

        this.armor = new ItemStack[] {
                armorBoots,
                armorLeggings,
                armorChest,
                armorHelm
        };


        this.items = new ItemStack[]{
                sword,
                new ItemBuilder(Material.TRIPWIRE_HOOK).displayName("&dFluffy Handcuffs").lore(Collections.singletonList("&7Right-click to spawn a sphere of ice around you.")).create(),
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


        this.potionEffects = new PotionEffect[]{
                new PotionEffect(PotionEffectType.SPEED, 2000000000, 0),
        };
    }
}
