package rip.skyland.practice.match;

import fanciful.FancyMessage;
import rip.skyland.core.CorePlugin;
import rip.skyland.core.profile.Profile;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.PlayerUtil;
import rip.skyland.practice.match.inventory.CachedInventory;
import rip.skyland.practice.queue.QueueManager;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.util.EloCalculator;
import rip.skyland.soup.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.skyland.soup.util.Locale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class MatchManager {

    public void handleDeath(Match match, UUID winner, UUID loser, boolean left, boolean addKitToPostMatch, boolean sendMessage) {


        if (match.getQueue() == null) {
            QueueManager.getByKit(match.getKit(), match.isRanked()).getPlaying().remove(match.getPlayer1());
            QueueManager.getByKit(match.getKit(), match.isRanked()).getPlaying().remove(match.getPlayer2());
        } else {
            match.getQueue().getPlaying().remove(match.getPlayer1());
            match.getQueue().getPlaying().remove(match.getPlayer2());
        }

        Profile w = Profile.getByUuid(winner);
        Profile l = Profile.getByUuid(loser);

        /*Arrays.asList(w, l).forEach(profile -> {

            if (profile != null) {
                if (profile.getPlayer() != null)
                    profile.getPlayer().setHealth(profile.getPlayer().getMaxHealth());

                profile.setLastItems(null);
                profile.setLastArmor(null);
                profile.setLastItems(new ArrayList<>());
                profile.setLastArmor(new ArrayList<>());

                Arrays.stream(profile.getPlayer().getInventory().getArmorContents()).forEach(profile.getLastArmor()::add);
                Arrays.stream(profile.getPlayer().getInventory().getContents()).forEach(profile.getLastItems()::add);
            }
        });*/


        if (left)
            match.broadcast(l.getDisplayName() + " &7disconnected.");
        else {
            if (sendMessage)
                match.broadcast(w.getDisplayName() + " &7killed " + l.getDisplayName());
        }

        Bukkit.getPlayer(l.getUuid()).getWorld().strikeLightningEffect(Bukkit.getPlayer(l.getUuid()).getLocation());

        match.broadcast("&cMatch ended.");
        Bukkit.getScheduler().runTaskLater(CorePlugin.getInstance(), () -> {
            FancyMessage msg = new FancyMessage(CC.translate("&aWinner: "))
                    .then(CC.translate("&e" + w.getName()))
                    .command("/_ " + w.getName())
                    .tooltip(CC.translate("&aClick to view the inventory of &6" + w.getName()))
                    .then(CC.translate(" &7- &cLoser:"))
                    .then(CC.translate(" &e" + l.getName()))
                    .command("/_ " + l.getName())
                    .tooltip(CC.translate("&aClick to view the inventory of &6" + l.getName()));

            if (sendMessage) {
                match.broadcast("&7&m-------------------------------------------");
                match.broadcast("&6Post-Match Inventories &7(click name to view inventories)");
                match.broadcast(msg);

                if (addKitToPostMatch)
                    match.broadcast("&6Kit: &5" + match.getKit().getName());

                if (!match.getSpectators().isEmpty()) {
                    String spectators = match.getSpectators().size() <= 4 ?
                            match.getSpectators().values().stream().map(spectator -> Profile.getByUuid(spectator).getName()).collect(Collectors.joining(", ")) :
                            match.getSpectators().values().stream().map(spectator -> Profile.getByUuid(spectator).getName()).collect(Collectors.joining(", ")).substring(0, 4)
                                    + (match.getSpectators().size() > 4 ? " &7(+" + (match.getSpectators().size() - 4) + ")" : "");
                    match.broadcast("&bSpectator" + (match.getSpectators().values().size() > 1 ? "s" : "") + " &b(" + match.getSpectators().size() + ")&7: " + spectators);
                }

                match.broadcast("&7&m-------------------------------------------");
            }

            if (match.isRanked() && !Locale.disableElo) {

                int elo1 = SoupProfile.getByPlayer(Bukkit.getPlayer(w.getUuid())).getElo(match.getKit());
                int elo2 = SoupProfile.getByPlayer(Bukkit.getPlayer(l.getUuid())).getElo(match.getKit());

                int[] ints = EloCalculator.getNewRankings(elo1, elo2, true);

                SoupProfile.getByPlayer(Bukkit.getPlayer(w.getUuid())).getElos().put(match.getKit().getName(), ints[0]);
                SoupProfile.getByPlayer(Bukkit.getPlayer(l.getUuid())).getElos().put(match.getKit().getName(), ints[1]);

                match.broadcast("&eElo Changes: &a" + w.getName() + " +" + (ints[0] - elo1) + " (" + ints[0] + ") &c" + l.getName() + " " + (ints[1] - elo2) + " (" + ints[1] + ')');


            }

            Arrays.asList(w, l).forEach(profile -> {
                SoupProfile.getByPlayer(Bukkit.getPlayer(profile.getUuid())).setCurrentMatch(null);
                SoupProfile.getByPlayer(Bukkit.getPlayer(profile.getUuid())).setKit(null);

                PlayerUtil.getOnlinePlayers().forEach(target -> {
                    SoupPlugin.getInstance().getGameManager().showPlayer(Bukkit.getPlayer(profile.getUuid()), target);
                    SoupPlugin.getInstance().getGameManager().showPlayer(target, Bukkit.getPlayer(profile.getUuid()));
                });
            });


            match.getSpectators().values().forEach(spectator -> SoupPlugin.getInstance().getSpectateManager().unspectateMatch(Bukkit.getPlayer(spectator), match));

            Items.resetPlayer(Bukkit.getPlayer(w.getUuid()));
            Items.resetPlayer(Bukkit.getPlayer(l.getUuid()));
        }, 20 * 3L);
        new CachedInventory(Profile.getByUuid(winner), match);
        new CachedInventory(Profile.getByUuid(loser), match);
    }

    public void handleMatch(Match match, int startDelay, boolean freeze, boolean first) {
        if(Bukkit.getPlayer(match.getPlayer1()) == null)
            match.handleDeath(match.getPlayer1(), true);

        if(Bukkit.getPlayer(match.getPlayer2()) == null)
            match.handleDeath(match.getPlayer2(), true);

        if(!first)
            match.setStarted(false);

        Player p1 = Bukkit.getPlayer(match.getPlayer1());
        Player p2 = Bukkit.getPlayer(match.getPlayer2());


        if(first) {
            PlayerUtil.getOnlinePlayers().forEach(target -> SoupPlugin.getInstance().getGameManager().hidePlayer(Bukkit.getPlayer(match.getPlayer1()), target));
            PlayerUtil.getOnlinePlayers().forEach(target -> SoupPlugin.getInstance().getGameManager().hidePlayer(Bukkit.getPlayer(match.getPlayer2()), target));

            SoupPlugin.getInstance().getGameManager().showPlayer(Bukkit.getPlayer(match.getPlayer1()), Bukkit.getPlayer(match.getPlayer2()));
            SoupPlugin.getInstance().getGameManager().showPlayer(Bukkit.getPlayer(match.getPlayer2()), Bukkit.getPlayer(match.getPlayer1()));
            SoupPlugin.getInstance().getGameManager().showPlayer(Bukkit.getPlayer(match.getPlayer1()), Bukkit.getPlayer(match.getPlayer1()));
            SoupPlugin.getInstance().getGameManager().showPlayer(Bukkit.getPlayer(match.getPlayer2()), Bukkit.getPlayer(match.getPlayer2()));

            match.getSpectators().values().forEach(spectator -> SoupPlugin.getInstance().getGameManager().showPlayer(Bukkit.getPlayer(match.getPlayer1()), Bukkit.getPlayer(spectator)));
            match.getSpectators().values().forEach(spectator -> SoupPlugin.getInstance().getGameManager().showPlayer(Bukkit.getPlayer(match.getPlayer2()), Bukkit.getPlayer(spectator)));


            Arrays.asList(p1, p2).forEach(t -> {
                t.getInventory().clear();
                SoupProfile.getByPlayer(t).setFallDamage(false);
                Profile.getByUuid(t.getUniqueId()).setName(t.getName());
            });

            Arrays.asList(match.getPlayer1(), match.getPlayer2()).forEach(player -> {
                Player p = Bukkit.getPlayer(player);
                SoupProfile profile = SoupProfile.getByPlayer(Bukkit.getPlayer(player));
                profile.setCurrentQueue(null);
                profile.setCurrentMatch(match);

                profile.clearCooldowns();

                if (freeze)
                    PlayerUtil.freeze(p, startDelay);
            });
        }

        Bukkit.getScheduler().runTaskTimer(SoupPlugin.getInstance(), new Runnable() {
            int i = startDelay;

            @Override
            public void run() {

                if(first) {
                    if (i == startDelay) {
                        if (match.isRanked()) {
                            SoupProfile profile1 = SoupProfile.getByPlayer(p1);
                            SoupProfile profile2 = SoupProfile.getByPlayer(p2);

                            int difference1 = profile1.getElos().get(match.getKit().getName()) - profile2.getElos().get(match.getKit().getName());
                            int difference2 = profile2.getElos().get(match.getKit().getName()) - profile1.getElos().get(match.getKit().getName());

                            String s = difference2 < 0 ? " &c(" + difference2 + ')' : " &a(+" + difference2 + ')';
                            String s1 = difference1 < 0 ? " &c(" + difference1 + ')' : " &a(+" + difference1 + ')';
                            if(!freeze) {
                                p1.sendMessage(CC.translate("&e&lMatch found! &eOpponent: &b" + profile2.getProfile().getName() + '(' + profile2.getElos().get(match.getKit().getName()) + ')' + s1));
                                p2.sendMessage(CC.translate("&e&lMatch found! &eOpponent: &b" + profile1.getProfile().getName() + '(' + profile1.getElos().get(match.getKit().getName()) + ')' + s));
                            } else {
                                p1.sendMessage(CC.translate("&e&lMatch found!" + s1));
                                p2.sendMessage(CC.translate("&e&lMatch found!" + s));
                            }
                        }
                    }
                }

                if(i > 0) {
                    match.broadcast("&e" + i + "...");
                }

                if(i == 0) {
                    if(freeze)
                        match.broadcast("&eYou have been given &a&l" + match.getKit().getName() + " &eto fight, kill or be killed.");

                    if(first)
                        match.broadcast("&aDuel started.");
                    else
                        match.broadcast("&aFight!");

                    p1.setWalkSpeed(0.2F);
                    p1.setFlySpeed(0.2F);

                    p2.setFlySpeed(0.2F);
                    p2.setWalkSpeed(0.2F);

                    if(first) {
                        match.getKit().onEquipNormal(p1);
                        match.getKit().onEquipNormal(p2);
                    }

                    match.setStarted(true);
                }

                i--;
            }

        }, 20L, 20L);
    }

    public void broadcast(Match match, String msg) {
        Arrays.asList(match.getPlayer1(), match.getPlayer2()).forEach(player -> {
            if(Bukkit.getPlayer(player) != null)
                Bukkit.getPlayer(player).sendMessage(CC.translate(msg));
        });

        if (!match.getSpectators().isEmpty())
            match.getSpectators().values().forEach(spectator -> {
                if (Bukkit.getPlayer(spectator) != null)
                    Bukkit.getPlayer(spectator).sendMessage(CC.translate(msg));
            });
    }

    public void broadcast(Match match, FancyMessage msg) {
        Arrays.asList(match.getPlayer1(), match.getPlayer2()).forEach(player -> {
            if(Bukkit.getPlayer(player) != null)
                msg.send(Bukkit.getPlayer(player));
        });

        if(!match.getSpectators().isEmpty())
            match.getSpectators().values().forEach(spectator -> {
                if(Bukkit.getPlayer(spectator) != null)
                    msg.send(Bukkit.getPlayer(spectator));
            });
    }

}
