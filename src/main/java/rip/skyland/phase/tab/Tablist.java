package rip.skyland.phase.tab;

import com.google.common.collect.Table;
import lombok.Getter;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.skyland.phase.tab.adapter.TabEntry;
import rip.skyland.phase.tab.reflection.ReflectionConstants;
import rip.skyland.phase.util.PlayerUtil;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.IntStream;

public class Tablist {

	private static Object[] GAME_PROFILES;
	private static String[] TAB_NAMES;

	@Getter
	private final ClientVersion version;

	@Getter
	private final Player player;

	@Getter
	private boolean initiated;

	Tablist(Player player) {
		this.player = player;
		this.version = ClientVersion.getVersion(player);

		initializeGameProfiles();

		for(int i = 0; i < 80; i++) {
			Object packet = ReflectionConstants.SCOREBOARD_TEAM_CONSTRUCTOR.invoke();
			ReflectionConstants.SCOREBOARD_TEAM_NAME.set(packet, TAB_NAMES[i]);
			ReflectionConstants.SCOREBOARD_TEAM_PLAYERS.get(packet).add(TAB_NAMES[i]);
			sendPacket(player, packet);
		}

		addFakePlayers();
		update();
	}

	private void sendPacket(Player player, Object packet) {
		Object handle = ReflectionConstants.GET_HANDLE_METHOD.invoke(player);
		Object connection = ReflectionConstants.PLAYER_CONNECTION.get(handle);
		ReflectionConstants.SEND_PACKET.invoke(connection, packet);
	}

	public void update() {
		TablistManager manager = TablistManager.INSTANCE;
		if (!initiated || manager == null)
			return;


		Table<Integer, Integer, TabEntry> entries = manager.supplier.getTablist(player).getStrings();
		boolean useProfiles = version.ordinal() != 0;
		int magic = useProfiles ? 4 : 3;

		for(int i = 0; i < magic*20; i++) {
			int x = i % magic;
			int y = i / magic;

			TabEntry entry = entries.get(x, y);

			if (entry == null) {
				sendPacket(player, useProfiles, -1, i, "", "", "");
				continue;
			}
			int ping = entry.getName().isEmpty() ? -1 : entry.getPing();

			if(manager.supplier.getTablist(player).getStrings().get(x, y) == null)
				ping = -1;

			String text = entry.getName();

			if (text == null)
				text = "";


			String name = TAB_NAMES[i];
			String prefix, suffix = "";

			if (text.toCharArray().length < 17) {
				prefix = text;
			} else {
				String left = text.substring(0, 16), right = text.substring(16, text.toCharArray().length);
				if (left.endsWith("§")) {
					left = left.substring(0, left.toCharArray().length - 1);
					right = "§" + right;
				}
				String last = ChatColor.getLastColors(left);
				right = last + right;
				prefix = left;
				suffix = StringUtils.left(right, 16);
			}

			sendPacket(player, useProfiles, ping, i, prefix, suffix, name);
		}
	}

	private void sendPacket(Player player, boolean useProfiles, int ping, int index, String prefix, String suffix, String name) {
		Object packet = ReflectionConstants.SCOREBOARD_TEAM_CONSTRUCTOR.invoke();
		ReflectionConstants.SCOREBOARD_TEAM_NAME.set(packet, TAB_NAMES[index]);
		ReflectionConstants.SCOREBOARD_TEAM_ACTION.set(packet, 2);
		ReflectionConstants.SCOREBOARD_TEAM_PREFIX.set(packet, prefix);
		ReflectionConstants.SCOREBOARD_TEAM_SUFFIX.set(packet, suffix);
		sendPacket(player, packet);


		packet = ReflectionConstants.TAB_PACKET_CONSTRUCTOR.invoke();
		Object profile = GAME_PROFILES[index];

		ReflectionConstants.TAB_PACKET_ACTION.set(packet, 0);
		ReflectionConstants.TAB_PACKET_LATENCY.set(packet, ping);

		if (useProfiles)
			ReflectionConstants.TAB_PACKET_PROFILE.set(packet, profile);
		else
			ReflectionConstants.TAB_PACKET_NAME.set(packet, name);



		sendPacket(player, packet);
	}

	public Tablist hideRealPlayers() {
		if (!initiated)
			return this;
		boolean useProfiles = version.ordinal() != 0;

		PlayerUtil.getOnlinePlayers().forEach(other -> {
			if (!player.canSee(other)) {
				return;
			}
			Object packet = ReflectionConstants.TAB_PACKET_CONSTRUCTOR.invoke();
			if (useProfiles) {
				Object profile = ReflectionConstants.GET_PROFILE_METHOD.invoke(other);
				ReflectionConstants.TAB_PACKET_PROFILE.set(packet, profile);
			} else {
				ReflectionConstants.TAB_PACKET_NAME.set(packet, other.getName());
			}
			ReflectionConstants.TAB_PACKET_ACTION.set(packet, 4);
			sendPacket(player, packet);
		});

		return this;
	}

