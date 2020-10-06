package rip.skyland.phase;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import rip.skyland.phase.adapter.PhaseAdapter;
import rip.skyland.phase.tab.TablistManager;
import rip.skyland.phase.thread.NametagThread;
import rip.skyland.phase.thread.ScoreboardThread;

@Getter
public class Phase {

    @Getter
    private static Phase instance;

    private JavaPlugin plugin;
    private PhaseAdapter adapter;
    private TablistManager tablistManager;

    public Phase(JavaPlugin plugin, PhaseAdapter adapter, boolean heartsNameTag) {
        this.plugin = plugin;
        this.adapter = adapter;
        this.tablistManager = new TablistManager(plugin, adapter, 200L, this);

        new ScoreboardThread(this);

        if(heartsNameTag)
            new NametagThread();
    }



}
