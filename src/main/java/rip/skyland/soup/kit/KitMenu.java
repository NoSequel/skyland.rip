package rip.skyland.soup.kit;

import rip.skyland.core.CorePlugin;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.ItemBuilder;
import rip.skyland.core.util.PlayerUtil;

import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.IntStream;

public class KitMenu implements Listener {

    public static void openMenu(Player player) {

        Inventory inventory = Bukkit.createInventory(null, 27, CC.translate("&dChoose Your Kit"));
        if(!PlayerUtil.inArea(player.getLocation(), rip.skyland.soup.util.Locale.SPAWN_LOCATION_1, Locale.SPAWN_LOCATION_2)) {
            player.sendMessage(CC.translate("&eYou must be in spawn in order to open this menu..."));
            return;
        }

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)15);

        inventory.setItem(0, glass);
        inventory.setItem(8, glass);
        inventory.setItem(9, glass);

        inventory.setItem(17, glass);
        inventory.setItem(18, glass);

        inventory.setItem(21, glass);
        inventory.setItem(22, glass);
        inventory.setItem(23, glass);
        inventory.setItem(24, glass);
        inventory.setItem(25, glass);
        inventory.setItem(26, glass);


        KitManager.getKits().stream().filter(Kit::isActualKit).forEach(kit -> {
            String[] strings = kit.getDescription().split(":");
            List<String> std = new ArrayList<>();
            std.add("");
            Arrays.stream(strings).forEach(string -> std.add(CC.GRAY + string));
            std.add("");

            ItemStack item = new ItemBuilder(kit.getIcon().getType()).displayName(CC.GREEN + kit.getName()).lore(std).create();
            inventory.addItem(item);
        });


        player.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase(CC.translate("&dChoose Your Kit"))) {
            event.setCancelled(true);
            if (event.getInventory() != null && event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().getDisplayName() != null) {

                Kit kit = KitManager.getByName(CC.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                if (kit != null) {
                    kit.onEquip((Player) event.getWhoClicked());
                    event.getWhoClicked().closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getItem() == null || event.getItem().getItemMeta() == null)
            return;

        if (event.getItem().getType().equals(Material.BOOK) && event.getItem().getItemMeta().getDisplayName().contains("Selector"))
            openMenu(player);

        if(event.getItem().getType().equals(Material.WATCH) && event.getItem().getItemMeta().getDisplayName().contains("Previous")) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(CorePlugin.getInstance(), player::updateInventory, 2L);

            if(SoupProfile.getByPlayer(player).getLastKit() == null)
                player.sendMessage(CC.translate("&cYou have no previous kits..."));
             else
                SoupProfile.getByPlayer(player).getLastKit().onEquip(player);
        }

        if(event.getItem().getType().equals(Material.EYE_OF_ENDER)) {
            event.setCancelled(true);
            player.chat("/1v1");
        }
    }
}
