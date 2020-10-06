package rip.skyland.soup.kit;

import rip.skyland.core.util.CC;
import rip.skyland.soup.profiles.SoupProfile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;

public abstract class Kit {

    public abstract String getName();
    public abstract String getDescription();
    public abstract ItemStack getIcon();
    public abstract CC getColor();
    public abstract boolean isAbilityKit();
    public abstract boolean isActualKit();

    public abstract ItemStack[] getArmor();
    public abstract ItemStack[] getItems();
    public abstract PotionEffect[] getPotionEffects();

    void onEquip(Player player) {
        player.sendMessage(CC.translate("&eYou have chosen the " + getColor() +  getName() + " &ekit."));

        this.onEquipNormal(player);
    }

    public void onEquipNormal(Player player) {
        player.getInventory().clear();

        player.getInventory().setArmorContents(getArmor());

        if(getItems() != null)
            player.getInventory().setContents(getItems());
        else
            player.getInventory().clear();

        player.getActivePotionEffects().forEach(ef -> player.removePotionEffect(ef.getType()));
        if(getPotionEffects() != null)
            Arrays.stream(getPotionEffects()).forEach(player::addPotionEffect);
        SoupProfile.getByPlayer(player).setKit(this);
        SoupProfile.getByPlayer(player).clearCooldowns();
    }

}
