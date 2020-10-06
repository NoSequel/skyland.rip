package rip.skyland.soup.util;

import rip.skyland.soup.profiles.SoupProfile;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class FormatUtil {

    public static String getKdr(Player player) {
        SoupProfile profile = SoupProfile.getByPlayer(player);
        double kills = profile.getKills();
        double deaths = profile.getDeaths();

        if(deaths <= 0)
            return kills + "";

        DecimalFormat format = new DecimalFormat("#.##");

        return format.format(((kills > 0) || (deaths > 0)) ? (kills / deaths) : 0);

    }

}
