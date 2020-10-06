package rip.skyland.soup.commands;

import org.bukkit.command.CommandSender;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.core.util.command.annotation.Param;
import rip.skyland.soup.util.ConfigUtil;
import rip.skyland.soup.util.Locale;
import org.bukkit.entity.Player;

public class SetCornerCommand {

    @Command(names={"setcorner"}, permission="soup.staff")
    public void execute(Player player, @Param(name="1|2") int i) {
        if(i > 2) {
            player.sendMessage(CC.translate("&cUsage: /setcorner <1|2>"));
            return;
        }

        if(i == 1) {
            Locale.SPAWN_LOCATION_1 = player.getLocation();
            ConfigUtil.setLocation("SPAWN.LOCATION_1", player.getLocation());


        } else if(i == 2) {
            Locale.SPAWN_LOCATION_2 = player.getLocation();
            ConfigUtil.setLocation("SPAWN.LOCATION_2", player.getLocation());
        }

        player.sendMessage(CC.translate("&eYou have set corner &d" + i + " &eof the spawn."));
    }
}