	Tablist hideFakePlayers() {
		if (!initiated)
			return this;
		boolean useProfiles = version.ordinal() != 0;
		Arrays.stream(GAME_PROFILES).forEach(other -> {
			Object packet = ReflectionConstants.TAB_PACKET_CONSTRUCTOR.invoke();
			if (useProfiles) {
				ReflectionConstants.TAB_PACKET_PROFILE.set(packet, other);
			} else {
				String name = ReflectionConstants.GAME_PROFILE_NAME.get(other);
				ReflectionConstants.TAB_PACKET_NAME.set(packet, name);
			}
			ReflectionConstants.TAB_PACKET_ACTION.set(packet, 4);
			sendPacket(player, packet);
		});
		return this;
	}

    private void addFakePlayers() {
		if (initiated)
			return;
		boolean useProfiles = version.ordinal() != 0;
		int magic = useProfiles ? 4 : 3;
		for (int i = 0; i < (magic * 20); i++) {
			Object packet = ReflectionConstants.TAB_PACKET_CONSTRUCTOR.invoke();
			Object profile = GAME_PROFILES[i];
			ReflectionConstants.TAB_PACKET_ACTION.set(packet, 0);
			ReflectionConstants.TAB_PACKET_LATENCY.set(packet, -1);

			if (useProfiles) {
				ReflectionConstants.TAB_PACKET_PROFILE.set(packet, profile);
			} else {
				String name = ReflectionConstants.GAME_PROFILE_NAME.get(profile);
				ReflectionConstants.TAB_PACKET_NAME.set(packet, name);
			}


			sendPacket(player, packet);
		}
		initiated = true;
	}

	void clear() {
		for (int i = 0; i < 80; i++) {
			Object packet = ReflectionConstants.SCOREBOARD_TEAM_CONSTRUCTOR.invoke();
			ReflectionConstants.SCOREBOARD_TEAM_NAME.set(packet, TAB_NAMES[i]);
			ReflectionConstants.SCOREBOARD_TEAM_ACTION.set(packet, 4);
			sendPacket(player, packet);
		}
	}

	static void initializeGameProfiles() {
		GAME_PROFILES = new Object[80];
		TAB_NAMES = new String[80];
		for (int i = 0; i < 80; i++) {
			int x = i % 4;
			int y = i / 4;
			String name = "§0§" + x + (y > 9 ? "§" + String.valueOf(y).toCharArray()[0] + "§" + String.valueOf(y).toCharArray()[1]
					: "§0§" + String.valueOf(y).toCharArray()[0]) + ChatColor.DARK_AQUA;
			UUID id = UUID.randomUUID();
			GameProfile profile = new GameProfile(id, name);
			profile.getProperties().put("textures", new Property("textures",
					"eyJ0aW1lc3RhbXAiOjE0MTEyNjg3OTI3NjUsInByb2ZpbGVJZCI6IjNmYmVjN2RkMGE1ZjQwYmY5ZDExODg1YTU0NTA3MTEyIiwicHJvZmlsZU5hbWUiOiJsYXN0X3VzZXJuYW1lIiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg0N2I1Mjc5OTg0NjUxNTRhZDZjMjM4YTFlM2MyZGQzZTMyOTY1MzUyZTNhNjRmMzZlMTZhOTQwNWFiOCJ9fX0=",
					"u8sG8tlbmiekrfAdQjy4nXIcCfNdnUZzXSx9BE1X5K27NiUvE1dDNIeBBSPdZzQG1kHGijuokuHPdNi/KXHZkQM7OJ4aCu5JiUoOY28uz3wZhW4D+KG3dH4ei5ww2KwvjcqVL7LFKfr/ONU5Hvi7MIIty1eKpoGDYpWj3WjnbN4ye5Zo88I2ZEkP1wBw2eDDN4P3YEDYTumQndcbXFPuRRTntoGdZq3N5EBKfDZxlw4L3pgkcSLU5rWkd5UH4ZUOHAP/VaJ04mpFLsFXzzdU4xNZ5fthCwxwVBNLtHRWO26k/qcVBzvEXtKGFJmxfLGCzXScET/OjUBak/JEkkRG2m+kpmBMgFRNtjyZgQ1w08U6HHnLTiAiio3JswPlW5v56pGWRHQT5XWSkfnrXDalxtSmPnB5LmacpIImKgL8V9wLnWvBzI7SHjlyQbbgd+kUOkLlu7+717ySDEJwsFJekfuR6N/rpcYgNZYrxDwe4w57uDPlwNL6cJPfNUHV7WEbIU1pMgxsxaXe8WSvV87qLsR7H06xocl2C0JFfe2jZR4Zh3k9xzEnfCeFKBgGb4lrOWBu1eDWYgtKV67M2Y+B3W5pjuAjwAxn0waODtEn/3jKPbc/sxbPvljUCw65X+ok0UUN1eOwXV5l2EGzn05t3Yhwq19/GxARg63ISGE8CKw="));
			TAB_NAMES[i] = name;
			GAME_PROFILES[i] = profile;
		}
	}

}
