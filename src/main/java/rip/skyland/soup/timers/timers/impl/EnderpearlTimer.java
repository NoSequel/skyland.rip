package rip.skyland.soup.timers.timers.impl;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import rip.skyland.core.util.CC;
import rip.skyland.phase.PhaseSupplier;
import rip.skyland.soup.timers.PlayerTimer;

public class EnderpearlTimer extends PlayerTimer implements Listener {

    public EnderpearlTimer() {
        super(true, "Enderpearl", 16000L);
    }

    public void onExpire(Player player) {
        player.sendMessage(CC.translate("&eYou may now use &dEnderpearl&e."));
    }


    @EventHandler
    public void onEnderpearl(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(event.getItem() != null && event.getItem().getType().equals(Material.ENDER_PEARL) && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                Player player = event.getPlayer();
                if(hasCooldown(player)) {
                    event.getPlayer().sendMessage(CC.translate("&cYou can't use this for another &e" + PhaseSupplier.getRemainingTime(getRemaining(event.getPlayer()), true) + '.'));
                    event.setCancelled(true);
                } else setCooldown(player);
            }
        }
    }

}
