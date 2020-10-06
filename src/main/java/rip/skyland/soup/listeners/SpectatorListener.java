package rip.skyland.soup.listeners;

import rip.skyland.soup.profiles.SoupProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SpectatorListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            if(SoupProfile.getByPlayer((Player) event.getDamager()).isSpectating())
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        if(SoupProfile.getByPlayer((Player) event.getWhoClicked()).isSpectating())
            event.setCancelled(true);
    }

}
