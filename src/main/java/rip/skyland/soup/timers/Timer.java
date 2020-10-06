package rip.skyland.soup.timers;

public interface Timer {

    boolean isOnScoreboard();
    String getScoreboardPrefix();
    long getDefaultCooldown();

}
