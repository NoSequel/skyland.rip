package rip.skyland.practice.match.impl;

import fanciful.FancyMessage;
import rip.skyland.core.profile.Profile;
import rip.skyland.core.util.CC;
import rip.skyland.practice.arena.SumoArenaGeneration;
import rip.skyland.practice.match.Match;
import rip.skyland.practice.queue.Queue;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.kit.Kit;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class SumoMatch implements Match {

    private Queue queue;
    private Kit kit;
    private UUID player1, player2, winner, loser;
    private boolean ranked, ended;
    private ConcurrentHashMap<Match, UUID> spectators;

    private int winsPlayer1, winsPlayer2, x, y, z, xIncrease;
    private World world;

    private boolean started;

    public SumoMatch(Kit kit, UUID player1, UUID player2, boolean ranked) {
        this.kit = kit;
        this.player1 = player1;
        this.player2 = player2;
        this.ranked = ranked;

        this.winsPlayer1 = 0;
        this.winsPlayer2 = 0;

        spectators = new ConcurrentHashMap<>();

        SumoArenaGeneration.generate(this);
        this.handleMatch();
    }

    public void addSpectator(UUID player) {
        spectators.put(this, player);
    }

    public void removeSpectator(UUID player1) {
        spectators.remove(this, player1);
    }

    private void start(boolean first) {
        SoupPlugin.getInstance().getMatchManager().handleMatch(this, 3, false, first);
        Arrays.asList(player1, player2).forEach(player -> {
            Player target = Bukkit.getPlayer(player);
            target.setWalkSpeed(0);
            target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 4*20, 1001));

            Bukkit.getScheduler().scheduleAsyncDelayedTask(SoupPlugin.getInstance(), () -> target.setWalkSpeed(0.2F), 70L);
        });
    }

    @Override
    public void handleMatch() {
        start(true);
    }

    @Override
    public void handleDeath(UUID player, boolean left) {

        if(left) {
            SoupPlugin.getInstance().getMatchManager().handleDeath(this, getOpponent(player).getUniqueId(), player, true, false, false);
            return;
        }

        int wins;

        if(player == player1) {
            winsPlayer2++;
            wins = winsPlayer2;
        }
        else {
            winsPlayer1++;
            wins = winsPlayer1;
        }
        Profile profile = Profile.getByUuid(getOpponent(player).getUniqueId());

        if(wins < 3) {
            Player target = Bukkit.getPlayer(player);

            target.sendMessage(CC.translate(profile.getDisplayName() + " &ehas &awon &ethe round, they need &d" + (3 - wins) + " &emore to win."));
            getOpponent(player).sendMessage(CC.translate("&eYou have &awon &ethe round, you need &d" + (3 - wins) + " &emore to win."));

            Bukkit.getPlayer(player1).teleport(new Location(world, x+xIncrease, y, z, 90, 0));
            Bukkit.getPlayer(player2).teleport(new Location(world, x-xIncrease, y, z, -90, 0));

            start(false);
        } else {
            if(!ended) {
                SoupPlugin.getInstance().getMatchManager().handleDeath(this, getOpponent(player).getUniqueId(), player, false, false, false);
                this.ended = true;
            }
        }

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
