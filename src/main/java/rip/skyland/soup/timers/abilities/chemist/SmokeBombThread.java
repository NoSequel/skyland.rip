package rip.skyland.soup.timers.abilities.chemist;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.skyland.phase.util.PlayerUtil;
import rip.skyland.soup.util.BlockUtil;

import java.util.Arrays;

public class SmokeBombThread extends Thread {

    public void run() {
        while (true) {
            SmokeBomb.getSmokeBombs().forEach(smokeBomb -> {
                if(smokeBomb.getEnd() >= System.currentTimeMillis())
                    SmokeBomb.getSmokeBombs().remove(smokeBomb);
                else {
                    smokeBomb.getLocations().forEach(BlockUtil::sendEnchantmentParticles);

                    PlayerUtil.getOnlinePlayers().stream()
                            .filter(player -> smokeBomb.getLocations().contains(player.getLocation()))
                            .forEach(player -> Arrays.asList(PotionEffectType.BLINDNESS, PotionEffectType.SLOW, PotionEffectType.CONFUSION)
                                    .forEach(potion -> player.addPotionEffect(new PotionEffect(potion, 8*20, 4))));
                }
            });
        }
    }
}
