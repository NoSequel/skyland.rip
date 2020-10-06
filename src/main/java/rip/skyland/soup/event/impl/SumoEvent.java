package rip.skyland.soup.event.impl;

import rip.skyland.core.profile.Profile;
import rip.skyland.core.util.CC;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.event.Event;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.util.ConfigUtil;
import rip.skyland.soup.util.Items;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

@Getter
public class SumoEvent extends Event {

    private BukkitTask task;
    public List<Player> players = new ArrayList<>();
    public List<Player> fighting = new ArrayList<>();

    private String name, description;
    private int maxPlayers, minPlayers;
    private ItemStack[] items;

    public boolean loseDamage() { return false; }

    @Setter
    public int round;

    public SumoEvent() {
        name = "Sumo";
        description = "In the sumo event:you have to knock people:off an island in a 1v1:Winner receives 400 credits.";
        maxPlayers = 40;
        minPlayers = 2;
        items = null;
    }

    public Location getSpawnLocation() {
        String p = "SUMO.LOBBY";
        return ConfigUtil.getLocation(p);
    }

    private Location[] getSpawnPoints() {
        String path = "SUMO.";
        String loc1 = path + "LOCATION1";
        String loc2 = path + "LOCATION2";
        return new Location[] {
                ConfigUtil.getLocation(loc1),
                ConfigUtil.getLocation(loc2)
        };
    }

    public void startTask() {
        this.setRound(0);
        this.setStarted(true);
        randomPlayers();

        Bukkit.getPluginManager().registerEvents(new Listener() {

            @EventHandler
            public void onDeath(PlayerDeathEvent event) {
                Player player = event.getEntity();
                if(fighting.contains(player))
                    handleDeath(player);
            }

        }, SoupPlugin.getInstance());

    }

    private void randomPlayers() {

        setRound(getRound()+1);
        fighting.clear();

        task = new BukkitRunnable() {

            String prefix = "&7[&9Round " + round + "&7] ";

            int i = 3;
            public void run() {

                if(!SoupPlugin.getInstance().getEventManager().isBusy())
                    this.cancel();

                if(i > 0) {
                    String s = i == 1 ? "second" : "seconds";
                    broadcast(prefix + "&eStarting in &6" + i + ' ' + s + CC.YELLOW + '.');
                } else if(i == 0) {


                    Collections.addAll(fighting, selectPlayers());

                    Profile profile1 = Profile.getByUuid(fighting.get(0).getUniqueId());
                    Profile profile2 = Profile.getByUuid(fighting.get(1).getUniqueId());
                    broadcast(prefix + "&6" + profile1.getName() + " &evs &6" + profile2.getName() + "&e.");
                    fighting.get(0).teleport(getSpawnPoints()[0]);
                    fighting.get(1).teleport(getSpawnPoints()[1]);
                }
                i--;
            }
        }.runTaskTimer(SoupPlugin.getInstance(), 20L, 20L);


    }

    private Player[] selectPlayers() {
        Player player1 = players.get(new Random().nextInt(players.size()));
        Player player2 = players.get(new Random().nextInt(players.size()));

        if(player1 == player2)
            return selectPlayers();

        return new Player[] { player1, player2 };
    }

    public void handleDeath(Player player) {
        if(fighting.get(0).equals(player))
            fighting.get(1).teleport(getSpawnLocation());
        else
            fighting.get(0).teleport(getSpawnLocation());


        fighting.clear();
        players.remove(player);
        Items.resetPlayer(player);

        broadcast(CC.DARK_RED + Profile.getByUuid(player.getUniqueId()).getName() + " &chas been eliminated.");

        if(players.size() == 1) {
            SoupProfile profile = SoupProfile.getByPlayer(players.get(0));
            Bukkit.broadcastMessage(CC.translate(profile.getProfile().getDisplayName() + " &ehas won the &9Sumo &eevent and received &a400 &ecredits."));
            profile.setCredits(profile.getCredits()+400);

            Items.resetPlayer(profile.getPlayer());

            SoupPlugin.getInstance().getEventManager().setBusy(false);
            SoupPlugin.getInstance().getEventManager().setCurrentEvent(null);
            this.setStarted(false);
            this.setRound(0);

            this.getPlayers().clear();
            this.getFighting().clear();
            task.cancel();

            return;
        }

        randomPlayers();
    }

}
