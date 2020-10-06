package rip.skyland.phase.thread;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import rip.skyland.phase.Phase;
import rip.skyland.phase.adapter.ScoreboardAdapter;
import rip.skyland.phase.entry.PhaseBoard;
import rip.skyland.phase.entry.PhaseEntry;
import rip.skyland.phase.util.CC;
import rip.skyland.phase.util.PlayerUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ScoreboardThread extends Thread {

    private Phase phase;

    public ScoreboardThread(Phase phase) {
        this.phase = phase;
        this.start();
    }

    public void run() {
        while (true) {
            PlayerUtil.getOnlinePlayers().forEach(player -> {
                ScoreboardAdapter layout = phase.getAdapter().getScoreboard(player);

                if(layout == null) return;

                List<String> strings = layout.getLines();
                List<String> translatedStrings = new ArrayList<>();
                if(strings.isEmpty()) return;

                PhaseBoard board = PhaseBoard.getByPlayer(player);
                if(board == null) return;

                strings.forEach(string -> translatedStrings.add(CC.translate(string)));
                Collections.reverse(strings);

                Scoreboard scoreboard = board.getScoreboard();
                Objective objective = board.getObjective();
                if (!objective.getDisplayName().equalsIgnoreCase(layout.getTitle())) objective.setDisplayName(CC.translate(layout.getTitle()));

                for(int i = 0; i < strings.size(); i++) {
                    String text = strings.get(i);
                    int pos;

                    pos = i + 1;

                    Iterator<PhaseEntry> iterator = board.getEntries().iterator();
                    while (iterator.hasNext()) {
                        PhaseEntry entry = iterator.next();
                        int entryPosition = objective.getScore(entry.getKey()).getScore();
                        if (entryPosition > strings.size()) {
                            iterator.remove();
                            entry.remove();
                        }
                    }

                    int positionToSearch;
                    positionToSearch = pos - 1;

                    PhaseEntry entry = board.getByPosition(positionToSearch);

                    if (entry == null)
                        new PhaseEntry(board, text).send(pos);

                    else {
                        entry.setText(text);
                        entry.setup();
                        entry.send(pos);
                    }


                    if (board.getEntries().size() > strings.size()) {
                        iterator = board.getEntries().iterator();
                        while (iterator.hasNext()) {
                            entry = iterator.next();
                            if (!translatedStrings.contains(entry.getText()) || Collections.frequency(board.getBoardEntriesFormatted(), entry.getText()) > 1) {
                                iterator.remove();
                                entry.remove();
                            }
                        }
                    }
                }

                player.setScoreboard(scoreboard);
            });

            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
