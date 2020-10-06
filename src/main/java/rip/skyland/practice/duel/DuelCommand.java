package rip.skyland.practice.duel;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.core.util.command.annotation.Param;
import rip.skyland.core.util.menu.Button;
import rip.skyland.core.util.menu.Menu;
import rip.skyland.practice.queue.Queue;
import rip.skyland.practice.queue.QueueManager;
import rip.skyland.soup.profiles.ProfileState;
import rip.skyland.soup.profiles.SoupProfile;

import java.util.*;
import java.util.stream.IntStream;

public class DuelCommand {

    @Command(names={"duel"})
    public void execute(Player player, @Param(name="player") Player target) {
        if(SoupProfile.getByPlayer(target).getCurrentMatch() != null || !SoupProfile.getByPlayer(target).getState().equals(ProfileState.ONE_VS_ONE)) {
            player.sendMessage(CC.translate("&cThat player is not in the lobby!"));
            return;
        }

        if(SoupProfile.getByPlayer(player).getOutgoingRequests().stream().filter(request -> request.getTarget().equals(target)).findFirst().orElse(null) != null) {
            player.sendMessage(CC.translate("&cYou already sent this player a request."));
            return;
        }

        new Menu() {
            @Override
            public String getTitle(Player player) {
                return CC.translate("&9&lSelect a kit type...");
            }

            @Override
            public Map<Integer, Button> getButtons(Player player) {
                Map<Integer, Button> buttons = new HashMap<>();
                List<Queue> queues = new ArrayList<>();
                QueueManager.getQueues().stream().filter(Queue::isDuelQueue).forEach(queues::add);

                IntStream.range(0, queues.size()).forEach(i ->
                        buttons.put(i, new Button() {
                            Queue queue = queues.get(i);
                            @Override
                            public String getName(Player player) {
                                return CC.translate(queue.getDisplayName());
                            }

                            @Override
                            public List<String> getDescription(Player player) {
                                return CC.translate(Arrays.asList(
                                        "",
                                        "&eClick here to select " + queue.getDisplayName() + "&e."
                                ));
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
                                if(target == null) {
                                    player.sendMessage(CC.translate("&cThat player does not exist."));
                                    return;
                                }

                                new DuelRequest(player, target, queue);
                            }
                        }));

                return buttons;
            }
        }.openMenu(player);
    }

}
