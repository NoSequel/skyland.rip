package rip.skyland.phase.thread;

import rip.skyland.phase.Phase;
import rip.skyland.phase.util.PlayerUtil;

public class TablistThread extends Thread {

    private Phase phase;
    private long updateTime;

    public TablistThread(Phase phase, long updateTime) {
        this.phase = phase;
        this.updateTime = updateTime;
        this.start();
    }

    public void run() {
        while (true) {
            PlayerUtil.getOnlinePlayers().stream()
                    .filter(player -> phase.getTablistManager().getTablist(player) != null)
                    .forEach(player -> phase.getTablistManager().getTablist(player).hideRealPlayers().update());

            try {
                Thread.sleep(updateTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
