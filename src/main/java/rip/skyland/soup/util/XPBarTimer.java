package rip.skyland.soup.util;

import rip.skyland.soup.SoupPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class XPBarTimer {

    private static Map<Player, BukkitTask> runnables = new HashMap<>();

    public static void runXpBar(Player player, int cooldown) {
        if(player.getLevel() < cooldown) {
            if(runnables.get(player) != null)
                runnables.get(player).cancel();

            player.setLevel(cooldown);

            runnables.put(player, new BukkitRunnable() {
                int cd = cooldown-1;
                public void run() {
                    player.setLevel(cd);

                    cd--;
                }

            }.runTaskTimerAsynchronously(SoupPlugin.getInstance(), 20L, 20L));
        }
    }

}
