package rip.skyland.soup.timers.abilities;

import org.bukkit.event.Listener;
import rip.skyland.core.util.CC;
import rip.skyland.phase.PhaseSupplier;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.timers.PlayerTimer;
import rip.skyland.soup.util.XPBarTimer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

public class FishermanAbility extends PlayerTimer implements Listener {

    private String scoreboardPrefix;

    public FishermanAbility() {
        super(false, "Fisherman", 10000L);
    }

    public void onExpire(Player player) {
        player.sendMessage(CC.translate("&eYou may now use &dFisherman&e."));
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {

        if(!SoupProfile.getByPlayer(event.getPlayer()).getKit().getName().equals("Fisherman"))
            return;

        if (hasCooldown(event.getPlayer())) {
            event.getPlayer().sendMessage(CC.translate("&cYou can't use this for another &e" + PhaseSupplier.getRemainingTime(getRemaining(event.getPlayer()), true) + '.'));
            return;
        }
        if(event.getCaught() instanceof Player) {
            event.getCaught().teleport(event.getPlayer().getLocation());

            XPBarTimer.runXpBar(event.getPlayer(), 10);
            setCooldown(event.getPlayer());
        }
    }

}
