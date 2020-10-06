package rip.skyland.phase.adapter;

import org.bukkit.entity.Player;
import rip.skyland.phase.tab.adapter.TabLayout;

public interface PhaseAdapter {

    ScoreboardAdapter getScoreboard(Player player);
    TabLayout getTablist(Player player);

}
