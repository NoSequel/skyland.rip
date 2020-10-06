package rip.skyland.soup.scoreboard.style;

import rip.skyland.core.util.CC;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Style {

    // 1v1 and spawn pvp
    private boolean kills, deaths, killstreak, killstreak2, highestKillstreak, credits, kdr, barsWithStars;

    @Setter
    private CC primaryColor, secondaryColor;

    // events
    private boolean modern;

    @Setter
    private CC primaryColorEvents, secondaryColorEvents;


    private String name, scoreboard;

    @Getter
    private static List<Style> styles = new ArrayList<>();

    private String author;

    public Style(String name, CC primaryColor, CC secondaryColor, CC primaryColorEvents, CC secondaryColorEvents, boolean addToArray) {
        this.name = name;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.primaryColorEvents = primaryColorEvents;
        this.secondaryColorEvents = secondaryColorEvents;

        this.kills = true;
        this.deaths = true;
        this.killstreak = true;
        this.killstreak2 = false;
        this.highestKillstreak = true;
        this.credits = true;
        this.kdr = true;

        if(addToArray)
         styles.add(this);
    }

    public Style setKills(boolean value) {
        this.kills = value;
        return this;
    }


    public Style setDeaths(boolean value) {
        this.deaths = value;
        return this;
    }


    public Style setKillstreak(boolean value) {
        this.killstreak = value;
        return this;
    }

    public Style setKillstreak2(boolean value) {
        this.killstreak2 = value;
        return this;
    }

    public Style setHighestKillstreak(boolean value) {
        this.highestKillstreak = value;
        return this;
    }


    public Style setCredits(boolean value) {
        this.credits = value;
        return this;
    }

    public Style setKdr(boolean value) {
        this.kdr = value;
        return this;
    }

    public Style setScoreboard(String value) {
        this.scoreboard = value;
        return this;
    }

    public Style setModern(boolean value) {
        this.modern = value;
        return this;
    }

    public Style setBarWithStars(boolean value) {
        this.barsWithStars = value;
        return this;
    }

    public Style setAuthor(String value) {
        this.author = value;
        return this;
    }

    public static Style getByName(String name) { return styles.stream().filter(style -> style.getName().equals(name)).findFirst().orElse(null); }


}
