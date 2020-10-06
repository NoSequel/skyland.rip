package rip.skyland.phase.entry;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import rip.skyland.phase.Phase;
import rip.skyland.phase.util.CC;

import java.util.*;

@Getter
public class PhaseBoard {

    private Player player;
    private Scoreboard scoreboard;
    private Phase phase;
    private Objective objective;

    private List<PhaseEntry> entries;
    private Set<String> keys;
    private static Set<PhaseBoard> boards = new HashSet<>();

    public PhaseBoard(Player player, Phase phase) {
        this.player = player;
        this.phase = phase;
        this.keys = new HashSet<>();
        this.entries  = new ArrayList<>();

        this.setup();
    }

    public void setup() {
        this.scoreboard = player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())
                ? Bukkit.getScoreboardManager().getNewScoreboard()
                : player.getScoreboard();

        this.objective = this.scoreboard.getObjective("phase") == null ? this.scoreboard.registerNewObjective("phase", "dummy") : this.scoreboard.getObjective("phase");
        this.objective.setDisplaySlot(DisplaySlot.valueOf("SIDEBAR"));

        objective.setDisplayName(CC.translate(phase.getAdapter().getScoreboard(player).getTitle()));

        boards.add(this);
    }

    String getNewKey() {
        String text = CC.values()[new Random().nextInt(CC.values().length)].toString() + CC.WHITE;
        while(keys.contains(text)) {
            text+= CC.values()[new Random().nextInt(CC.values().length)].toString() + CC.WHITE;
        }

        this.keys.add(text);

        return text;
    }

    public List<String> getBoardEntriesFormatted() {
        List<String> toReturn = new ArrayList<>();
        new ArrayList<>(this.entries).forEach(entry -> toReturn.add(entry.getText()));

        return toReturn;
    }

    public PhaseEntry getByPosition(int position) {
        int i = 0;
        for (PhaseEntry board : this.entries) {
            if (i == position) {
                return board;
            }
            ++i;
        }
        return null;
    }

    public void delete() {
        boards.remove(this);
    }

    public static PhaseBoard getByPlayer(Player player) { return boards.stream().filter(board -> board.getPlayer().equals(player)).findFirst().orElse(null); }

}
