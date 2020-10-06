package rip.skyland.soup.event.commands;

import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.util.ConfigUtil;
import org.bukkit.entity.Player;

public class SetLobbyCommand {

    @Command(names={"setlobby"}, permission="core.admin")
    public void execute(Player player) {
        ConfigUtil.setLocation("EVENTS.LOBBY", player.getLocation());
        SoupPlugin.getInstance().getEventManager().setLobby(player.getLocation());
    }
}
