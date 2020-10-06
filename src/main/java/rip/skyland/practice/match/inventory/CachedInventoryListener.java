package rip.skyland.practice.match.inventory;

import rip.skyland.core.CorePlugin;
import rip.skyland.core.profile.Profile;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class CachedInventoryListener implements Listener {

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        if(event.getInventory().getTitle().contains("Inventory of") && event.getCurrentItem() != null) {
            event.setCancelled(true);
            if(event.getCurrentItem().getType().equals(Material.TRIPWIRE_HOOK)) {
                String[] strings = event.getInventory().getTitle().split(" ");
                Profile profile = Profile.getByUuid(UUID.fromString(CorePlugin.getInstance().getMojang().getUUIDOfUsername(strings[2])));
                CachedInventory cachedInventory = CachedInventory.getByUuid(profile.getUuid());
                Inventory nextInventory = cachedInventory.getNextInventory().getInventory();
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().openInventory(nextInventory);
            }

        }
    }

}
