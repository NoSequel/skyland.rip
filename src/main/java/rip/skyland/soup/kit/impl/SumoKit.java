package rip.skyland.soup.kit.impl;

import rip.skyland.core.util.CC;
import rip.skyland.soup.kit.Kit;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

@Getter
public class SumoKit extends Kit {

    private boolean abilityKit;
    private String name, description;
    private ItemStack icon;
    private ItemStack[] armor, items;
    private PotionEffect[] potionEffects;
    private CC color;
    private boolean actualKit;

    public SumoKit() {
        this.actualKit = false;
        this.abilityKit = false;
        this.name = "Sumo";
        this.description = "";
        this.icon = new ItemStack(Material.STICK);
        this.armor = null;
        this.items = null;
        this.potionEffects = null;
        this.color = CC.PINK;
    }

}
