package rip.skyland.soup.timers;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import rip.skyland.soup.SoupPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class PlayerTimer implements Timer {

    private boolean onScoreboard;
    private String scoreboardPrefix;
    private long defaultCooldown;

    private Map<Player, Long> cooldowns = new ConcurrentHashMap<>();
    private Map<Player, Boolean> cancelled = new HashMap<>();
    private Map<Player, BukkitTask> tasks = new HashMap<>();

    public PlayerTimer(boolean onScoreboard, String scoreboardPrefix, long defaultCooldown) {
        this.onScoreboard = onScoreboard;
        this.scoreboardPrefix = scoreboardPrefix;
        this.defaultCooldown = defaultCooldown;
    }

    public boolean hasCooldown(Player player) { return cooldowns.containsKey(player); }
    public void removeCooldown(Player player, boolean expire) {
        if(expire) onExpire(player);
        cancelled.put(player, true);
    }

    /**
     *
     * Give a player a cooldown
     * The duration is automatically set to the default cooldown.
     *
     * @param player the player to give the cooldown
     */

    public void setCooldown(Player player) { this.setCooldown(player, defaultCooldown); }

    public void cancel(Player player) {
        this.cancelled.put(player, true);
    }


    /**
     *
     * Give a player a cooldown
     *
     * @param player the player to give the cooldown
     * @param cooldown the duration of the cooldown
     */


    public void setCooldown(Player player, long cooldown) {

        if(cooldowns.containsKey(player)) {
            cooldowns.remove(player);
            if(tasks.containsKey(player))
                tasks.get(player).cancel();
        }

        cooldowns.put(player, cooldown);

        tasks.put(player, new BukkitRunnable() {
            public void run() {
                if (cancelled.containsKey(player)) {
                    cooldowns.remove(player);
                    this.cancel();
                    cancelled.remove(player);
                    return;
                }

                if (cooldowns.containsKey(player)) {
                    if (cooldowns.get(player) > 0) {
                        cooldowns.put(player, cooldowns.get(player) - 100L);
                        onTick(player);

                    }

                    if (cooldowns.get(player) <= 0) {
                        cooldowns.remove(player);
                        onExpire(player);
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(SoupPlugin.getInstance(), 2L, 2L));
    }

    public long getRemaining(Player player) { return cooldowns.get(player); }
    public void onExpire(Player player) {}
    public void onTick(Player player) {}
}
