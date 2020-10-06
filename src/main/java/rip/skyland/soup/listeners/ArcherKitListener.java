package rip.skyland.soup.listeners;


import rip.skyland.core.CorePlugin;
import rip.skyland.core.util.CC;
import rip.skyland.practice.match.impl.SumoMatch;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.event.impl.SumoEvent;
import rip.skyland.soup.profiles.ProfileState;
import rip.skyland.soup.profiles.SoupProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.text.DecimalFormat;

public class ArcherKitListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if(event.getEntity() instanceof Player) {


            Player player = (Player) event.getEntity();
            if(SoupProfile.getByPlayer(player).getTamed().contains(event.getDamager())) {
                event.setCancelled(true);
                return;
            }

            if(SoupProfile.getByPlayer(player).getCurrentMatch() != null && SoupProfile.getByPlayer(player).getCurrentMatch() instanceof SumoMatch)
                Bukkit.getScheduler().runTaskLaterAsynchronously(CorePlugin.getInstance(), () -> player.setHealth(player.getMaxHealth()), 2L); // idc intellij shut the fuck up

            if(SoupProfile.getByPlayer(player).getCurrentMatch() != null && SoupProfile.getByPlayer(player).getCurrentMatch().isEnded())
                event.setCancelled(true);

            if(event.getDamager() instanceof Player) {
                if (SoupProfile.getByPlayer(player).getState().equals(ProfileState.ONE_VS_ONE) || SoupProfile.getByPlayer((Player) event.getDamager()).getState().equals(ProfileState.ONE_VS_ONE)) {
                    if (SoupProfile.getByPlayer(player).getCurrentMatch() == null || SoupProfile.getByPlayer((Player) event.getDamager()).getCurrentMatch() == null)
                        event.setCancelled(true);
                    else if (!SoupProfile.getByPlayer(player).getCurrentMatch().isStarted())
                        event.setCancelled(true);
                    else if(!SoupProfile.getByPlayer(player).getCurrentMatch().getOpponent(player.getUniqueId()).equals(event.getDamager()))
                        event.setCancelled(true);
                }
            }

            if(SoupPlugin.getInstance().getEventManager().isBusy() && SoupPlugin.getInstance().getEventManager().getCurrentEvent() instanceof SumoEvent) {
                SumoEvent e = (SumoEvent) SoupPlugin.getInstance().getEventManager().getCurrentEvent();
                if(e.getPlayers().contains(player)) {
                    if(!e.getFighting().contains(player))
                        event.setCancelled(true);
                    else
                        Bukkit.getScheduler().runTaskLaterAsynchronously(CorePlugin.getInstance(), () -> player.setHealth(player.getMaxHealth()), 2L);
                }
            }
        }
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();

            Player player = (Player) arrow.getShooter();
            Player damaged = (Player) event.getEntity();

            SoupProfile d = SoupProfile.getByPlayer(damaged);

            event.setDamage(event.getDamage()*player.getLocation().distance(damaged.getLocation())/6.3);

            DecimalFormat health = new DecimalFormat("##.#");
            String h = health.format(((Player) event.getEntity()).getHealth());
            String hDone = health.format(event.getFinalDamage());

            player.sendMessage(CC.translate(d.getProfile().getDisplayName() + "&6's health: &c" + h + "&4❤ &7(&eYou did &c" + hDone + "&4❤&7)"));
        }
    }
}
