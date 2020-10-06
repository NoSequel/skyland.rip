package rip.skyland.practice.kits;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.ItemBuilder;
import rip.skyland.soup.kit.Kit;
import rip.skyland.soup.util.PotionUtil;

import java.util.Arrays;

@Getter
@Setter
public class NoDebuffKit extends Kit {

    private boolean abilityKit;
    private String name, description;
    private ItemStack icon;
    private ItemStack[] armor, items;
    private PotionEffect[] potionEffects;
    private CC color;
    private boolean actualKit;

    public NoDebuffKit() {
        this.abilityKit = false;
        this.name = "NoDebuff";
        this.description = "";
        this.icon = PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, "&dNoDebuff");
        this.potionEffects = new PotionEffect[]{
                new PotionEffect(PotionEffectType.SPEED, 10000000, 1),
                new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 10000000, 0)
        };
        this.color = CC.PINK;
        this.actualKit = false;

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);

        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);

        Arrays.asList(boots, leggings, chestplate, helmet).forEach(item -> {
            item.addEnchantment(Enchantment.DURABILITY, 3);
            item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

            if(item.getType().equals(Material.DIAMOND_BOOTS))
                 item.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        });

        this.armor = new ItemStack[]{
                boots,
                leggings,
                chestplate,
                helmet
        };

        this.items = new ItemStack[]{
                sword,
                new ItemBuilder(Material.ENDER_PEARL).amount(16).create(),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""), PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
                PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 2, false, true, ""),
        };

    }
}
