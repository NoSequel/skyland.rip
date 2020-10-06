package rip.skyland.soup.timers.abilities.barbarian;

import net.minecraft.server.v1_7_R4.EntitySilverfish;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftSilverfish;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import rip.skyland.core.util.CC;
import rip.skyland.phase.PhaseSupplier;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.kit.KitManager;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.timers.PlayerTimer;
import rip.skyland.soup.timers.abilities.event.AbilityUseEvent;
import rip.skyland.soup.util.BlockUtil;
import rip.skyland.soup.util.XPBarTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.IntStream;

import static rip.skyland.soup.util.BlockUtil.addEntity;

public class BarbarianAbility extends PlayerTimer implements Listener {


    public BarbarianAbility() {
        super(false, "Barbarian", 35000L);
    }

    public void onExpire(Player player) {
        player.sendMessage(CC.translate("&eYou may now use &dSilverfish Swarm&e."));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (SoupProfile.getByPlayer(event.getPlayer()).getKit() != null && SoupProfile.getByPlayer(event.getPlayer()).getKit().equals(KitManager.getByName("Barbarian")) && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().equals(Material.INK_SACK)) {

            if(hasCooldown(event.getPlayer())) {
                event.getPlayer().sendMessage(CC.translate("&cYou can't use this for another &e" + PhaseSupplier.getRemainingTime(getRemaining(event.getPlayer()), true) + '.'));
                return;

            }

            AbilityUseEvent event1 = new AbilityUseEvent(event.getPlayer(), this);
            Bukkit.getPluginManager().callEvent(event1);

            if (!event1.isCancelled()) {
                Player player = event.getPlayer();
                List<Player> nearbyPlayers = new ArrayList<>();
                SoupProfile profile = SoupProfile.getByPlayer(player);

                if(profile.getCurrentMatch() == null) {
                    player.getWorld().getEntities().stream().filter(entity -> entity instanceof Player).forEach(entity -> nearbyPlayers.add((Player) entity));

                    Comparator<Entity> comparator = Comparator.comparing(entity -> player.getLocation().distance(entity.getLocation()));
                    nearbyPlayers.sort(comparator);
                } else {
                    nearbyPlayers.add(profile.getCurrentMatch().getOpponent(player.getUniqueId()));
                }

                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 7*20, 1));
                IntStream.range(0, 6).forEach(i -> {


                    EntitySilverfish silverfish = new EntitySilverfish(((CraftWorld) player.getWorld()).getHandle());
                    CraftSilverfish silverfish2 = new CraftSilverfish(((CraftServer) Bukkit.getServer()), silverfish);

                    silverfish.setLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), 0.0F, 0.0F);

                    if(!nearbyPlayers.isEmpty())
                        silverfish.setTarget((((CraftPlayer) nearbyPlayers.get(0)).getHandle()));

                    silverfish.setSprinting(true);
                    silverfish.setAbsorptionHearts(silverfish.getMaxHealth()*5);

                    if(profile.getTamed() == null)
                        profile.setTamed(new ArrayList<>());
                    profile.getTamed().add(silverfish2);

                    addEntity(((CraftWorld) player.getWorld()).getHandle(), silverfish, CreatureSpawnEvent.SpawnReason.CUSTOM);
                    Bukkit.getScheduler().runTaskTimer(SoupPlugin.getInstance(), () -> {
                        if(silverfish.getGoalTarget() != null && silverfish.getGoalTarget().equals(player))
                            silverfish.setTarget(null);
                    }, 10L, 10L);

                    Bukkit.getScheduler().runTaskLater(SoupPlugin.getInstance(), () -> BlockUtil.removeEntity(((CraftWorld) player.getWorld()).getHandle(), silverfish), 140L);
                });


                XPBarTimer.runXpBar(player, 35);

                setCooldown(player);
            }
        }
    }

    @EventHandler
    public void onFireball(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Silverfish && event.getEntity() instanceof Player) {
            event.setDamage(EntityDamageEvent.DamageModifier.BASE, 4);
        }
    }


}
