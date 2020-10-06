package rip.skyland.phase;

import org.bukkit.entity.Player;
import rip.skyland.core.CoreAPI;
import rip.skyland.core.profile.Profile;
import rip.skyland.core.util.CC;
import rip.skyland.phase.adapter.PhaseAdapter;
import rip.skyland.phase.adapter.ScoreboardAdapter;
import rip.skyland.phase.tab.adapter.TabLayout;

import rip.skyland.phase.util.PlayerUtil;
import rip.skyland.practice.match.impl.ArcadeMatch;
import rip.skyland.practice.queue.QueueManager;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.event.Event;
import rip.skyland.soup.event.impl.SumoEvent;
import rip.skyland.soup.kit.KitManager;
import rip.skyland.soup.profiles.ProfileState;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.timers.PlayerTimer;
import rip.skyland.soup.util.FormatUtil;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class PhaseSupplier implements PhaseAdapter {

    @Override
    public ScoreboardAdapter getScoreboard(Player player) {
        ScoreboardAdapter layout = new ScoreboardAdapter();
        SoupProfile profile = SoupProfile.getByPlayer(player);


        String bar = "&7&m------------------";
        layout.setTitle("&6&lSkylandRIP &4[Beta]");
        layout.add(bar);

        if (profile == null)
            return layout;

        switch (profile.getState()) {
            case PLAYING: {
                layout.add(rip.skyland.core.util.CC.DARK_RED + "Kills: " + rip.skyland.core.util.CC.YELLOW + profile.getKills());
                layout.add(rip.skyland.core.util.CC.DARK_RED + "Deaths: " + rip.skyland.core.util.CC.YELLOW + profile.getDeaths());
                layout.add(rip.skyland.core.util.CC.DARK_RED + "Killstreak: " + rip.skyland.core.util.CC.YELLOW + profile.getKillstreak());

                layout.add(rip.skyland.core.util.CC.DARK_RED + "Highest Killstreak: " + rip.skyland.core.util.CC.YELLOW + profile.getHighestKillstreak());

                layout.add(rip.skyland.core.util.CC.DARK_RED + "Credits: " + rip.skyland.core.util.CC.YELLOW + profile.getCredits());
                layout.add(rip.skyland.core.util.CC.DARK_RED + "KDR: " + rip.skyland.core.util.CC.YELLOW + FormatUtil.getKdr(player));

                List<PlayerTimer> activeTimers = new ArrayList<>();

                SoupPlugin.getInstance().getTimerManager().getTimers().stream().filter(timer -> timer.hasCooldown(player) && timer.isOnScoreboard()).forEach(activeTimers::add);

                activeTimers.forEach(timer ->
                        layout.add(rip.skyland.core.util.CC.DARK_RED + timer.getScoreboardPrefix() + ": " + rip.skyland.core.util.CC.YELLOW + getRemainingTime(timer.getRemaining(player), true))
                );
            }
            break;

            case EVENT: {
                if (SoupPlugin.getInstance().getEventManager().getCurrentEvent() == null)
                    profile.setState(ProfileState.PLAYING);
                else {
                    Event event = SoupPlugin.getInstance().getEventManager().getCurrentEvent();
                    layout.add(rip.skyland.core.util.CC.ORANGE + "Event: " + rip.skyland.core.util.CC.YELLOW + event.getName());
                    layout.add(rip.skyland.core.util.CC.ORANGE + "Players: " + rip.skyland.core.util.CC.YELLOW + event.getPlayers().size() + '/' + event.getMaxPlayers());

                    if (!event.isStarted()) {
                        if (event.isStartedTimer())
                            layout.add(rip.skyland.core.util.CC.ORANGE + "Starting in: " + rip.skyland.core.util.CC.YELLOW + getRemainingTime(event.getStartTimer(), true));
                    } else {
                        if (event instanceof SumoEvent) {
                            layout.add(bar);

                            SumoEvent sumoEvent = (SumoEvent) event;

                            if (sumoEvent.getFighting().isEmpty() || sumoEvent.getFighting().size() < 2)
                                layout.add(rip.skyland.core.util.CC.ORANGE + "Waiting...");
                            else {

                                Player p1 = sumoEvent.getFighting().get(0);
                                Player p2 = sumoEvent.getFighting().get(1);
                                int ping = CoreAPI.getPing(p1);
                                int ping2 = CoreAPI.getPing(p2);

                                int cps = 0;
                                int cps2 = 0;
                                layout.add(rip.skyland.core.util.CC.ORANGE + p1.getName() + rip.skyland.core.util.CC.YELLOW + " vs " + rip.skyland.core.util.CC.ORANGE + p2.getName());
                                layout.add(rip.skyland.core.util.CC.YELLOW + "(" + rip.skyland.core.util.CC.ORANGE + "" + ping + "ms" + rip.skyland.core.util.CC.YELLOW + ") vs " + rip.skyland.core.util.CC.YELLOW + "(" + rip.skyland.core.util.CC.ORANGE + ping2 + "ms" + rip.skyland.core.util.CC.YELLOW + ")");
                                layout.add(rip.skyland.core.util.CC.YELLOW + "(" + rip.skyland.core.util.CC.ORANGE + "" + cps + "CPS" + rip.skyland.core.util.CC.YELLOW + ") vs " + rip.skyland.core.util.CC.YELLOW + "(" + rip.skyland.core.util.CC.ORANGE + cps2 + "CPS" + rip.skyland.core.util.CC.YELLOW + ")");

                            }
                        }
                    }
                }
            }
            break;

            case ONE_VS_ONE: {
                if (profile.getSpectatingMatch() != null) {
                    layout.add("&6&lKit: &f" + profile.getSpectatingMatch().getKit().getName());
                    layout.add("&c&lPlayers: &f" + Profile.getByUuid(profile.getSpectatingMatch().getPlayer1()).getName() + " vs " + Profile.getByUuid(profile.getSpectatingMatch().getPlayer2()).getName());
                    layout.add(bar);

                    return layout;
                }

                if (profile.getCurrentMatch() == null) {
                    int[] ints = new int[]{profile.getElo(KitManager.getByName("Buffed")), profile.getElo(KitManager.getByName("Arcade")), profile.getElo(KitManager.getByName("Sumo"))};
                    layout.add(rip.skyland.core.util.CC.DARK_RED + "Global Elo &a» " + rip.skyland.core.util.CC.YELLOW + median(ints));
                    layout.add(rip.skyland.core.util.CC.DARK_RED + "Buffed Elo &a» &e" + rip.skyland.core.util.CC.YELLOW + profile.getElo(QueueManager.getByName("Buffed", true).getKit()));
                    layout.add(rip.skyland.core.util.CC.DARK_RED + "Arcade Elo &a» &e" + rip.skyland.core.util.CC.YELLOW + profile.getElo(QueueManager.getByName("Arcade", true).getKit()));
                    layout.add(rip.skyland.core.util.CC.DARK_RED + "Sumo Elo &a» &e" + rip.skyland.core.util.CC.YELLOW + profile.getElo(QueueManager.getByName("Sumo", true).getKit()));
                    if (profile.getCurrentQueue() != null) {
                        layout.add(bar);
                        layout.add(rip.skyland.core.util.CC.DARK_RED + "Queued " + profile.getCurrentQueue().getName());
                        layout.add(rip.skyland.core.util.CC.YELLOW + "Time: &f" + getExpired(profile.getCurrentQueue().getJoinTime().get(player)));

                        if (profile.getCurrentQueue().isRanked())
                            layout.add(rip.skyland.core.util.CC.YELLOW + "ELO range: &f" + profile.getCurrentQueue().getEloRange().get(player)[0] + " - " + profile.getCurrentQueue().getEloRange().get(player)[1]);
                    }
                } else {
                    if(profile.getCurrentMatch() != null) {
                        if (profile.getCurrentMatch() instanceof ArcadeMatch) {
                            if (!profile.getCurrentMatch().isStarted()) {
                                layout.add("&6&lKit: &f???");
                                layout.add("&c&lOpponent: &f???");
                                layout.add(bar);

                                return layout;
                            }
                        }

                        if(profile.getCurrentMatch().getKit() != null)
                            layout.add("&6&lKit: &f" + profile.getCurrentMatch().getKit().getName());

                        if(profile.getCurrentMatch().getOpponent(player.getUniqueId()) != null)
                            layout.add("&c&lOpponent: &f" + profile.getCurrentMatch().getOpponent(player.getUniqueId()).getName());
                    }
                }
            }
            break;
        }


        layout.add(bar);

        return layout;
    }

    @Override
    public TabLayout getTablist(Player player) {
        TabLayout layout = new TabLayout();
        SoupProfile profile = SoupProfile.getByPlayer(player);

        layout.add(0, 0, "&3&lwww.skyland.rip");
        layout.add(1, 0, "&3&lskyland.rip");
        layout.add(2, 0, "&3&lstore.skyland.rip");

        if(profile.getCurrentMatch() != null) {
            layout.add(0, 2, "&a&lYou");
            layout.add(0, 3, CC.GREEN + player.getName(), PlayerUtil.getPing(player));

            Player opponent = profile.getCurrentMatch().getOpponent(player.getUniqueId());
            if(opponent == null)
                opponent = player;


            layout.add(2, 2, "&c&lOpponent");
            layout.add(2, 3, CC.RED + opponent.getName(), PlayerUtil.getPing(opponent));
        } else if(profile.getState().equals(ProfileState.PLAYING)) {

            AtomicInteger x = new AtomicInteger(0);
            AtomicInteger y = new AtomicInteger(5);

            if(profile.getKit() != null) {
                layout.add(0, 2, "&b&lKit");
                layout.add(0, 3, "&f" + profile.getKit().getName());
            } else {
                layout.add(0, 2, "&b&lKit");
                layout.add(0, 3, "&fNone");
            }

            layout.add(2, 2, "&b&lLast Kit");
            layout.add(2, 3, profile.getLastKit() == null ? "&fNone" : "&f" + profile.getLastKit().getName());

            PlayerUtil.getOnlinePlayers().stream().filter(online -> Profile.getByUuid(online.getUniqueId()) != null).forEach(online -> {
                if(x.get() < 2) {
                    layout.add(x.getAndIncrement(), y.get(), Profile.getByUuid(online.getUniqueId()).getDisplayName(), PlayerUtil.getPing(online));
                } else {
                    layout.add(2, y.getAndIncrement(), Profile.getByUuid(online.getUniqueId()).getDisplayName(), PlayerUtil.getPing(online));
                    x.set(0);
                }
            });
        } else if(profile.getState().equals(ProfileState.ONE_VS_ONE)) {
            layout.add(1, 2, "&3&lYour Rankings");
            layout.add(0, 3, "&bBuffed Elo - " + profile.getElo(KitManager.getByName("Buffed")));
            layout.add(1, 3, "&bArcade Elo - " + profile.getElo(KitManager.getByName("Arcade")));
            layout.add(2, 3, "&bSumo Elo - " + profile.getElo(KitManager.getByName("Sumo")));
            layout.add(0, 4, "&bNoDebuff - " + profile.getElo(KitManager.getByName("NoDebuff")));

            AtomicInteger x = new AtomicInteger(0);
            AtomicInteger y = new AtomicInteger(6);

            PlayerUtil.getOnlinePlayers().stream().filter(online -> Profile.getByUuid(online.getUniqueId()) != null).forEach(online -> {
                if(x.get() < 2) {
                    layout.add(x.getAndIncrement(), y.get(), Profile.getByUuid(online.getUniqueId()).getDisplayName(), PlayerUtil.getPing(online));
                } else {
                    layout.add(2, y.getAndIncrement(), Profile.getByUuid(online.getUniqueId()).getDisplayName(), PlayerUtil.getPing(online));
                    x.set(0);
                }
            });
        }

        return layout;
    }

    public static String getRemainingTime(float millis, boolean trailing) {
        DateFormat format = new SimpleDateFormat((millis >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") + "mm:ss");
        NumberFormat formatter = new DecimalFormat("#0.0");
        return trailing ? formatter.format(millis / 1000) : format.format(millis);
    }

    private String getExpired(float millis) {
        DateFormat format = new SimpleDateFormat((millis * 1000 >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") + "mm:ss");

        return format.format((millis * 1000));
    }

    private long median(int[] l) {
        Arrays.sort(l);
        int middle = l.length / 2;
        if (l.length % 2 == 0) {
            long left = l[middle - 1];
            long right = l[middle];
            return (left + right) / 2;
        } else {
            return l[middle];
        }
    }

}
