package rip.skyland.soup.kit;

import org.bukkit.Bukkit;
import rip.skyland.core.util.CC;
import rip.skyland.practice.kits.BuffedKit;
import rip.skyland.practice.kits.NoDebuffKit;
import rip.skyland.practice.kits.SpeedKit;
import rip.skyland.practice.kits.StandardKit;
import lombok.Getter;
import rip.skyland.soup.kit.impl.*;
import rip.skyland.soup.kit.impl.ability.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitManager {

    @Getter
    private static List<Kit> kits = new ArrayList<>();

    public KitManager() {

        Arrays.asList(
                new StandardKit(),
                new BuffedKit(),
                new SpeedKit(),
                new SumoKit(),

                new PvpKit(),
                new ArcherKit(),
                new ProKit(),
                new AvatarKit(),
                new BarbarianKit(),
                new ChemistKit(),
                new FishermanKit(),
                new KidnapperKit(),
                new MarioKit(),
                new WarriorKit(),
                new NoDebuffKit()
        ).forEach(this::registerKit);

    }

    private void registerKit(Kit kit) {
        kits.add(kit);
    }

    public static Kit getByName(String name) { return kits.stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null); }
}
