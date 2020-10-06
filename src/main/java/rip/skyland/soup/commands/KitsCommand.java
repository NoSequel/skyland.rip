package rip.skyland.soup.commands;

import org.bukkit.command.CommandSender;
import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.soup.kit.KitMenu;
import org.bukkit.entity.Player;

public class KitsCommand {

    @Command(names={"kits"})
    public void execute(Player player) {
        KitMenu.openMenu(player);
    }
}
