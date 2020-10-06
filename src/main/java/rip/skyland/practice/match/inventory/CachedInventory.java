package rip.skyland.practice.match.inventory;

import rip.skyland.core.profile.Profile;
import rip.skyland.core.util.ItemBuilder;
import rip.skyland.practice.match.Match;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.IntStream;

public class CachedInventory {

    private static List<CachedInventory> cachedInventories = new ArrayList<>();
    private List<ItemStack> armor, items;

    @Getter
    private UUID player;
    private Profile profile;
    private Match match;

    private double health;

    public CachedInventory(Profile profile, Match match) {
        this.player = profile.getUuid();
        this.match = match;

        this.profile = profile;
        this.health = 0;

        this.armor = Collections.singletonList(new ItemBuilder(Material.FEATHER).displayName("haha bitch").create());
        this.items = Collections.singletonList(new ItemBuilder(Material.FEATHER).displayName("haha bitch").create());

        if(getByUuid(player) != null)
            cachedInventories.remove(getByUuid(player));

        cachedInventories.add(this);
    }

    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "Inventory of " + profile.getName());

        IntStream.range(0, items.size()).forEach(i -> {
            ItemStack item = items.get(i);

            if (item != null) {
                inventory.setItem(i, item);
            }
        });

        IntStream.range(0, armor.size()).forEach(i ->
                inventory.setItem(i + 36, armor.get(i))
        );


        DecimalFormat format = new DecimalFormat("##");
        inventory.setItem(45, new ItemBuilder(Material.SPECKLED_MELON).amount((int) health).displayName("&a" + format.format(health) + "/10").create());

        inventory.setItem(53, new ItemBuilder(Material.TRIPWIRE_HOOK)
        .displayName("&aView " + match.getOpponent(player).getName() + "'s inventory")
        .lore(Arrays.asList("", "&eSwap your view to " + match.getOpponent(player).getName() + "'s inventory")).create());
        return inventory;
    }

    public CachedInventory getNextInventory() {
        return getByUuid(match.getOpponent(profile.getUuid()).getUniqueId());
    }

    public static CachedInventory getByUuid(UUID player) { return cachedInventories.stream().filter(inv -> inv.getPlayer().equals(player)).findFirst().orElse(null); }

}
