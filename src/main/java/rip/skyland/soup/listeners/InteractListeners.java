package rip.skyland.soup.listeners;

import rip.skyland.core.util.CC;
import rip.skyland.core.util.PlayerUtil;
import rip.skyland.core.util.menu.Button;
import rip.skyland.core.util.menu.Menu;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.practice.queue.Queue;
import rip.skyland.practice.queue.QueueManager;
import rip.skyland.soup.profiles.ProfileState;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.timers.abilities.event.AbilityUseEvent;
import rip.skyland.soup.util.Locale;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;


import java.util.*;
import java.util.stream.IntStream;

public class InteractListeners implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getItem() == null || event.getItem().getItemMeta() == null)
            return;

        Player player = event.getPlayer();
        SoupProfile profile = SoupProfile.getByPlayer(player);

        if(profile.getState().equals(ProfileState.ONE_VS_ONE)) {
            if(profile.isSpectating()) {
                if(event.getItem().getType().equals(Material.FIRE))
                    SoupPlugin.getInstance().getSpectateManager().unspectateMatch(player, profile.getSpectatingMatch());
            }

            if(event.getItem().getType().equals(Material.SLIME_BALL))
                getInventory(false).openMenu(player);
            else if(event.getItem().getType().equals(Material.MAGMA_CREAM))
                getInventory(true).openMenu(player);
            else if(event.getItem().getType().equals(Material.INK_SACK) && profile.getCurrentMatch() == null && profile.getCurrentQueue() != null)
                SoupPlugin.getInstance().getQueueManager().leaveQueue(profile.getCurrentQueue(), player);
        }
    }

    @EventHandler
    public void onAbility(AbilityUseEvent event) {
        Player player = event.getPlayer();

        if(PlayerUtil.inArea(player.getLocation(), Locale.SPAWN_LOCATION_1, Locale.SPAWN_LOCATION_2)) {
            player.sendMessage(CC.translate("&cYou can't use that ability here."));
            event.setCancelled(true);
        }

    }

    private Menu getInventory(boolean ranked) {
        return new Menu() {
            @Override
            public boolean isAutoUpdate() { return true; }

            @Override
            public String getTitle(Player player) {
                return CC.translate("&9&lJoin " + (ranked ? "Ranked" : "Unranked"));
            }

            @Override
            public Map<Integer, Button> getButtons(Player player) {
                Map<Integer, Button> buttons = new HashMap<>();

                List<rip.skyland.practice.queue.Queue> queues = new ArrayList<>();

                if(ranked)
                    QueueManager.getQueues().stream().filter(rip.skyland.practice.queue.Queue::isRanked).forEach(queues::add);
                else
                    QueueManager.getQueues().stream().filter(queue -> !queue.isRanked()).forEach(queues::add);


                IntStream.range(0, queues.size()).forEach(i ->
                    buttons.put(i, new Button() {
                        Queue queue = queues.get(i);

                        @Override
                        public String getName(Player player) {
                            return CC.translate(queue.getDisplayName());
                        }

                        @Override
                        public List<String> getDescription(Player player) {
                            return CC.translate(
                                    Arrays.asList(
                                            "",
                                            "&b&l&n" + queue.getName(),
                                            " &AIn fights: &f" + queue.getPlaying().size(),
                                            " &aIn queue: &f" + queue.getQueueing().size(),
                                            "",
                                            "&eClick here to select " + queue.getDisplayName(),
                                            ""
                                    )
                            );
                        }

                        @Override
                        public Material getMaterial(Player player) {
                            return queue.getIcon().getType();
                        }

                        @Override
                        public byte getDamageValue(Player player) {
                            return 0;
                        }

                        @Override
                        public void clicked(Player player, int i, ClickType clickType, int i1) {
                            SoupPlugin.getInstance().getQueueManager().joinQueue(queue, player);
                            player.closeInventory();
                        }
                    }));

                return buttons;
            }
        };
    }

}
