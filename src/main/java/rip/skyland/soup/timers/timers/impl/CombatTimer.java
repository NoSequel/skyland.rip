package rip.skyland.soup.timers.timers.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import rip.skyland.core.profile.Profile;
import rip.skyland.core.util.PlayerUtil;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.profiles.ProfileState;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.timers.PlayerTimer;
import rip.skyland.soup.util.Locale;

public class CombatTimer extends PlayerTimer implements Listener {

    public CombatTimer() {
        super(true, "Combat", 30000L);
    }

    public void onExpire(Player player) {}

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            if(!SoupProfile.getByPlayer(Bukkit.getPlayer(event.getDamager().getUniqueId())).getState().equals(ProfileState.PLAYING)) {
                return;
            }
        }

        if(event.getDamager() instanceof Arrow) {
            if(((Arrow) event.getDamager()).getShooter() instanceof Player && event.getEntity() instanceof Player) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(SoupPlugin.getInstance(), () -> {
                    setCooldown((Player) ((Arrow) event.getDamager()).getShooter());
                    setCooldown((Player) event.getEntity());
                }, 1L);

            }

            return;
        }

        if(!(event.getEntity() instanceof Player) || (!(event.getDamager() instanceof Player)))
            return;

        Player player = (Player) event.getEntity();

        if(PlayerUtil.inArea(player.getLocation(), Locale.SPAWN_LOCATION_1, Locale.SPAWN_LOCATION_2) || PlayerUtil.inArea(player.getLocation(), Locale.SPAWN_LOCATION_1, Locale.SPAWN_LOCATION_2)) {
            event.setCancelled(true);
            return;
        }

        if(event.isCancelled())
            return;


        Bukkit.getScheduler().scheduleSyncDelayedTask(SoupPlugin.getInstance(), () -> {
            setCooldown((Player) event.getDamager());
            setCooldown((Player) event.getEntity());
        }, 1L);
    }
}
