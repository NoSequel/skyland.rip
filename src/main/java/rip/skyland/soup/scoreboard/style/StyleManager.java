package rip.skyland.soup.scoreboard.style;

import rip.skyland.core.util.CC;

public class StyleManager {

    public StyleManager() {
        new Style("SkylandKits", CC.DARK_RED, CC.YELLOW, CC.ORANGE, CC.YELLOW, true)
                .setModern(false)
                .setScoreboard("&6&lSkylandRIP &a[Alpha]");
        new Style("Lemon", CC.GREEN, CC.AQUA, CC.GREEN, CC.AQUA, true)
                .setAuthor("Staud")
                .setModern(true)
                .setScoreboard("&6&lSkylandRIP &a[Alpha]");

        new Style("Peach", CC.PINK, CC.YELLOW, CC.PINK, CC.YELLOW, true)
                .setAuthor("skrf")
                .setModern(false)
                .setScoreboard("&d&lSkylandRIP &e[Alpha]");

        new Style("Snow", CC.WHITE, CC.AQUA, CC.WHITE, CC.AQUA, true)
                .setAuthor("skrf")
                .setModern(false)
                .setScoreboard("&b&lSkylandRIP &f[Alpha]");

        new Style("Modern", CC.DARK_AQUA, CC.AQUA, CC.AQUA, CC.WHITE, true)
                .setModern(true)
                .setKillstreak(false)
                .setHighestKillstreak(false)
                .setKdr(false)
                .setScoreboard("&b&lSkyland &7&l｜ &a&lALPHA");

        new Style("Redisson", CC.RED, CC.WHITE, CC.RED, CC.WHITE, true)
                .setAuthor("Axris")
                .setModern(true)
                .setHighestKillstreak(false)
                .setKdr(false)
                .setScoreboard("&c&lSkyland &7&l｜ &fAlpha");

        new Style("Exylands", CC.DARK_RED, CC.RED, CC.DARK_RED, CC.RED, true)
                .setAuthor("pcrunn/uwuhelen")
                .setModern(true)
                .setKdr(false)
                .setDeaths(false)
                .setBarWithStars(true)
                .setScoreboard("&4&lExylands &d[Beta]");
    }

}
