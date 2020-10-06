package rip.skyland.soup.listeners;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import rip.skyland.core.profile.Profile;
import rip.skyland.core.util.CC;

import rip.skyland.core.util.PlayerUtil;

import rip.skyland.phase.entry.PhaseBoard;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.event.EventManager;
import rip.skyland.soup.kit.KitManager;
import rip.skyland.soup.profiles.ProfileState;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.util.Items;
import rip.skyland.soup.util.Locale;
import rip.skyland.soup.util.PotionUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;

import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.IntStream;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new SoupProfile(player);

        Profile profile = Profile.getByUuid(player.getUniqueId());

        if(PhaseBoard.getByPlayer(player) == null)
            new PhaseBoard(player, SoupPlugin.getInstance().getPhase());

        Items.resetPlayer(player);

        PlayerUtil.getOnlinePlayers().stream().filter(t -> SoupProfile.getByPlayer(t).getCurrentMatch() != null).forEach(t -> SoupPlugin.getInstance().getGameManager().hidePlayer(t, player));

        player.setExp(0.0F);
        player.setLevel(0);

        player.setWalkSpeed(0.2F);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));

        MoveListener.checkPlayerMove(player);
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if((event.getEntity() instanceof EntityMonster || event.getEntity() instanceof EntityAnimal) && event.getEntityType() != EntityType.SILVERFISH)
            event.setCancelled(true);
    }



    @EventHandler(priority= EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        SoupProfile profile = SoupProfile.getByPlayer(event.getPlayer());
        EventManager manager = SoupPlugin.getInstance().getEventManager();

        if(manager.isBusy()) {
            if(manager.getCurrentEvent().getPlayers().contains(event.getPlayer()))
                manager.quit(event.getPlayer());
        }

        if(profile == null)
            return;

        if(profile.getCurrentMatch() != null)
            profile.getCurrentMatch().handleDeath(profile.getPlayer().getUniqueId(), true);

        if(profile.getCurrentQueue() != null)
            SoupPlugin.getInstance().getQueueManager().leaveQueue(profile.getCurrentQueue(), event.getPlayer());

        if(profile.getSpectatingMatch() != null)
            SoupPlugin.getInstance().getSpectateManager().unspectateMatch(event.getPlayer(), profile.getSpectatingMatch());

        profile.destroy();
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if(event.getTarget() instanceof Player) {
            if(SoupProfile.getByPlayer((Player) event.getTarget()).getTamed().contains(event.getEntity())) {
                Player player = (Player) event.getTarget();

                List<Player> nearbyPlayers = new ArrayList<>();
                player.getWorld().getEntities().stream().filter(entity -> entity instanceof Player).forEach(entity -> nearbyPlayers.add((Player) entity));
                Comparator<Entity> comparator = Comparator.comparing(entity -> player.getLocation().distance(entity.getLocation()));
                nearbyPlayers.sort(comparator);

                event.setTarget(nearbyPlayers.get(0));
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if(SoupProfile.getByPlayer(event.getPlayer()).getCurrentMatch() == null)
            Items.resetPlayer(event.getPlayer());
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        int id = event.getBlock().getTypeId();
        if(id == 8 || id == 9) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player player = event.getEntity();
        player.setExp(0.0F);
        player.setLevel(0);

        event.setDroppedExp(0);
        runCheck();
        event.setDeathMessage(null);
        SoupProfile profile = SoupProfile.getByPlayer(player);

        if (profile.getCurrentMatch() != null) {
            profile.getCurrentMatch().handleDeath(player.getUniqueId(), false);
            SoupPlugin.getInstance().getTimerManager().getTimers().stream().filter(timer -> timer.hasCooldown(player)).forEach(timer -> timer.cancel(player));
            if(player.getKiller() != null)
                player.getKiller().setHealth(20);
        } else {

            Player killer = player.getKiller();

            SoupProfile k = SoupProfile.getByPlayer(killer);


            if (player.getKiller() == null) {
                profile.setKillstreak(0);
                return;
            }

            if (k.getKit() == null || !k.getKit().equals(KitManager.getByName("Archer")))
                killer.sendMessage(CC.translate("&9You have killed " + profile.getProfile().getDisplayName() + " &9for &a" + Locale.KILL_INCREGMENT + " credits"));

            player.sendMessage(CC.translate("&cYou have been killed by " + k.getProfile().getDisplayName()));

            if(!player.equals(k.getLastKill())) {
                k.setCredits(k.getCredits() + Locale.KILL_INCREGMENT);
                k.setKillstreak(k.getKillstreak() + 1);
                k.setKills(k.getKills() + 1);

                profile.setDeaths(profile.getDeaths() + 1);
            } else {
                killer.sendMessage(CC.translate("&c&lWarning! &eYou have killed &4" + player.getName() + " &e" + k.getTimesKilled() + " times in a row. No statistics were changed."));
            }

            SoupPlugin.getInstance().getTimerManager().getTimers().stream().filter(timer -> timer.hasCooldown(player)).forEach(timer -> timer.onExpire(player));

            if (k.getHighestKillstreak() < k.getKillstreak()) {
                k.setHighestKillstreak(k.getKillstreak());
                killer.sendMessage(CC.translate("&a&lNew record! &eYou're on a killstreak of &d" + k.getHighestKillstreak() + "&e!"));
            }

            if (profile.getKillstreak() >= 5)
                Bukkit.broadcastMessage(CC.translate(k.getProfile().getDisplayName() + " &ehas ended " + profile.getProfile().getDisplayName() + "&e's killstreak of &d" + profile.getKillstreak() + CC.YELLOW + '!'));

            executeKillstreak(k);

            profile.setKillstreak(0);


        }
    }

    private void executeKillstreak(SoupProfile profile) {
        if(profile.getKillstreak() == 5 || profile.getKillstreak() == 20 || profile.getKillstreak() == 35) {
            Bukkit.broadcastMessage(CC.translate(profile.getProfile().getDisplayName() + " &ehas gotten a killstreak of &d" + profile.getKillstreak() + " &eand received &7Full Repair."));
            if(profile.getPlayer().getInventory().getArmorContents() != null)
                Arrays.stream(profile.getPlayer().getInventory().getArmorContents()).forEach(item -> item.setDurability((short)0));
        }

        if(profile.getKillstreak() == 10 || profile.getKillstreak() == 25 || profile.getKillstreak() == 40) {
            Bukkit.broadcastMessage(CC.translate(profile.getProfile().getDisplayName() + " &ehas gotten a killstreak of &d" + profile.getKillstreak() + " &eand received &dRegenV"));
            ItemStack itemStack = PotionUtil.getPotionItemStack(PotionType.REGEN, 5, true, false, "Potion of Regeneration");
            profile.getPlayer().getInventory().setItem(8, itemStack);
        }

        if(profile.getKillstreak() == 15 || profile.getKillstreak() == 30 || profile.getKillstreak() == 45) {
            Bukkit.broadcastMessage(CC.translate(profile.getProfile().getDisplayName() + " &ehas gotten a killstreak of &d" + profile.getKillstreak() + " &eand received &6Golden Apples"));
            profile.getPlayer().getInventory().setItem(8, new ItemStack(Material.GOLDEN_APPLE, 15));
        }

        if(profile.getKillstreak() == 50) {
            Bukkit.broadcastMessage(CC.translate(profile.getProfile().getDisplayName() + " &ehas gotten a killstreak of &d" + profile.getKillstreak() + " &eand received &7Invisibility"));
            profile.getPlayer().getInventory().setItem(8, PotionUtil.getPotionItemStack(PotionType.INVISIBILITY, 1, true, false,"Potion of Invisibility"));
        }

    }


    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                SoupProfile profile = SoupProfile.getByPlayer((Player) event.getEntity());

                if(profile.getKit() != null && profile.getKit().equals(KitManager.getByName("Avatar"))) {
                    event.setCancelled(true);
                    return;
                }

                if(profile.isFallDamage()) {
                    event.setCancelled(true);
                    profile.setFallDamage(false);
                }
            }
        }
    }

    @EventHandler
    public void onSoup(PlayerInteractEvent event) {
        if(event.getItem() == null)
            return;

        Player player = event.getPlayer();

        if (player.getItemInHand().getType() == Material.MUSHROOM_SOUP && (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR))) {
            double health = player.getHealth();
            double foodLevel = player.getFoodLevel();
            if (health == player.getMaxHealth())
                return;
            else if (health + 8 >= 20)
                player.setHealth(20);
            else
                player.setHealth(health + 8);

            if (foodLevel + 8 >= 20)
                player.setFoodLevel(20);
            else
                player.setFoodLevel((int) (foodLevel + 8));

            player.getItemInHand().setType(Material.BOWL);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(PlayerUtil.inArea(event.getPlayer().getLocation(), Locale.SPAWN_LOCATION_1, Locale.SPAWN_LOCATION_2)) {
            event.setCancelled(true);
            return;
        }

        runCheck();
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent event) {
        IntStream.range(0, 3).forEach(i -> event.setLine(i, CC.translate(event.getLine(i))));
    }

    @EventHandler
    public void onSign(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getClickedBlock() != null) {
            if(event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if(sign.getLine(0).contains("Soups")) {
                    Inventory inv = Bukkit.createInventory(null, 54, "Free Soups");
                    for(int i = 0; i < 54; i++)
                        inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));

                    player.openInventory(inv);
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(!(event.getPlayer().isOp()) || !event.getPlayer().getGameMode().equals(GameMode.CREATIVE) || SoupProfile.getByPlayer(event.getPlayer()).isSpectating())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(!(event.getPlayer().isOp()) || !event.getPlayer().getGameMode().equals(GameMode.CREATIVE) || SoupProfile.getByPlayer(event.getPlayer()).isSpectating())
            event.setCancelled(true);
    }

    private void runCheck() {
        Bukkit.getWorlds().forEach(world -> world.getEntities().stream().filter(entity -> entity instanceof Item).forEach(entity -> {
            new Thread(() -> {
                long sleep = this.applicableForDrop.contains(((Item) entity).getItemStack().getType()) ? 3000L : 500L;

                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                entity.remove();
            });
        }));
    }

    // ugly, but idc.
    private List<Material> applicableForDrop = Arrays.asList(
            Material.MUSHROOM_SOUP,
            Material.DIAMOND_SWORD,
            Material.IRON_SWORD,
            Material.GOLD_SWORD,
            Material.WOOD_SWORD,
            Material.STONE_SWORD,
            Material.DIAMOND_CHESTPLATE,
            Material.IRON_CHESTPLATE,
            Material.GOLD_CHESTPLATE,
            Material.LEATHER_CHESTPLATE,
            Material.CHAINMAIL_CHESTPLATE,
            Material.DIAMOND_HELMET,
            Material.GOLD_HELMET,
            Material.CHAINMAIL_HELMET,
            Material.LEATHER_HELMET,
            Material.DIAMOND_LEGGINGS,
            Material.IRON_LEGGINGS,
            Material.GOLD_LEGGINGS,
            Material.CHAINMAIL_LEGGINGS,
            Material.LEATHER_LEGGINGS,
            Material.DIAMOND_BOOTS,
            Material.GOLD_BOOTS,
            Material.IRON_BOOTS,
            Material.CHAINMAIL_BOOTS,
            Material.LEATHER_BOOTS
    );

}
