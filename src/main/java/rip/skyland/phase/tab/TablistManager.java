package rip.skyland.phase.tab;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import rip.skyland.phase.Phase;
import rip.skyland.phase.adapter.PhaseAdapter;
import rip.skyland.phase.thread.TablistThread;
import rip.skyland.phase.util.PlayerUtil;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class TablistManager implements Listener {

	static TablistManager INSTANCE;

	@Getter
	private final JavaPlugin plugin;

	private final Map<UUID, Tablist> tablists;

	PhaseAdapter supplier;

	private int updateTaskId;
	private Phase phase;

	public TablistManager(JavaPlugin plugin, PhaseAdapter supplier, long updateTime, Phase phase) {
		TablistManager.INSTANCE = this;
		this.tablists = new ConcurrentHashMap<>();
		this.supplier = supplier;
		this.plugin = plugin;
		this.phase = phase;

		updateTaskId = Bukkit.getScheduler().runTaskAsynchronously(plugin, new TablistThread(phase, updateTime)).getTaskId();

		Bukkit.getPluginManager().registerEvents(this, plugin);
		PlayerUtil.getOnlinePlayers().forEach(player -> getTablist(player, true));
	}

	public Tablist getTablist(Player player) {
		return getTablist(player, false);
	}

	private Tablist getTablist(Player player, boolean create) {
		UUID uniqueId = player.getUniqueId();
		Tablist tablist = tablists.get(uniqueId);
		if (tablist == null && create) {
			tablists.put(uniqueId, tablist = new Tablist(player));
		}
		return tablist;
	}

	@SuppressWarnings("unused")
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Bukkit.getScheduler().runTask(phase.getPlugin(), () -> getTablist(player, true));
	}

	@SuppressWarnings("unused")
	@EventHandler
	public void onDisable(PluginDisableEvent event) {
		if (event.getPlugin() == plugin) {
			tablists.forEach((id, tablist) ->
				tablist.hideFakePlayers().clear()
			);
			tablists.clear();
			HandlerList.unregisterAll(this);
			if (updateTaskId != -1)
				Bukkit.getScheduler().cancelTask(updateTaskId);

		}
	}

	@SuppressWarnings("unused")
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UUID uniqueId = player.getUniqueId();
		Tablist tablist;
		if ((tablist = tablists.remove(uniqueId)) != null)
			tablist.hideFakePlayers().clear();

	}

}
