package rip.skyland.practice.queue;

import org.bukkit.potion.PotionType;
import rip.skyland.core.CorePlugin;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.ItemBuilder;
import rip.skyland.practice.match.impl.SumoMatch;
import rip.skyland.soup.kit.Kit;
import rip.skyland.soup.kit.KitManager;
import rip.skyland.practice.match.impl.ArcadeMatch;
import rip.skyland.practice.match.impl.DefaultMatch;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.util.EloCalculator;
import rip.skyland.soup.util.Items;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import rip.skyland.soup.util.Locale;
import rip.skyland.soup.util.PotionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QueueManager {

    @Getter
    private static List<Queue> queues = new ArrayList<>();

    public QueueManager() {
        queues.add(new Queue("Standard", "&aStandard",false, new ItemStack(Material.DIAMOND_SWORD)).setKit(KitManager.getByName("Standard")).setDuelQueue(true));
        queues.add(new Queue("Elite",  "&2Elite", false, new ItemStack(Material.IRON_SWORD)).setKit(KitManager.getByName("PvP")).setDuelQueue(true));
        queues.add(new Queue("Buffed", "&5Buffed", false, new ItemBuilder(Material.DIAMOND_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).create()).setKit(KitManager.getByName("Buffed")).setDuelQueue(true));
        queues.add(new Queue("Speed", "&bSpeed", false, new ItemBuilder(Material.SUGAR).addEnchantment(Enchantment.DURABILITY, 1).create()).setKit(KitManager.getByName("Speed")).setDuelQueue(true));
        queues.add(new Queue("Refill", "&6Refill", false, new ItemStack(Material.MUSHROOM_SOUP)).setKit(KitManager.getByName("PvP")).setDuelQueue(true));

        queues.add(new Queue("Buffed", "&5Buffed", true, new ItemBuilder(Material.DIAMOND_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).create()).setKit(KitManager.getByName("Buffed")).setDuelQueue(false));
        queues.add(new Queue("Arcade", "&3Arcade", true, new ItemStack(Material.BLAZE_POWDER)).setDuelQueue(true));
        queues.add(new Queue("Sumo", "&6Sumo", true, new ItemBuilder(Material.STICK).addEnchantment(Enchantment.KNOCKBACK, 4).create()).setKit(KitManager.getByName("Sumo")).setDuelQueue(true));
        queues.add(new Queue("NoDebuff", "&dNo Debuff", true, PotionUtil.getPotionItemStack(PotionType.INSTANT_HEAL, 1, false, true, "&dNo Debuff")).setKit(KitManager.getByName("NoDebuff")).setDuelQueue(true));
    }

    public void leaveQueue(Queue queue, Player player) {
        queue.getQueueing().remove(player.getUniqueId());
        SoupProfile.getByPlayer(player).setCurrentQueue(null);
        Items.join1v1(player);
    }

    public void joinQueue(Queue queue, Player player) {
        SoupProfile profile = SoupProfile.getByPlayer(player);
        player.sendMessage(CC.translate("&aYou are now queued for " + queue.getDisplayName()));

        queue.getQueueing().add(player.getUniqueId());
        profile.setCurrentQueue(queue);
        queue.getJoinTime().put(player, 0);
        queue.getEloRange().put(player, new int[] { profile.getElo(queue.getKit()), profile.getElo(queue.getKit())});
        new BukkitRunnable() {

            public void run() {
                if (profile.getCurrentQueue() == null)
                    this.cancel();

                else {
                    int prev = queue.getJoinTime().get(player);
                    queue.getJoinTime().remove(player);
                    queue.getJoinTime().put(player, prev + 1);
                    queue.getEloRange().put(player, new int[]{queue.getEloRange().get(player)[0] <= 0 ? 0 : queue.getEloRange().get(player)[0] -5, queue.getEloRange().get(player)[1] + 5});
                }
            }
        }.runTaskTimerAsynchronously(CorePlugin.getInstance(), 20L, 20L);

        player.getInventory().setContents(Items.lobbyMatchLeaveItems());

        if(queue.isRanked() && !Locale.disableElo) {
            List<Player> players = new ArrayList<>();
            queue.getQueueing().stream().filter(target -> EloCalculator.inbetween(
                    profile.getElo(queue.getKit()),
                    queue.getEloRange().get(Bukkit.getPlayer(target))[0],
                    queue.getEloRange().get(Bukkit.getPlayer(target))[1],
                    SoupProfile.getByPlayer(Bukkit.getPlayer(target)).getElo(queue.getKit()),
                    queue.getEloRange().get(player)[0],
                    queue.getEloRange().get(player)[1])).forEach(std -> players.add(Bukkit.getPlayer(std)));
            if(players.size() >= 2) {
                Player player1 = players.get(0);
                Player player2 = players.get(1);

                Arrays.asList(player1, player2).forEach(player3 -> queue.getQueueing().remove(player3.getUniqueId()));
                doMatch(queue, player1, player2, false);
            }
        } else {
            if(queue.getQueueing().size() >= 2) {
                Player player1 = Bukkit.getPlayer(queue.getQueueing().get(0));
                Player player2 = Bukkit.getPlayer(queue.getQueueing().get(1));

                Arrays.asList(player1, player2).forEach(player3 -> queue.getQueueing().remove(player3.getUniqueId()));
                doMatch(queue, player1, player2, false);
            }
        }
    }

    public void doMatch(Queue queue, Player player1, Player player2, boolean forceUnranked) {
        if(queue.getName().equals("Arcade")) {
            List<Kit> kits = new ArrayList<>();
            KitManager.getKits().stream().filter(Kit::isAbilityKit).forEach(kits::add);

            new ArcadeMatch(kits.get(new Random().nextInt(kits.size())), player1.getUniqueId(), player2.getUniqueId(), !forceUnranked && queue.isRanked()).setQueue(queue);

        } else if(queue.getName().equals("Sumo"))
            new SumoMatch(queue.getKit(), player1.getUniqueId(), player2.getUniqueId(),  !forceUnranked && queue.isRanked()).setQueue(queue);
        else
            new DefaultMatch(queue.getKit() == null ? KitManager.getByName("Buffed") : queue.getKit(), player1.getUniqueId(), player2.getUniqueId(), !forceUnranked && queue.isRanked()).setQueue(queue);

        queue.getPlaying().add(player1.getUniqueId());
        queue.getPlaying().add(player2.getUniqueId());
    }

    public static Queue getByName(String name, boolean ranked) { return queues.stream().filter(queue -> queue.getName().equals(name) && (ranked == queue.isRanked())).findFirst().orElse(null); }
    public static Queue getByKit(Kit kit, boolean ranked) { return queues.stream().filter(queue -> {
        if(kit.isAbilityKit())
            return queue.getName().equals("Arcade");

        else
            return queue.getKit().equals(kit) && queue.isRanked()==ranked;
    }).findFirst().orElse(null); }

}
