package rip.skyland.soup.timers.timers.impl;

import org.bukkit.entity.Player;
import rip.skyland.core.util.CC;
import rip.skyland.soup.timers.PlayerTimer;
import rip.skyland.soup.util.Items;

public class SpawnWarpTimer extends PlayerTimer {

    public SpawnWarpTimer() { super(false, "Spawn Warp", 10000L); }

    public void onTick(Player player) {
        if(getRemaining(player) == 5*1000 || getRemaining(player) == 4*1000 || getRemaining(player) == 3*1000 || getRemaining(player) == 2*1000)
            player.sendMessage(CC.translate("&eWarping to &dspawn &ein &d" + (getRemaining(player)/1000) + " &eseconds..."));

        if(getRemaining(player) == 1000)
            player.sendMessage(CC.translate("&eWarping to &dspawn &ein &d1 &esecond..."));
    }

    public void onExpire(Player player) {
        player.sendMessage(CC.translate("&eWarped to &dspawn&e."));
        Items.resetPlayer(player);
    }

}
