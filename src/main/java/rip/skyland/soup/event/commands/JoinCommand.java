package rip.skyland.soup.event.commands;

import rip.skyland.core.util.CC;
import rip.skyland.core.util.PlayerUtil;
import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.soup.SoupPlugin;

import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.util.Locale;
import org.bukkit.entity.Player;

public class JoinCommand {

    @Command(names={"join"})
    public void execute(Player player) {
        if(SoupProfile.getByPlayer(player).getCurrentMatch() != null) {
            player.sendMessage(CC.translate("&cYou can not join events while in a match."));
            return;
        }


        if(!PlayerUtil.inArea((player).getLocation(), Locale.SPAWN_LOCATION_1, Locale.SPAWN_LOCATION_2)) {
            player.sendMessage(CC.translate("&cYou can only join events while in spawn."));
            return;
        }

        if(!SoupPlugin.getInstance().getEventManager().isBusy()) {
            player.sendMessage(CC.translate("&cThere is no active event."));
            return;
        }

        if(SoupPlugin.getInstance().getEventManager().getCurrentEvent().getStartTimer() <= 2) {
            player.sendMessage(CC.translate("&cThat event already started."));
        }

        if(SoupPlugin.getInstance().getEventManager().getCurrentEvent().getPlayers().size() >= SoupPlugin.getInstance().getEventManager().getCurrentEvent().getMaxPlayers() && !player.isOp()) {
            player.sendMessage(CC.translate("&cThat event has already filled up."));
            return;
        }

        if(SoupPlugin.getInstance().getEventManager().getCurrentEvent().getPlayers().contains(player)) {
            player.sendMessage(CC.translate("&cYou are already inside of an event."));
            return;
        }

        SoupPlugin.getInstance().getEventManager().join(player);

    }
}
