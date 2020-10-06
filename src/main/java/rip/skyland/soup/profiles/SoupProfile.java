package rip.skyland.soup.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.model.UpdateOptions;
import rip.skyland.core.profile.Profile;

import rip.skyland.core.util.CC;
import rip.skyland.practice.duel.DuelRequest;
import rip.skyland.practice.match.Match;
import rip.skyland.practice.queue.Queue;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.kit.Kit;
import rip.skyland.soup.kit.KitManager;
import rip.skyland.soup.scoreboard.style.Style;
import rip.skyland.soup.timers.PlayerTimer;
import rip.skyland.soup.util.MongoHandler;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

@Getter
@Setter
public class SoupProfile {

    @Getter
    private static List<SoupProfile> profiles = new ArrayList<>();

    private Profile profile;
    private Kit kit, lastKit;
    private Player player;
    private boolean fallDamage, avatarParticles, avatarWatergunInvincibility, spectating;
    private ProfileState state = ProfileState.PLAYING;

    private Style style, customStyle;

    private List<DuelRequest> outgoingRequests, incomingRequests;
    private List<Entity> tamed;

    private Match currentMatch, spectatingMatch;
    private Queue currentQueue;

    private Location lastLocation;

    private Map<String, Integer> elos;
    private int killstreak, highestKillstreak, credits, kills, deaths;

    private Player lastKill;
    private int timesKilled;

    public SoupProfile(Player player) {
        this.player = player;
        profiles.add(this);

        load();
    }

    private void load() {
        Document document = (Document) MongoHandler.getSoupProfiles().find(eq("uuid", player.getUniqueId().toString())).first();

        this.state = ProfileState.PLAYING;
        this.profile = Profile.getByUuid(player.getUniqueId());
        this.elos = new HashMap<>();
        this.tamed = new ArrayList<>();
        this.incomingRequests = new ArrayList<>();
        this.outgoingRequests = new ArrayList<>();

        if (document == null) {
            this.highestKillstreak = 0;
            this.credits = 0;
            this.kills = 0;
            this.deaths = 0;
            this.style = Style.getByName("SkylandKits");
            this.customStyle = new Style("Custom", CC.DARK_RED, CC.YELLOW, CC.ORANGE, CC.YELLOW, false).setScoreboard(Style.getByName("SkylandKits").getScoreboard());
            return;
        }

        if (document.getString("style") == null)
            document.put("style", "SkylandKits");

        this.highestKillstreak = document.getInteger("highest_killstreak", 0);
        this.credits = document.getInteger("credits", 0);
        this.kills = document.getInteger("kills", 0);
        this.deaths = document.getInteger("deaths", 0);
        this.style = Style.getByName(document.getString("style"));

        if(document.containsKey("lastKit") && KitManager.getByName(document.getString("lastKit")) != null) {
            this.lastKit = KitManager.getByName(document.getString("lastKit"));
        }

        if(style == null)
            this.style = Style.getByName("SkylandKits");
        
        customStyle = new Style("Custom", CC.valueOf(document.getString("cprimaryColor")), CC.valueOf(document.getString("csecondaryColor")),
                CC.valueOf(document.getString("ceventPrimaryColor")), CC.valueOf(document.getString("ceventSecondaryColor")), false)
                .setKills(document.getBoolean("ckills"))
                .setDeaths(document.getBoolean("cdeaths"))
                .setKillstreak(document.getBoolean("ckillstreaks"))
                .setHighestKillstreak(document.getBoolean("chighestKillstreaks"))
                .setCredits(document.getBoolean("ccredits"))
                .setKdr(document.getBoolean("ckdr"))
                .setModern(document.getBoolean("cmodern")).setScoreboard(Style.getByName("SkylandKits").getScoreboard());

        new JsonParser().parse(document.getString("elo")).getAsJsonArray().forEach(element -> {
            JsonObject obj = element.getAsJsonObject();
            elos.put(obj.get("kit").getAsString(), obj.get("elo").getAsInt());
        });

        KitManager.getKits().forEach(queue -> {
            if(!elos.containsKey(queue.getName()))
                elos.put(queue.getName(), 1000);
        });
    }

    public void save() {
        Document document = new Document();

        document.put("highest_killstreak", highestKillstreak);
        document.put("credits", credits);
        document.put("uuid", player.getUniqueId().toString());
        document.put("kills", kills);
        document.put("deaths", deaths);
        document.put("style", style.getName());

        if(lastKit != null)
        document.put("lastKit", lastKit.getName());

        document.put("ckills", customStyle.isKills());
        document.put("cdeaths", customStyle.isDeaths());
        document.put("ckillstreaks", customStyle.isKillstreak());
        document.put("chighestKillstreaks", customStyle.isHighestKillstreak());
        document.put("ccredits", customStyle.isCredits());
        document.put("ckdr", customStyle.isKdr());
        document.put("cmodern", customStyle.isModern());
        document.put("cprimaryColor", customStyle.getPrimaryColor().name());
        document.put("csecondaryColor", customStyle.getSecondaryColor().name());
        document.put("ceventPrimaryColor", customStyle.getPrimaryColorEvents().name());
        document.put("ceventSecondaryColor", customStyle.getSecondaryColorEvents().name());
        
        JsonArray elo = new JsonArray();
        elos.keySet().forEach(e -> {
            JsonObject eloDoc = new JsonObject();
            eloDoc.addProperty("kit", e);
            eloDoc.addProperty("elo", elos.get(e));
        });

        document.put("elo", elo.toString());


        MongoHandler.getSoupProfiles().replaceOne(eq("uuid", player.getUniqueId().toString()), document, new UpdateOptions().upsert(true));
    }
    public void destroy() {
        this.save();
        profiles.remove(this);
    }

    public int getElo(Kit kit) {
        return elos == null ? 1000 : (kit == null ? 1000 : (elos.containsKey(kit.getName()) ? elos.get(kit.getName()) : elos.put(kit.getName(), 1000)));
    }

    public void clearCooldowns() {
        SoupPlugin.getInstance().getTimerManager().getTimers().stream().filter(timer -> timer.hasCooldown(player)).forEach(timer -> timer.onExpire(player));
    }

    public static SoupProfile getByPlayer(Player player) { return profiles.stream().filter(profile -> profile.getPlayer().equals(player)).findFirst().orElse(null); }

}
