package rip.skyland.soup.timers.abilities;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.kit.KitManager;
import rip.skyland.soup.profiles.SoupProfile;

import rip.skyland.soup.timers.PlayerTimer;
import rip.skyland.soup.timers.abilities.event.AbilityUseEvent;
import rip.skyland.soup.util.BlockUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.scheduler.BukkitRunnable;

public class MarioAbility extends PlayerTimer implements Listener {

    public MarioAbility() {
        super(false, "Mario", 35000L);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (SoupProfile.getByPlayer(event.getPlayer()).getKit() != null && SoupProfile.getByPlayer(event.getPlayer()).getKit().equals(KitManager.getByName("Mario")) && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().equals(Material.FIREBALL)) {
            AbilityUseEvent event1 = new AbilityUseEvent(event.getPlayer(), this);
            Bukkit.getPluginManager().callEvent(event1);

            if (!event1.isCancelled()) {
                Player player = event.getPlayer();

                if(player.getItemInHand().getAmount() >= 2)
                    player.getItemInHand().setAmount(player.getItemInHand().getAmount()-1);
                else
                    player.getInventory().remove(Material.FIREBALL);

                Fireball fireball = player.launchProjectile(Fireball.class);
                fireball.setIsIncendiary(false);
                fireball.setBounce(false);
                fireball.setVelocity(player.getLocation().getDirection().normalize().multiply(2.3));

                new BukkitRunnable() {

                    public void run() {
                        if(fireball.isDead())
                            this.cancel();
                        else
                            BlockUtil.sendFlameParticles(fireball);
                    }
                }.runTaskTimer(SoupPlugin.getInstance(), 2L, 2L);
            }
        }
    }

    @EventHandler
    public void onFireball(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Fireball && event.getEntity() instanceof Player) {
            event.setDamage(EntityDamageEvent.DamageModifier.BASE, 5);
        }
    }

    @EventHandler
    public void onDestroy(EntityExplodeEvent event) {
        if(event.getEntity() instanceof Fireball) {
            event.setCancelled(true);
        }
    }

}
