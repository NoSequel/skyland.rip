package rip.skyland.soup.listeners;

import rip.skyland.core.util.CC;
import rip.skyland.soup.profiles.ProfileState;
import rip.skyland.soup.profiles.SoupProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class EventListeners implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if(!event.getMessage().startsWith("/leave")
                && !event.getMessage().startsWith("/quit")
        && !event.getMessage().startsWith("/msg")
        && !event.getMessage().startsWith("/r")
        && !event.getMessage().startsWith("/reply")
        && !event.getPlayer().isOp()
        && SoupProfile.getByPlayer(event.getPlayer()).getState().equals(ProfileState.EVENT)) {
            event.getPlayer().sendMessage(CC.translate("&cYou can not execute \"" + event.getMessage() + "\" in events."));
            event.setCancelled(true);
        }
    }
}
