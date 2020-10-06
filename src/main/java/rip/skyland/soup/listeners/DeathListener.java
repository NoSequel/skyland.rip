package rip.skyland.soup.listeners;

import rip.skyland.core.util.CC;
import rip.skyland.core.util.ItemBuilder;
import rip.skyland.soup.kit.KitManager;
import rip.skyland.soup.profiles.SoupProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.Arrays;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(event.getEntity().getKiller() == null)
            return;

        Player player = event.getEntity().getKiller();
        SoupProfile profile = SoupProfile.getByPlayer(player);
        SoupProfile d = SoupProfile.getByPlayer(event.getEntity());

        d.clearCooldowns();

        if (profile.getKit().equals(KitManager.getByName("Archer")) || profile.getKit().equals(KitManager.getByName("Mario")) || profile.getKit().equals(KitManager.getByName("Kidnapper"))) {
            Arrays.stream(player.getInventory().getArmorContents()).forEach(item -> {
                int x = item.getDurability() + 10;
                item.setDurability((short) x);
            });

            if(profile.getKit().equals(KitManager.getByName("Mario"))) {
                player.getInventory().setItem(1, new ItemBuilder(Material.FIREBALL).displayName("&cFireball").create());
            }

            if(profile.getKit().equals(KitManager.getByName("Archer"))) {
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));

                DecimalFormat format = new DecimalFormat("####");
                player.sendMessage(CC.translate("&9You have killed " + d.getProfile().getDisplayName() + " &9from &e[&c" +
                        format.format(player.getLocation().distance(d.getPlayer().getLocation())) + "] &9blocks away for &a5 credits."));
            }
        }
    }
}
