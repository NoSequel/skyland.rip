package rip.skyland.soup.commands;

import org.bukkit.command.CommandSender;
import rip.skyland.core.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.skyland.core.util.command.annotation.Command;

public class SetSpawnCommand {

    @Command(names={"setspawn"}, permission="soup.admin")
    public void execute(Player player) {
        Bukkit.getWorlds().get(0).setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
        Bukkit.getWorlds().get(0).getSpawnLocation().setPitch(player.getLocation().getPitch());
        Bukkit.getWorlds().get(0).getSpawnLocation().setYaw(player.getLocation().getYaw());

        player.sendMessage(CC.translate("&aYou have set the world spawn to your location."));
    }
}
