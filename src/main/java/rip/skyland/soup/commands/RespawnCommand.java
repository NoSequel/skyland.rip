package rip.skyland.soup.commands;

import rip.skyland.core.util.CC;
import rip.skyland.core.util.PlayerUtil;
import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.soup.SoupPlugin;
import org.bukkit.entity.Player;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.timers.timers.TimerManager;
import rip.skyland.soup.util.Locale;

public class RespawnCommand {

    @Command(names={"respawn", "spawn"})
    public void execute(Player player) {
        TimerManager manager = SoupPlugin.getInstance().getTimerManager();

        SoupProfile profile = SoupProfile.getByPlayer(player);

        if(profile.getCurrentMatch() != null) {
            player.sendMessage(CC.translate("&cYou can not warp while in a match."));
            return;
        }

        if(profile.getCurrentQueue() != null)
            SoupPlugin.getInstance().getQueueManager().leaveQueue(profile.getCurrentQueue(), player);

        if(manager.getEnderpearlTimer().hasCooldown(player)) {
            player.sendMessage(CC.translate("&cYou can not warp while under enderpearl cooldown."));
            return;
        }

        if(manager.getCombatTimer().hasCooldown(player)) {
            player.sendMessage(CC.translate("&cYou can not warp while in combat."));
            return;
        }

        if(manager.getSpawnWarpTimer().hasCooldown(player)) {
            player.sendMessage(CC.translate("&cYou are already awaiting a warp."));
            return;
        }

        if(PlayerUtil.inArea(player.getLocation(), Locale.SPAWN_LOCATION_1, Locale.SPAWN_LOCATION_2))
            manager.getSpawnWarpTimer().setCooldown(player, 100L);
        else {
            manager.getSpawnWarpTimer().setCooldown(player);
            player.sendMessage(CC.translate("&eWarping in &d10 seconds... &edon't move!"));
        }
    }
}
