package rip.skyland.soup.util;

import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;
import net.minecraft.util.com.google.common.base.Preconditions;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.material.Directional;
import org.bukkit.material.Step;
import rip.skyland.core.util.PlayerUtil;
import rip.skyland.soup.SoupPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class BlockUtil {

    public static void generateTemporarySphere(Location centerBlock, int radius, boolean hollow, Material type, int duration) {
        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {

                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));

                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

                        Location l = new Location(centerBlock.getWorld(), x, y, z);

                        if (y>=centerBlock.getBlockY() && l.getBlock().getType() == Material.AIR) {
                            l.getBlock().setType(type);

                            Bukkit.getScheduler().runTaskLater(SoupPlugin.getInstance(), () -> {
                                if (l.getBlock().getType() == type)
                                    l.getBlock().setType(Material.AIR);
                            }, duration * 20);
                        }
                    }

                }
            }
        }
    }

    public static void sendFlameParticles(Entity entity) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles("flame",(float)  entity.getLocation().getX(), (float) entity.getLocation().getY(), (float)  entity.getLocation().getZ(), (float)  1, (float) 2, (float) 1, (float) 0, 24);
        PacketPlayOutWorldParticles packet2 = new PacketPlayOutWorldParticles("smoke",(float)  entity.getLocation().getX(), (float) entity.getLocation().getY(), (float)  entity.getLocation().getZ(), (float)  1, (float) 1, (float) 1, (float) 0, 4);

        PlayerUtil.getOnlinePlayers().forEach(target -> {
            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet);
            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet2);
        });
    }
    
    public static void sendEnchantmentParticles(Location location) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles("instantSpell",(float)  location.getX(), (float) location.getY(), (float)  location.getZ(), (float)  1, (float) 2, (float) 1, (float) 0, 24);

        PlayerUtil.getOnlinePlayers().forEach(target ->
            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet)
        );
    }

    public static void placeBlock(World world, double x, double y, double z, int mX, int mZ, int material) {
        Location location = new Location(world, x - mX, y, z - mZ);
        location.getBlock().setTypeId(material);
    }

    public static void placeBlock(World world, double x, double y, double z, int mX, int mZ, int material, int facing) {
        Location location = new Location(world, x - mX, y, z - mZ);
        location.getBlock().setTypeId(material);

    }

    public static<T extends Entity> T addEntity(net.minecraft.server.v1_7_R4.World world, net.minecraft.server.v1_7_R4.Entity entity, CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Cannot spawn null entity");

        world.addEntity(entity, reason);
        return (T) entity.getBukkitEntity();
    }

    public static <T extends Entity> T removeEntity(net.minecraft.server.v1_7_R4.World world, net.minecraft.server.v1_7_R4.Entity entity) throws IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Cannot spawn null entity");

        world.removeEntity(entity);
        return (T) entity.getBukkitEntity();
    }




}
