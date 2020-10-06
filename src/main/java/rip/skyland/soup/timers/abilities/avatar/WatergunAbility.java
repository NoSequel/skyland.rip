package rip.skyland.soup.timers.abilities.avatar;

import rip.skyland.core.CorePlugin;
import rip.skyland.core.util.CC;
import rip.skyland.phase.PhaseSupplier;
import rip.skyland.soup.kit.KitManager;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.timers.PlayerTimer;
import rip.skyland.soup.timers.abilities.event.AbilityUseEvent;
import rip.skyland.soup.util.XPBarTimer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

@Getter
public class WatergunAbility extends PlayerTimer implements Listener {


    public WatergunAbility() {
        super(false, "Warrior", 35000L);
    }

    public void onExpire(Player player) {
        player.sendMessage(CC.translate("&eYou may now use &dWater Gun&e."));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (SoupProfile.getByPlayer(event.getPlayer()).getKit() != null && SoupProfile.getByPlayer(event.getPlayer()).getKit().equals(KitManager.getByName("Avatar")) && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getItemMeta() != null && event.getPlayer().getItemInHand().getType().equals(Material.INK_SACK)) {
            if (hasCooldown(event.getPlayer())) {
                event.getPlayer().sendMessage(CC.translate("&cYou can't use this for another &e" + PhaseSupplier.getRemainingTime(getRemaining(event.getPlayer()), true) + '.'));
                return;
            }

            Player player = event.getPlayer();
            AbilityUseEvent event1 = new AbilityUseEvent(player, this);
            Bukkit.getPluginManager().callEvent(event1);

            if(!event1.isCancelled()) {

                Location eyeLocation = player.getEyeLocation();
                Location loc = eyeLocation.add(player.getEyeLocation().getDirection().multiply(1.3));
                loc.setY(loc.getY()+1.4);

                player.getEyeLocation().getDirection();

                FallingBlock block = player.getWorld().spawnFallingBlock(loc, Material.WATER, (byte) 6969);
                block.setVelocity(loc.getDirection().normalize().multiply(2));

                SoupProfile.getByPlayer(player).setAvatarWatergunInvincibility(true);
                Bukkit.getScheduler().runTaskLater(CorePlugin.getInstance(), () -> SoupProfile.getByPlayer(player).setAvatarWatergunInvincibility(false), 10L);

                setCooldown(player);
                XPBarTimer.runXpBar(player, 35);
            }
        }
    }
}
