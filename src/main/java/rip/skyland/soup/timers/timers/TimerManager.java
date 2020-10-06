package rip.skyland.soup.timers.timers;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.timers.PlayerTimer;
import rip.skyland.soup.timers.abilities.*;
import rip.skyland.soup.timers.abilities.avatar.JumpAbility;
import rip.skyland.soup.timers.abilities.avatar.WatergunAbility;
import rip.skyland.soup.timers.abilities.barbarian.BarbarianAbility;
import rip.skyland.soup.timers.abilities.chemist.ChemistAbility;
import rip.skyland.soup.timers.timers.impl.CombatTimer;
import rip.skyland.soup.timers.timers.impl.EnderpearlTimer;
import rip.skyland.soup.timers.timers.impl.SpawnWarpTimer;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TimerManager {

    private List<PlayerTimer> timers = new ArrayList<>();

    private CombatTimer combatTimer;
    private EnderpearlTimer enderpearlTimer;
    private SpawnWarpTimer spawnWarpTimer;

    // abilities
    private FishermanAbility fishermanAbility;
    private KidnapperAbility kidnapperAbility;
    private MarioAbility marioAbility;
    private WarriorAbility warriorAbility;
    private BarbarianAbility barbarianAbility;
    private JumpAbility jumpAbility;
    private WatergunAbility watergunAbility;
    private ChemistAbility chemistAbility;

    public TimerManager() {
        register((enderpearlTimer = new EnderpearlTimer()));
        register((combatTimer = new CombatTimer()));
        register((spawnWarpTimer = new SpawnWarpTimer()));

        // abilities
        register((fishermanAbility = new FishermanAbility()));
        register((kidnapperAbility = new KidnapperAbility()));
        register((marioAbility = new MarioAbility()));
        register((chemistAbility = new ChemistAbility()));
        register((warriorAbility = new WarriorAbility()));
        register((barbarianAbility = new BarbarianAbility()));
        register((jumpAbility = new JumpAbility()));
        register((watergunAbility = new WatergunAbility()));
    }

    private void register(PlayerTimer timer) {
        timers.add(timer);

        if(timer instanceof Listener)
            Bukkit.getPluginManager().registerEvents((Listener) timer, SoupPlugin.getInstance());
    }

}
