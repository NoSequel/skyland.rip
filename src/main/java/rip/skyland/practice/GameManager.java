package rip.skyland.practice;

import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GameManager {

    public void hidePlayer(Player player, Player target) {
        PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer) player).getHandle());
        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet);
        target.hidePlayer(player);

    }

    public void showPlayer(Player player, Player target) {
        PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.addPlayer(((CraftPlayer) player).getHandle());
        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet);
        target.showPlayer(player);
    }

}
