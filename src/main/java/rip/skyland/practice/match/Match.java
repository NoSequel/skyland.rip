package rip.skyland.practice.match;

import fanciful.FancyMessage;
import rip.skyland.practice.queue.Queue;
import rip.skyland.soup.kit.Kit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface Match {

    public Queue getQueue();
    public UUID getPlayer1();
    public UUID getPlayer2();
    public ConcurrentHashMap<Match, UUID> getSpectators();
    public Kit getKit();
    public boolean isStarted();
    public boolean isRanked();
    public boolean isEnded();

    public void setQueue(Queue queue);
    public void setStarted(boolean b);
    public void handleMatch();
    public void handleDeath(UUID player, boolean left);

    public void broadcast(String msg);
    public void broadcast(FancyMessage msg);
    public void addSpectator(UUID player);
    public void removeSpectator(UUID player);

    public Player getOpponent(UUID player);
}
