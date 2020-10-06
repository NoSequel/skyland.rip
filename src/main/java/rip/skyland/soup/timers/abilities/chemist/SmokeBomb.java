package rip.skyland.soup.timers.abilities.chemist;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class SmokeBomb {

    private long start;
    private long end;
    private Player player;
    private Location location;

    @Getter
    private static List<SmokeBomb> smokeBombs = new ArrayList<>();

    public SmokeBomb(Player player, Location location) {
        this.location = location;
        this.player = player;
        this.start = System.currentTimeMillis();
        this.end = System.currentTimeMillis()+10000;

        smokeBombs.add(this);
    }

    public List<Location> getLocations() {
        return Arrays.asList(
                location.subtract(1, 0, 0),
                location.subtract(0, 1, 0),
                location.subtract(1, 1, 0),
                location.subtract(1, 2, 0),
                location.subtract(2, 1, 0),
                location.add(1, 0, 0),
                location.add(0, 1, 0),
                location.add(1, 1, 0),
                location.add(1, 2, 0),
                location.add(2, 1, 0)
        );
    }

}
