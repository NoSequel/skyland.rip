package rip.skyland.practice.match.impl;

import fanciful.FancyMessage;

import rip.skyland.practice.match.Match;
import rip.skyland.practice.queue.Queue;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.kit.Kit;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class DefaultMatch implements Match {

    private Queue queue;
    private Kit kit;
    private UUID player1, player2, winner, loser;
    private boolean ranked, ended;
    private ConcurrentHashMap<Match, UUID> spectators;

    private boolean started;

    public DefaultMatch(Kit kit, UUID player1, UUID player2, boolean ranked) {
        this.kit = kit;
        this.player1 = player1;
        this.player2 = player2;
        this.ranked = ranked;

        spectators = new ConcurrentHashMap<>();
        this.handleMatch();
    }

    public void addSpectator(UUID player) {
        spectators.put(this, player);
    }

    public void removeSpectator(UUID player1) {
        spectators.remove(this, player1);
    }

    @Override
    public void handleMatch() {
        SoupPlugin.getInstance().getMatchManager().handleMatch(this, 3, false, true);
        Bukkit.getPlayer(player1).teleport(new Location(Bukkit.getWorlds().get(0), 195, 90, 245, 90, 0));
        Bukkit.getPlayer(player2).teleport(new Location(Bukkit.getWorlds().get(0), 145, 90, 245, 90, 0));
    }

    @Override
    public void handleDeath(UUID player, boolean left) {
        this.ended = true;

        SoupPlugin.getInstance().getMatchManager().handleDeath(this, getOpponent(player).getUniqueId(), player, left, false, true);
    }

    @Override
    public void broadcast(String msg) {
        SoupPlugin.getInstance().getMatchManager().broadcast(this, msg);
    }

    @Override
    public void broadcast(FancyMessage msg) {
        SoupPlugin.getInstance().getMatchManager().broadcast(this, msg);
    }

    public Player getOpponent(UUID player) {
        return player.equals(player1) ? Bukkit.getPlayer(player2) : Bukkit.getPlayer(player1);
    }
}
