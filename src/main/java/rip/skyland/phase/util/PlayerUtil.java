package rip.skyland.phase.util;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public final class PlayerUtil {

    public static Collection<Player> getOnlinePlayers() {
        return new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
    }

    public static int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping+1;
    }

}
