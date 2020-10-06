package rip.skyland.soup.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class PotionUtil {

    public static ItemStack getPotionItemStack(PotionType type, int level, boolean extend, boolean splash, String displayName){
        Potion potion = new Potion(type, level, splash);

        if(extend)
            potion.setHasExtendedDuration(true);

        ItemStack itemstack = potion.toItemStack(1);
        ItemMeta meta = itemstack.getItemMeta();
        meta.setDisplayName(displayName);
        itemstack.setItemMeta(meta);
        return itemstack;
    }
}
