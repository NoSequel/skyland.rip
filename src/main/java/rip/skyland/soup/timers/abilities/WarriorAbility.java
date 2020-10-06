package rip.skyland.soup.timers.abilities;

import rip.skyland.core.util.CC;
import rip.skyland.phase.PhaseSupplier;
import rip.skyland.soup.kit.KitManager;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.timers.PlayerTimer;
import rip.skyland.soup.timers.abilities.event.AbilityUseEvent;
import rip.skyland.soup.util.XPBarTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;


public class WarriorAbility extends PlayerTimer implements Listener {

    public WarriorAbility() {
        super(false, "Warrior", 35000L);
    }

    public void onExpire(Player player) {
        player.sendMessage(CC.translate("&eYou may now use &dWarrior&e."));
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if(SoupProfile.getByPlayer(event.getPlayer()).getKit() != null && SoupProfile.getByPlayer(event.getPlayer()).getKit().equals(KitManager.getByName("Warrior")) && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().equals(Material.GLOWSTONE_DUST)) {
            if (event.getRightClicked() instanceof Player) {
                if (hasCooldown(event.getPlayer())) {
                    event.getPlayer().sendMessage(CC.translate("&cYou can't use this for another &e" + PhaseSupplier.getRemainingTime(getRemaining(event.getPlayer()), true) + '.'));
                    return;

                }

                AbilityUseEvent event1 = new AbilityUseEvent(event.getPlayer(), this);
                Bukkit.getPluginManager().callEvent(event1);

                if(!event1.isCancelled()) {
                    Player player1 = (Player) event.getRightClicked();
                    Player player2 = event.getPlayer();

                    player2.getWorld().strikeLightningEffect(player2.getLocation());

                    player1.setVelocity(new Vector().setY(2.6).add(player2.getEyeLocation().getDirection().multiply(2.4)));
                    setCooldown(player2);
                    XPBarTimer.runXpBar(player2, 35);
                }
            }
        }
    }

}
