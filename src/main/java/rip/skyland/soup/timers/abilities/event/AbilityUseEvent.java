package rip.skyland.soup.timers.abilities.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import rip.skyland.soup.timers.PlayerTimer;

@Getter
public class AbilityUseEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private PlayerTimer ability;
    private boolean cancelled;

    public AbilityUseEvent(Player player, PlayerTimer ability) {
        super(player);

        this.player = player;
        this.ability = ability;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
