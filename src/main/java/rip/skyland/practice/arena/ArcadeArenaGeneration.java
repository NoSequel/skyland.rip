package rip.skyland.practice.arena;

import net.minecraft.server.v1_7_R4.NBTCompressedStreamTools;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.practice.match.Match;
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
public class ArcadeArenaGeneration {

    private static int nextX, mX = 0, mZ = 0, increment = 200;

    public static void generate(Match match) {
        World world = Bukkit.getWorld("arcade");

        File file = new File(SoupPlugin.getInstance().getDataFolder() + "/schematics",  "arcade.schematic");
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
                        int b = blocks[index] & 0xFF;
                        int facing = b & 0x03;

                        Material m = Material.getMaterial(b);

                        BlockUtil.placeBlock(world, x+nextX, y, z, mX, mZ, m.getId());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Player player1 = Bukkit.getPlayer(match.getPlayer1());
        Player player2 = Bukkit.getPlayer(match.getPlayer2());

        player1.teleport(new Location(world, nextX+40, 2, 0, 90, 0));
        player2.teleport(new Location(world, nextX-40, 2, 0, -90, 0));


        nextX+=increment;
    }


}
