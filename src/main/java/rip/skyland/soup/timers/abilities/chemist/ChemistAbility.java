package rip.skyland.soup.timers.abilities.chemist;

import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.timers.PlayerTimer;

public class ChemistAbility extends PlayerTimer implements Listener {


    public ChemistAbility() {
        super(false, "", 350000L);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(player.getItemInHand().getType().equals(Material.MONSTER_EGG)) {
            SoupProfile profile = SoupProfile.getByPlayer(player);
            if(profile.getKit() != null && profile.getKit().getName().equals("Chemist")) {
                new SmokeBomb(player, player.getLocation().add(player.getEyeLocation().getDirection().multiply(2)));
            }
        }
    }

}
