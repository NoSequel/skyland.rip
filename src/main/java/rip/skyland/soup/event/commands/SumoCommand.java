package rip.skyland.soup.event.commands;

import rip.skyland.core.util.CC;
import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.core.util.command.annotation.Param;
import rip.skyland.soup.util.ConfigUtil;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SumoCommand {

     private String[] strings = new String[] {
            "&4&lSumo Command",
            "&c/sumo setpoint <1|2>",
            "&c/sumo setlobby",
    };

     @Command(names={"sumo"}, permission="core.admin")
     public void execute(Player player, @Param(name="args") String params) {
         String[] args = params.split(" ");
         if(args.length < 1) {
             Arrays.stream(strings).forEach(string -> player.sendMessage(CC.translate(string)));
             return;
         }

         if(args[0].equalsIgnoreCase("setlobby")) {
             player.sendMessage(CC.translate("&dYou have set the lobby for &9Sumo &bevents."));
             ConfigUtil.setLocation("SUMO.LOBBY", player.getLocation());
         } else if(args[0].equalsIgnoreCase("setpoint")) {
             if(args.length < 2 || (!args[1].equalsIgnoreCase("1") && !args[1].equalsIgnoreCase("2"))) {
                 Arrays.stream(strings).forEach(string -> player.sendMessage(CC.translate(string)));
                 return;
             }
             if(args[1].equalsIgnoreCase("1"))
                 ConfigUtil.setLocation("SUMO.LOCATION1", player.getLocation());

             if(args[1].equalsIgnoreCase("2"))
                 ConfigUtil.setLocation("SUMO.LOCATION2", player.getLocation());
         }

     }
}
