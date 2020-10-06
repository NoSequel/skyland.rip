package rip.skyland.practice.duel;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.core.util.command.annotation.Param;
import rip.skyland.soup.profiles.ProfileState;
import rip.skyland.soup.profiles.SoupProfile;

import java.util.Objects;

public class AcceptCommand {

    @Command(names={"accept"})
    public void execute(Player player, @Param(name="player") Player target) {
        if(SoupProfile.getByPlayer(player).getIncomingRequests().stream().filter(request -> request.getRequester().equals(target)).findFirst().orElse(null) == null) {
            player.sendMessage(CC.translate("&cYou don't have a duel invite from " + target.getName()));
            return;
        }

        if(SoupProfile.getByPlayer(player).getCurrentMatch() != null || !SoupProfile.getByPlayer(player).getState().equals(ProfileState.ONE_VS_ONE) || SoupProfile.getByPlayer(player).getCurrentQueue() != null) {
            player.sendMessage(CC.translate("&cYou can't do that in your current state."));
            return;
        }

        // will not stfu
        Objects.requireNonNull(SoupProfile.getByPlayer(player).getIncomingRequests().stream().filter(request -> request.getRequester().equals(target)).findFirst().orElse(null)).accept();

    }
}
