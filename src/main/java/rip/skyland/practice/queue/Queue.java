package rip.skyland.practice.queue;

import rip.skyland.soup.kit.Kit;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@Setter
public class Queue {

    private List<UUID> queueing;
    private List<UUID> playing;

    private Map<Player, Integer> joinTime = new HashMap<>();
    private Map<Player, int[]> eloRange = new HashMap<>();

    private String name, displayName;
    private boolean ranked, duelQueue;

    private ItemStack icon;

    private Kit kit;

    public Queue(String name, String displayName, boolean ranked, ItemStack icon) {
        this.name = name;
        this.displayName = displayName;
        this.ranked = ranked;
        this.icon = icon;

        this.queueing = new ArrayList<>();
        this.playing = new ArrayList<>();
    }

    public Queue setKit(Kit kit) {
        this.kit = kit;

        return this;
    }

    public Queue setDuelQueue(boolean value) {
        this.duelQueue = value;

        return this;
    }


}
