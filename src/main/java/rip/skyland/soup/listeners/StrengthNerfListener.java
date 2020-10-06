package rip.skyland.soup.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StrengthNerfListener implements Listener {

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            boolean b = false;
            for(PotionEffect potion : ((Player) event.getDamager()).getActivePotionEffects()) {
                if(potion.getType().equals(PotionEffectType.INCREASE_DAMAGE) && potion.getAmplifier() == 1)
                    b = true;
            }

            if(b)
                event.setDamage(event.getDamage()/1.9);
        }
    }

}
