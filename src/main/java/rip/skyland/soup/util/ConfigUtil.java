package rip.skyland.soup.util;

import rip.skyland.soup.SoupPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ConfigUtil {

    public static void setLocation(String path, Location location) {
        SoupPlugin p = SoupPlugin.getInstance();
        p.getConfig().set(path + ".WORLD", location.getWorld().getName());
        p.getConfig().set(path + ".X", location.getBlockX());
        p.getConfig().set(path + ".Y", location.getBlockY());
        p.getConfig().set(path + ".Z", location.getBlockZ());
        p.getConfig().set(path + ".YAW", location.getYaw());
        p.getConfig().set(path + ".PITCH", location.getPitch());

        p.saveConfig();
        p.reloadConfig();
    }

    public static Location getLocation(String path) {
        SoupPlugin p = SoupPlugin.getInstance();
        if(!p.getConfig().contains(path))
            return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        else {
            World world = Bukkit.getWorld(p.getConfig().getString(path + ".WORLD"));
            int x =  p.getConfig().getInt(path + ".X");
            int y = p.getConfig().getInt(path + ".Y");
            int z = p.getConfig().getInt(path + ".Z");
            double pitch = (p.getConfig().getDouble(path + ".PITCH"));
            double yaw = (p.getConfig().getDouble(path + ".YAW"));

            System.out.println(pitch + " " + yaw);
            return new Location(world, x, y, z, (float) yaw, (float) pitch);
        }
    }
}
