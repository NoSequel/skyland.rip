package rip.skyland.soup.listeners;

import rip.skyland.core.CorePlugin;
import rip.skyland.core.util.CC;
import rip.skyland.phase.PhaseSupplier;
import rip.skyland.practice.match.impl.SumoMatch;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.event.Event;
import rip.skyland.soup.event.impl.SumoEvent;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.timers.abilities.avatar.JumpAbility;

import rip.skyland.soup.timers.abilities.event.AbilityUseEvent;
import rip.skyland.soup.util.BlockUtil;
import rip.skyland.soup.util.XPBarTimer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @see org.bukkit.event.player.PlayerMoveEvent
 *
 * Used instead of PlayerMoveEvent because it just causes overload.
 * Note that this is an async thread, so you can't teleport players.
 *
 */

public class MoveListener {

    /**
     * Task caller
     *
     * @param player Check player's movement
     */

    static void checkPlayerMove(Player player) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(CorePlugin.getInstance(), () -> {
            if (player != null && SoupProfile.getByPlayer(player) != null) {

                SoupProfile profile = SoupProfile.getByPlayer(player);
                checkAvatar(profile);
                checkSumo(player);

                profile.setLastLocation(player.getLocation());

            }
        }, 5L, 5L);
    }


    /**
     * Checks for the avatar ability
     *
     * @param profile Check player's movement
     */

    private static void checkAvatar(SoupProfile profile) {
        if (profile == null) {
            return;
        }

        if (profile.getLastLocation() != null && SoupPlugin.getInstance().getTimerManager().getSpawnWarpTimer().hasCooldown(profile.getPlayer())) {
            if (profile.getPlayer().getLocation().distance(profile.getLastLocation()) > 0) {
                profile.getPlayer().sendMessage(CC.translate("&cYour teleportation has been cancelled because you moved."));
                SoupPlugin.getInstance().getTimerManager().getSpawnWarpTimer().removeCooldown(profile.getPlayer(), false);
            }
        }


        if (profile.isAvatarParticles() && !profile.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.AIR))
            profile.setAvatarParticles(false);

        Player player = profile.getPlayer();
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            if (profile.getKit() == null || !profile.getKit().getName().equals("Avatar"))
                return;

            JumpAbility ability1 = SoupPlugin.getInstance().getTimerManager().getJumpAbility();


            if (player.isSneaking()) {
                AbilityUseEvent event1 = new AbilityUseEvent(player, ability1);
                Bukkit.getPluginManager().callEvent(event1);
                if (!event1.isCancelled()) {

                    if (ability1.hasCooldown(player)) {
                        player.sendMessage(CC.translate("&cYou can't use this for another &e" + PhaseSupplier.getRemainingTime(ability1.getRemaining(player), true) + '.'));

                        return;
                    }


                    profile.setAvatarParticles(true);

                    ability1.setCooldown(player);
                    XPBarTimer.runXpBar(player, 10);

                    player.setVelocity(player.getLocation().getDirection().normalize().multiply(1.9));
                    player.playSound(player.getLocation(), Sound.FIREWORK_LARGE_BLAST2, 5F, 1F);

                    new BukkitRunnable() {
                        public void run() {
                            if (!profile.isAvatarParticles())
                                this.cancel();
                            else {
                                BlockUtil.sendFlameParticles(player);
                                player.getNearbyEntities(1, 1, 1)
                                        .stream().filter(entity -> entity instanceof Player)
                                        .forEach(player ->
                                                player.setFireTicks(160)
                                        );
                            }
                        }
                    }.runTaskTimer(SoupPlugin.getInstance(), 2L, 2L);
                }
            }
        }
    }


    private static void checkSumo(Player player) {
        if (SoupProfile.getByPlayer(player) == null) return;

        SoupProfile profile = SoupProfile.getByPlayer(player);

        if (profile.getCurrentMatch() != null && profile.getCurrentMatch() instanceof SumoMatch) {

            if(player.getLocation().getBlock().isLiquid()) {
                profile.getCurrentMatch().handleDeath(player.getUniqueId(), false);
                return;
            }

        }

        if (SoupPlugin.getInstance().getEventManager().getCurrentEvent() != null) {

            Event event = SoupPlugin.getInstance().getEventManager().getCurrentEvent();
            if (event.getPlayers().contains(player)) {
                if (event instanceof SumoEvent) {
                    if (((SumoEvent) event).getFighting().contains(player) && player.getLocation().getBlock().isLiquid())
                        ((SumoEvent) event).handleDeath(player);
                }
            }
        }
    }

}
