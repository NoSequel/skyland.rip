package rip.skyland.soup.event;

import rip.skyland.core.util.CC;
import rip.skyland.soup.event.impl.SumoEvent;
import rip.skyland.soup.profiles.ProfileState;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.util.Items;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EventManager {

    public boolean busy;
    public Location lobby;
    public Event currentEvent;


    @Getter
    private static List<Event> events = new ArrayList<>();

    public EventManager() {
        this.setBusy(false);
        events.add(new SumoEvent());
    }

    public void join(Player player) {

        if(this.isBusy()) {


            SoupProfile profile = SoupProfile.getByPlayer(player);
            Event event = this.getCurrentEvent();

            player.getActivePotionEffects().forEach(ef -> player.removePotionEffect(ef.getType()));

            profile.setState(ProfileState.EVENT);
            event.getPlayers().add(player);
            event.broadcast(CC.ORANGE + profile.getProfile().getName() + " &ehas joined the game. ");
            event.broadcast("" + CC.YELLOW + '(' + event.getPlayers().size() + '/' + event.getMaxPlayers() + ')');
            player.getInventory().clear();
            if(event.getItems() != null) player.getInventory().setContents(event.getItems());
            player.teleport(this.getLobby());
        }
    }

    public void quit(Player player) {
        SoupProfile profile = SoupProfile.getByPlayer(player);
        if(profile.getState().equals(ProfileState.EVENT)) {
            Items.resetPlayer(player);
            if(currentEvent != null) {
                currentEvent.getPlayers().remove(player);
                currentEvent.broadcast(CC.ORANGE + profile.getProfile().getName() + " &ehas left the game. ");
                currentEvent.broadcast("" + CC.YELLOW + '(' + currentEvent.getPlayers().size() + '/' + currentEvent.getMaxPlayers() + ')');
            }
        }
    }


    public static Event getByClass(Class clazz) { return events.stream().filter(event -> event.getClass().equals(clazz)).findFirst().orElse(null); }

}
