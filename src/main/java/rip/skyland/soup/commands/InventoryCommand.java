package rip.skyland.soup.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import rip.skyland.core.profile.Profile;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.core.util.command.annotation.Param;
import rip.skyland.practice.match.inventory.CachedInventory;
import org.bukkit.entity.Player;

public class InventoryCommand {

    @Command(names={"_"})
    public void execute(Player player, @Param(name="player") String playerName) {
        if(Bukkit.getPlayer(playerName) == null || Profile.getByUuid(Bukkit.getPlayer(playerName).getUniqueId()) == null) {
            player.sendMessage(CC.translate("&cThat player does not have a cached inventory."));
            return;
        }

        player.openInventory(CachedInventory.getByUuid(Bukkit.getPlayer(playerName).getUniqueId()).getInventory());
    }
}
