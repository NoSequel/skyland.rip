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
import rip.skyland.soup.util.PotionUtil;

@Getter
public class BarbarianKit extends Kit {

    private boolean abilityKit;
    private String name, description;
    private ItemStack icon;
    private ItemStack[] armor, items;
    private PotionEffect[] potionEffects;
    private CC color;
    private boolean actualKit;

    public BarbarianKit() {
        this.actualKit = true;

        abilityKit = true;
        this.name = "Barbarian";
        this.description = "Surround your enemies with:a swarm of silverfish.";
        this.icon = new ItemStack(Material.STONE_SWORD, 1);
        this.color = CC.PINK;

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);


        this.armor = null;

        ItemStack silverfish = new ItemStack(Material.INK_SACK, 1, (byte) 6);

        this.items = new ItemStack[]{
                sword,
                silverfish,
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
                new ItemBuilder(Material.MUSHROOM_SOUP).create()
        };


        this.potionEffects = new PotionEffect[]{
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2000000000, 2),
        };
    }
}
