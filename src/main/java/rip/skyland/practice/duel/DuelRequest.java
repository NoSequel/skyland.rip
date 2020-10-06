package rip.skyland.practice.duel;

import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.skyland.core.util.CC;
import rip.skyland.practice.queue.Queue;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.util.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DuelRequest {

    private static List<DuelRequest> duelRequests = new ArrayList<>();

    private Player requester, target;
    private Queue ladder;

    private boolean expired;

    public DuelRequest(Player requester, Player target, Queue ladder) {
        target.sendMessage(CC.translate("&b" + requester.getName() + " &ehas sent you a " + ladder.getDisplayName() + " &eduel."));
        new MessageBuilder("&6Click here or type &b/accept " + requester.getName() + " &6to accept the invite").click(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + requester.getName())).send(target);

        requester.sendMessage(CC.translate("&eSuccessfully sent a " + ladder.getDisplayName() + " &eduel invite to &b" + target.getName()));

        SoupProfile profile = SoupProfile.getByPlayer(requester);
        SoupProfile targetProfile = SoupProfile.getByPlayer(target);

        this.requester = requester;
        this.target = target;
        this.ladder = ladder;

        duelRequests.add(this);
        profile.getOutgoingRequests().add(this);
        targetProfile.getIncomingRequests().add(this);

        Bukkit.getScheduler().scheduleAsyncDelayedTask(SoupPlugin.getInstance(), () -> {
            expired = true;
            duelRequests.remove(this);

            profile.getOutgoingRequests().remove(this);
            targetProfile.getIncomingRequests().remove(this);
        }, 20*120L);
    }

    public void accept() {
        SoupPlugin.getInstance().getQueueManager().doMatch(ladder, requester, target, true);

        this.expired = true;
        SoupProfile.getByPlayer(requester).getOutgoingRequests().remove(this);
        SoupProfile.getByPlayer(target).getIncomingRequests().remove(this);

    }

}
