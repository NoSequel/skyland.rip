package rip.skyland.soup;

import org.bukkit.entity.Entity;
import rip.skyland.core.CorePlugin;
import rip.skyland.core.profile.Profile;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.command.CommandHandler;
import rip.skyland.core.util.redis.RedisHandler;
import rip.skyland.phase.Phase;
import rip.skyland.phase.PhaseSupplier;
import rip.skyland.phase.util.PlayerUtil;
import rip.skyland.practice.duel.AcceptCommand;
import rip.skyland.practice.duel.DuelCommand;
import rip.skyland.practice.match.MatchManager;
import rip.skyland.practice.spectate.SpectateCommands;
import rip.skyland.practice.spectate.SpectateManager;
import rip.skyland.soup.event.EventManager;
import rip.skyland.soup.kit.KitManager;
import rip.skyland.soup.kit.KitMenu;
import rip.skyland.practice.GameManager;
import rip.skyland.practice.match.inventory.CachedInventoryListener;
import rip.skyland.practice.queue.QueueManager;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.scoreboard.style.StyleManager;
import rip.skyland.soup.timers.timers.TimerManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import rip.skyland.soup.commands.*;
import rip.skyland.soup.event.commands.*;
import rip.skyland.soup.listeners.*;
import rip.skyland.soup.util.*;

import java.util.Arrays;

@Getter
@Setter
public class SoupPlugin extends JavaPlugin {

    @Getter
    private static SoupPlugin instance;

    private Config events;
    private EventManager eventManager;
    private QueueManager queueManager;
    private GameManager gameManager;
    private MatchManager matchManager;
    private SpectateManager spectateManager;
    private TimerManager timerManager;
    private Phase phase;

    public void onEnable() {
        instance = this;


        PlayerUtil.getOnlinePlayers().forEach(SoupProfile::new);

        new StyleManager();
        new KitManager();
        new MongoHandler(SoupPlugin.getInstance().getConfig().getString("MONGO.AUTH.USER"),
                SoupPlugin.getInstance().getConfig().getString("MONGO.AUTH.PASSWORD"),
                SoupPlugin.getInstance().getConfig().getString("MONGO.HOST"),
                SoupPlugin.getInstance().getConfig().getString("MONGO.MONGO_DATABASE"),
                SoupPlugin.getInstance().getConfig().getInt("MONGO.PORT")).setup();

        this.timerManager = new TimerManager();
        this.eventManager = new EventManager();
        this.queueManager = new QueueManager();
        this.gameManager = new GameManager();
        this.matchManager = new MatchManager();
        this.spectateManager = new SpectateManager();

        this.phase = new Phase(this, new PhaseSupplier(), true);

        this.events = new Config("events");

        executeSection("listeners", new Thread(this::loadListeners));
        executeSection("commands", new Thread(this::loadCommands));
        executeSection("locale", new Thread(this::setupLocale));

        optimize();

        new WaterTask().runTaskTimer(this, 1L, 1L);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(CorePlugin.getInstance(), () -> {
            long start = System.currentTimeMillis();

            PlayerUtil.getOnlinePlayers().forEach(player -> {
                Profile.getByUuid(player.getUniqueId()).save();
                SoupProfile.getByPlayer(player).save();

            });

            long finish = System.currentTimeMillis() - start;
            Bukkit.broadcastMessage(CC.translate("&e&l[Skylands] &eSaved &d" + Profile.getProfiles().size() + " &estats to Mongo database in &d" + finish + "ms&e."));
        }, 20 * 200L, 20 * 200L);
    }

    private void loadListeners() {
        Arrays.asList(
                new PlayerListener(),
                new ArcherKitListener(),
                new KitMenu(),
                new EventListeners(),
                new InteractListeners(),
                new CachedInventoryListener(),
                new DeathListener(),
                new SpectatorListener(),
                new StrengthNerfListener()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void loadCommands() {
        CommandHandler handler = new CommandHandler(this);

        handler.registerCommands(
                new KitsCommand(),
                new RespawnCommand(),
                new SetSpawnCommand(),
                new SetCornerCommand(),
                new ResetCommand(),
                new StyleCommand(),
                new OneVersusOneCommand(),

                new SumoCommand(),
                new HostCommand(),
                new JoinCommand(),
                new QuitCommand(),
                new SetLobbyCommand(),
                new InventoryCommand(),
                new SpectateCommands(),

                new AcceptCommand(),
                new DuelCommand()
                );

    }

    private void setupLocale() {
        Locale.KILL_INCREGMENT = this.getConfig().getInt("CREDITS.KILL_INCREGMENT");

        Locale.SPAWN_LOCATION_1 = ConfigUtil.getLocation("SPAWN.LOCATION_1");
        Locale.SPAWN_LOCATION_2 = ConfigUtil.getLocation("SPAWN.LOCATION_2");

        ConfigUtil.setLocation("SPAWN.ONE_VERSUS_ONE", new Location(Bukkit.getWorlds().get(0), 169, 89, 245));
        Locale.ONE_VS_ONE_SPAWN = ConfigUtil.getLocation("SPAWN.ONE_VERSUS_ONE");
        eventManager.setLobby(ConfigUtil.getLocation("EVENTS.LOBBY"));
    }

    private void optimize() {
        Bukkit.getWorlds().get(0).setDifficulty(Difficulty.EASY);
        Bukkit.getWorlds().get(0).setGameRuleValue("doMobSpawning", "false");

        Arrays.asList("arcade", "sumo").forEach(name -> {
            if(Bukkit.getWorld(name) == null) {
                WorldCreator creator = new WorldCreator(name);
                creator.type(WorldType.FLAT);
                creator.generator("2;0;1;");

                World world = creator.createWorld();
                world.setGameRuleValue("doMobSpawning", "false");
                world.setDifficulty(Difficulty.EASY);
            }
            else {
                Bukkit.unloadWorld(name, false);
                Bukkit.getScheduler().runTaskLater(this, () ->
                                FileUtils.deleteDirectory(Bukkit.getWorld(name).getWorldFolder())
                        , 40L);
            }
        });

        Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(Entity::remove));

    }

    private void executeSection(String section, Thread thread) {
        long start = System.currentTimeMillis();
        thread.start();

        Bukkit.getConsoleSender().sendMessage(CC.translate("&e&l[Skylands] &eLoaded section " + section + " &ein &d" + (System.currentTimeMillis()-start) + "ms&e."));
    }

    public void onDisable() {
        SoupProfile.getProfiles().forEach(SoupProfile::save);
    }
}
