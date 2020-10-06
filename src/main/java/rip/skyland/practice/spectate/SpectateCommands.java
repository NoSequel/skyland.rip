package rip.skyland.practice.spectate;

import rip.skyland.core.util.CC;
import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.core.util.command.annotation.Param;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.profiles.ProfileState;
import rip.skyland.soup.profiles.SoupProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SpectateCommands {

    @Command(names={"spectate", "spec"})
    public void execute(Player player, @Param(name="player") Player target) {

        if(SoupProfile.getByPlayer(target).getCurrentMatch() == null) {
            player.sendMessage(CC.translate("&cThat player is not in a match."));
            return;
        }

        SoupProfile profile = SoupProfile.getByPlayer(player);

        if(!profile.getState().equals(ProfileState.ONE_VS_ONE)) {
            player.sendMessage(CC.translate("&cYou must be in \"/1v1\" to execute this command."));
            return;
        }

        if(profile.getCurrentMatch() != null) {
            player.sendMessage(CC.translate("&cYou can not be in a match."));
            return;
        }

        if(profile.getCurrentQueue() != null) {
            player.sendMessage(CC.translate("&cYou can not be in a queue."));
            return;
        }

        SoupPlugin.getInstance().getSpectateManager().spectateMatch(player, SoupProfile.getByPlayer(target).getCurrentMatch());
    }
}
