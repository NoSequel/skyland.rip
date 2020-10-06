package rip.skyland.soup.kit.impl.ability;

import rip.skyland.core.util.CC;
import rip.skyland.core.util.ItemBuilder;
import rip.skyland.soup.kit.Kit;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
public class AvatarKit extends Kit {

    private boolean abilityKit;
    private String name, description;
    private ItemStack icon;
    private ItemStack[] armor, items;
    private PotionEffect[] potionEffects;
    private CC color;
    private boolean actualKit;

    public AvatarKit() {
        this.actualKit = true;

        abilityKit = true;
        this.name = "Avatar";
        this.description = "Incredible mobile class with:a stunning ability.";
        this.icon = new ItemStack(Material.BEACON);
        this.color = CC.PINK;

        this.armor = getArmor();
        this.items = getItems();

        this.potionEffects = new PotionEffect[] {
                new PotionEffect(PotionEffectType.SPEED, 2000000000, 1),
        };

    }

    public ItemStack[] getArmor() {
        return new ItemStack[] {
                new ItemBuilder(Material.IRON_BOOTS).create(),
                new ItemBuilder(Material.CHAINMAIL_LEGGINGS).create(),
                new ItemBuilder(Material.IRON_CHESTPLATE).create(),
                new ItemBuilder(Material.DIAMOND_HELMET).create()
        };
    }
    public ItemStack[] getItems() {
        ItemStack dye = new ItemStack(Material.INK_SACK, 1, (short) 12);
        ItemMeta meta = dye.getItemMeta();
        meta.setDisplayName(CC.translate("&bWater Gun"));
        dye.setItemMeta(meta);

        return new ItemStack[] {
                new ItemBuilder(Material.DIAMOND_SWORD).create(),
                dye,
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
    }

}
