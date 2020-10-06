package rip.skyland.soup.commands;

import org.bukkit.command.CommandSender;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.core.util.command.annotation.Param;
import rip.skyland.soup.profiles.SoupProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ResetCommand {

    @Command(names={"reset"}, permission="soup.reset")
    public void execute(CommandSender sender, @Param(name="player") String playerName, @Param(name="type") String type) {
        Player target = Bukkit.getPlayer(playerName);
        if(target == null) {
            sender.sendMessage(CC.translate("&cThat player does not exist."));
            return;
        }

        SoupProfile profile = SoupProfile.getByPlayer(target);

        switch(type) {
            case "KILLS": {
                profile.setKills(0);
            } break;
            case "DEATHS": {
                profile.setDeaths(0);
            } break;
            case "KILLSTREAK": {
                profile.setHighestKillstreak(0);
            } break;
            case "CREDITS": {
                profile.setCredits(0);
            } break;
            default: {
                sender.sendMessage(CC.translate("&cUsage: /reset <player> <kills|deaths|killstreak|credits>"));
                return;
            }
        }

        sender.sendMessage(CC.translate("&eYou have reset " + profile.getProfile().getDisplayName() + "&e's &d" + type.toLowerCase() + " statistic&e."));
        target.sendMessage(CC.translate("&eYour &d" + type.toLowerCase() + " statistic &ehas been reset."));

    }
}
