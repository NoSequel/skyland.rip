package rip.skyland.soup.util;

import rip.skyland.core.CorePlugin;
import rip.skyland.core.util.PlayerUtil;
import rip.skyland.soup.profiles.SoupProfile;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class WaterTask extends BukkitRunnable {

    @Override
    public void run() {
        PlayerUtil.getOnlinePlayers().stream().filter(player ->
                !SoupProfile.getByPlayer(player).isSpectating()
                        && !SoupProfile.getByPlayer(player).isAvatarWatergunInvincibility()
                        && !player.getGameMode().equals(GameMode.CREATIVE)
        ).forEach(this::executeWater);
    }

    private void executeWater(Player player) {
        player.getNearbyEntities(1.1, 1.1, 1.1).forEach(entity -> {
            if (entity.getType().equals(EntityType.FALLING_BLOCK)) {
                for (int x = 0; x < 3; ++x) {
                    for (int z = 0; z < 3; ++z) {
                        Location curLoc = player.getLocation();
                        curLoc.setX(curLoc.getX() - 1 + x);
                        curLoc.setY(curLoc.getY()+1);
                        curLoc.setZ(curLoc.getZ() - 1 + z);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 4));

                        if (curLoc.getBlock().getType().equals(Material.AIR)) {
                            curLoc.getBlock().setType(Material.STATIONARY_WATER);

                            Bukkit.getScheduler().runTaskLater(CorePlugin.getInstance(), () -> curLoc.getBlock().setType(Material.AIR), 80L);
                        }
                    }
                }
            }
        });
    }

}
