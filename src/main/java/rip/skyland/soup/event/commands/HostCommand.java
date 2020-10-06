package rip.skyland.soup.event.commands;

import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.soup.util.menuis.HostMenu;
import org.bukkit.entity.Player;

public class HostCommand {

    @Command(names={"host"}, permission="soup.host")
    public void execute(Player player) {
        new HostMenu().openMenu(player);
    }
}
