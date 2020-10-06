package rip.skyland.soup.timers.abilities;

import rip.skyland.core.util.CC;
import rip.skyland.phase.PhaseSupplier;
import rip.skyland.soup.kit.KitManager;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.timers.PlayerTimer;
import rip.skyland.soup.timers.abilities.event.AbilityUseEvent;
import rip.skyland.soup.util.BlockUtil;
import rip.skyland.soup.util.XPBarTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KidnapperAbility extends PlayerTimer implements Listener {


    public KidnapperAbility() {
        super(false, "Kidnapper", 35000L);
    }

    public void onExpire(Player player) {

        player.sendMessage(CC.translate("&eYou may now use &dKidnapper&e."));
    }



    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (SoupProfile.getByPlayer(event.getPlayer()).getKit() != null && SoupProfile.getByPlayer(event.getPlayer()).getKit().equals(KitManager.getByName("Kidnapper")) && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().equals(Material.TRIPWIRE_HOOK)) {

            if (hasCooldown(event.getPlayer())) {
                event.getPlayer().sendMessage(CC.translate("&cYou can't use this for another &e" + PhaseSupplier.getRemainingTime(getRemaining(event.getPlayer()), true) + '.'));
                return;

            }

            AbilityUseEvent event1 = new AbilityUseEvent(event.getPlayer(), this);
            Bukkit.getPluginManager().callEvent(event1);

            if (!event1.isCancelled()) {
                Player player = event.getPlayer();

                BlockUtil.generateTemporarySphere(player.getLocation(), 5, true, Material.ICE, 5);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20, 1));
                setCooldown(player);
                XPBarTimer.runXpBar(player, 35);

            }
        }
    }
}
