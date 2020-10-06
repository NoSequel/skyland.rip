package rip.skyland.soup.util;

import rip.skyland.core.CorePlugin;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.ItemBuilder;
import rip.skyland.soup.profiles.ProfileState;
import rip.skyland.soup.profiles.SoupProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Items {

    private static ItemStack[] lobbyItems(Player player) {
        SoupProfile profile = SoupProfile.getByPlayer(player);
        String previousKit = profile.getLastKit() == null ? "" : CC.translate("&a: " + profile.getLastKit().getName());

        return new ItemStack[] {
                new ItemBuilder(Material.BOOK).displayName("&eKit Selector").create(),
                new ItemBuilder(Material.WATCH).displayName("&ePrevious Kit" + previousKit).create(),
                null,
                null,
                new ItemBuilder(Material.EMERALD).displayName("&dShop").create(),
                null,
                null,
                null,
                new ItemBuilder(Material.EYE_OF_ENDER).displayName("&e1v1 Arena").create(),
        };
    }

    private static ItemStack[] lobbyMatchItems() {
        return new ItemStack[] {
                new ItemBuilder(Material.SLIME_BALL).displayName("&9&l» &6&lUnranked Queue &9&l«").create(),
                new ItemBuilder(Material.MAGMA_CREAM).displayName("&9&l» &6&lRanked Queue &9&l«").create(),
                null,
                new ItemBuilder(Material.DIAMOND_SWORD).displayName("&aStandard").create(),
                new ItemBuilder(Material.IRON_SWORD).displayName("&2Elite").create(),
                new ItemBuilder(Material.DIAMOND_SWORD).addEnchantment(Enchantment.DURABILITY, 3).displayName("&5Buffed").create(),
                new ItemBuilder(Material.SUGAR).displayName("&eSpeed").create(),
                new ItemBuilder(Material.MUSHROOM_SOUP).displayName("&6Refill").create(),
                new ItemBuilder(Material.EMERALD).displayName("&9&l» &6&lToggle Visibility &9&l«").create()
        };
    }

    public static ItemStack[] lobbyMatchLeaveItems() {
        ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) 1);

        return new ItemStack[] {
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                new ItemBuilder(item).displayName("&cLeave Queue").create()
        };
    }

    public static ItemStack[] spectateItems() {
        return new ItemStack[] {
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                new ItemBuilder(Material.FIRE).displayName("&cStop spectating").create()
        };
    }

    public static void resetPlayer(Player player) {
        SoupProfile profile = SoupProfile.getByPlayer(player);

        if(!profile.getState().equals(ProfileState.ONE_VS_ONE)) {
            leave1v1(player);
        } else {
            join1v1(player);
        }
    }

    public static void join1v1(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.getInventory().setContents(lobbyMatchItems());
        player.teleport(Locale.ONE_VS_ONE_SPAWN);

        SoupProfile.getByPlayer(player).setState(ProfileState.ONE_VS_ONE);
        SoupProfile.getByPlayer(player).clearCooldowns();

        SoupProfile profile = SoupProfile.getByPlayer(player);
        profile.setLastKit(profile.getKit());
        profile.setKit(null);

        Bukkit.getScheduler().runTaskLaterAsynchronously(CorePlugin.getInstance(), player::updateInventory, 2L);
    }

    public static void leave1v1(Player player) {
        SoupProfile profile = SoupProfile.getByPlayer(player);

        if (profile.getKit() != null) {
            profile.setLastKit(profile.getKit());
            profile.setKit(null);
        }
        profile.setState(ProfileState.PLAYING);
        profile.setFallDamage(true);
        profile.clearCooldowns();

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.getInventory().setContents(Items.lobbyItems(player));

        player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
    }

}
