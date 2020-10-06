package rip.skyland.soup.timers.abilities.avatar;

import org.bukkit.entity.Player;
import rip.skyland.core.util.CC;
import lombok.Getter;

import org.bukkit.event.Listener;
import rip.skyland.soup.listeners.MoveListener;
import rip.skyland.soup.timers.PlayerTimer;



/**
 * @see MoveListener
 */
@Getter
public class JumpAbility extends PlayerTimer implements Listener {
    private String scoreboardPrefix;
    public JumpAbility() {
        super(false, "Jump Avatar", 10000L);

        scoreboardPrefix = null;
    }

    @Override
    public void onExpire(Player player) {
        player.sendMessage(CC.translate("&eYou may now use &dAvatar Jump&e."));
    }

}
