package rip.skyland.phase.thread;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import rip.skyland.core.profile.Profile;
import rip.skyland.phase.Phase;
import rip.skyland.phase.entry.PhaseBoard;
import rip.skyland.phase.util.CC;
import rip.skyland.phase.util.PlayerUtil;

import java.util.ConcurrentModificationException;

public class NametagThread extends Thread {

    public NametagThread() {
        this.start();
    }

    public void run() {
        while (true) {
            PlayerUtil.getOnlinePlayers().stream().filter(player -> {
                try {
                    return PhaseBoard.getByPlayer(player) != null &&
                            Profile.getByUuid(player.getUniqueId()) != null;
                } catch(ConcurrentModificationException exception) {
                    return false;
                }
            }).forEach(player -> {
                Scoreboard scoreboard = PhaseBoard.getByPlayer(player).getScoreboard();
                Objective objective = scoreboard.getObjective("health") == null ? scoreboard.registerNewObjective("health", "health") : scoreboard.getObjective("health");

                objective.setDisplayName(CC.DARK_RED + "❤");
                objective.setDisplaySlot(DisplaySlot.BELOW_NAME);

                String playerName = player.getName().substring(0, Math.min(player.getName().toCharArray().length, 16));
                Team team = scoreboard.getTeam(playerName) == null ? scoreboard.registerNewTeam(playerName) : scoreboard.getTeam(playerName);
                team.setPrefix(Profile.getByUuid(player.getUniqueId()).getRank().getDisplayColor());
            });

            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
