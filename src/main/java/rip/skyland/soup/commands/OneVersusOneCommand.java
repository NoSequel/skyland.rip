package rip.skyland.soup.commands;

import rip.skyland.core.util.CC;
import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.soup.util.Items;
import org.bukkit.entity.Player;

public class OneVersusOneCommand {

    @Command(names={"1v1"})
    public void execute(Player sender) {
        sender.sendMessage(CC.translate("&eTeleporting you to the 1v1 lobby..."));
        Items.join1v1(sender);
    }
}
