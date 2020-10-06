package rip.skyland.soup.event;

import rip.skyland.core.util.CC;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.event.impl.SumoEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@Getter
@Setter
public abstract class Event {

    private boolean started, startedTimer;
    private long startTimer;

    public abstract String getName();
    public abstract String getDescription();

    public abstract int getMaxPlayers();
    public abstract int getMinPlayers();
    public abstract List<Player> getPlayers();

    public abstract ItemStack[] getItems();
    public abstract boolean loseDamage();

    public void broadcast(String message) {
        getPlayers().forEach(player -> player.sendMessage(CC.translate(message)));
    }

    public void start() {
        this.startTimer = 30000L;
        new BukkitRunnable() {

            @Override
            public void run() {
                SoupPlugin.getInstance().getEventManager().setCurrentEvent(Event.this);
                if(getPlayers().size() >= getMinPlayers()) {
                    if(!startedTimer) {
                        setStartedTimer(true);
                        broadcast("&6&lMinimum players reached! &ePlayers have &d2 &eseconds to join before starting.");
                    }

                    if(getPlayers().size() == getMaxPlayers() && startTimer > 15000) {
                        broadcast("&6&lEvent has filled up! &eYou have &d15 &eseconds before getting teleported.");
                        startTimer = 15000L;
                    }

                    if(startTimer == 0) {

                        if(EventManager.getByClass(Event.this.getClass()) instanceof SumoEvent) {
                            SumoEvent e = (SumoEvent) EventManager.getByClass(Event.this.getClass());
                            e.getPlayers().forEach(player -> player.teleport(e.getSpawnLocation()));
                            e.startTask();
                            e.setStarted(true);
                        }


                        Bukkit.broadcastMessage(CC.translate("&6The event &9" + getName() + " &6has started. &e(" + getPlayers().size() + '/' + getMaxPlayers() + ')'));
                        this.cancel();
                    }

                    startTimer-=100;
                }
            }
        }.runTaskTimer(SoupPlugin.getInstance(), 2L, 2L);
    }


}
