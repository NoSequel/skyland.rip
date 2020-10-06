package rip.skyland.soup.util.menuis;

import rip.skyland.core.CorePlugin;
import rip.skyland.core.profile.Profile;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.menu.Button;
import rip.skyland.core.util.menu.Menu;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.event.Event;
import rip.skyland.soup.event.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class HostMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Host event";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int i = 0;
        EventManager eventManager = SoupPlugin.getInstance().getEventManager();
        for (Event event : EventManager.getEvents()) {
            buttons.put(i, new Button() {
                @Override
                public String getName(Player player) {
                    return CC.translate("&9" + event.getName() + " Event");
                }

                @Override
                public List<String> getDescription(Player player) {
                    String[] strings = event.getDescription().split(":");
                    List<String> std = new ArrayList<>(Collections.singletonList(""));
                    Arrays.stream(strings).forEach(st -> std.add("&7" + st));
                    std.addAll(Arrays.asList("", "&aClick here to host.", ""));

                    return CC.translate(std);
                }

                @Override
                public Material getMaterial(Player player) {
                    return Material.PAPER;
                }

                @Override
                public byte getDamageValue(Player player) {
                    return 0;
                }

                @Override
                public void clicked(Player player, int i, ClickType clickType, int i1) {
                    if(!eventManager.isBusy()) {
                        eventManager.setBusy(true);
                        event.start();

                        Bukkit.broadcastMessage(
                                CC.translate(Profile.getByUuid(player.getUniqueId()).getDisplayName() + " &eis hosting the &9" + event.getName() + " &eevent for  a prize of &a400 credits. Type &6&l/join &eto join.\n&e(1/" + event.getMaxPlayers() + ')')
                        );


                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                if(!event.isStarted()) {
                                    Bukkit.broadcastMessage(
                                            CC.translate(Profile.getByUuid(player.getUniqueId()).getDisplayName() + " &eis hosting the &9" + event.getName() + " &eevent for  a prize of &a400 credits. Type &6&l/join &eto join.\n&e(" + event.getPlayers().size() + '/' + event.getMaxPlayers() + ')')
                                    );

                                } else {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimerAsynchronously(CorePlugin.getInstance(), 240L, 240L);
                    } else
                        player.sendMessage(CC.translate("&cAn event is already busy..."));

                }
            });
            i++;
        }

        return buttons;
    }
}
