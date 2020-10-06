package rip.skyland.soup.event.commands;

import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.profiles.ProfileState;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.util.Items;
import org.bukkit.entity.Player;

public class QuitCommand {

    @Command(names={"quit", "leave"})
    public void execute(Player player) {
        if(SoupProfile.getByPlayer(player).getState().equals(ProfileState.ONE_VS_ONE))
            Items.leave1v1(player);

        SoupPlugin.getInstance().getEventManager().quit(player);
    }
}
