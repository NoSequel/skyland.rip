package rip.skyland.practice.arena;

import net.minecraft.server.v1_7_R4.NBTCompressedStreamTools;
import rip.skyland.practice.match.impl.SumoMatch;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.util.BlockUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

@Getter
public class SumoArenaGeneration {

    private static int nextX, mX = 0, mZ = 0, increment = 200;

    public static void generate(SumoMatch match) {
        World world = Bukkit.getWorld("sumo");

        File file = new File(SoupPlugin.getInstance().getDataFolder() + "/schematics",  "sumo.schematic");
        try {
            FileInputStream fis = new FileInputStream(file);
            Object nbtData = NBTCompressedStreamTools.class.getMethod("a", InputStream.class).invoke(null, fis);
            Method getShort = nbtData.getClass().getMethod("getShort", String.class);
            Method getByteArray = nbtData.getClass().getMethod("getByteArray", String.class);

            short width = ((short) getShort.invoke(nbtData, "Width"));
            short height = ((short) getShort.invoke(nbtData, "Height"));
            short length = ((short) getShort.invoke(nbtData, "Length"));

            mX = width / 2;
            mZ = length / 2;

            byte[] blocks = ((byte[]) getByteArray.invoke(nbtData, "Blocks"));

            fis.close();
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    for (int z = 0; z < length; ++z) {
                        int index = y * width * length + z * width + x;
                        int b = blocks[index] & 0xFF;//make the block unsigned,
                        // so that blocks with an id over 127, like quartz and emerald, can be pasted
                        Material m = Material.getMaterial(b);

                        BlockUtil.placeBlock(world, x+nextX, y+10, z, mX, mZ, m.getId());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Player player1 = Bukkit.getPlayer(match.getPlayer1());
        Player player2 = Bukkit.getPlayer(match.getPlayer2());


        match.setX(nextX);
        match.setY(28);
        match.setZ(0);
        match.setXIncrease(4);
        match.setWorld(world);

        player1.teleport(new Location(world, nextX+4, 26, 0, 90, 0));
        player2.teleport(new Location(world, nextX-4, 26, 0, -90, 0));


        nextX+=increment;
    }
}
