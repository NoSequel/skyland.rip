package rip.skyland.practice.spectate;

import rip.skyland.core.util.CC;
import rip.skyland.practice.match.Match;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SpectateManager {

    void spectateMatch(Player player, Match match) {
        match.broadcast(CC.AQUA + player.getName() + " &eis now spectating.");
        match.addSpectator(player.getUniqueId());
        player.setGameMode(GameMode.CREATIVE);

        SoupProfile.getByPlayer(player).setSpectatingMatch(match);
        SoupProfile.getByPlayer(player).setSpectating(true);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setContents(Items.spectateItems());
        Arrays.asList(match.getPlayer1(), match.getPlayer2()).forEach(uuid -> {
            if(Bukkit.getPlayer(uuid) != null) {
                SoupPlugin.getInstance().getGameManager().showPlayer(player, Bukkit.getPlayer(uuid));
                player.teleport(Bukkit.getPlayer(uuid));
            }
        });

    }

    public void unspectateMatch(Player player, Match match) {
        match.broadcast(CC.AQUA + player.getName() + " &eis no longer spectating.");
        match.removeSpectator(player.getUniqueId());
        Items.resetPlayer(player);

        SoupProfile.getByPlayer(player).setSpectatingMatch(null);
        SoupProfile.getByPlayer(player).setSpectating(false);
    }
}
