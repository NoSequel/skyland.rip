package rip.skyland.phase.adapter;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import rip.skyland.phase.util.CC;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ScoreboardAdapter {

    private boolean descending = false;
    private int startNumber = 1;

    private String title;
    private List<String> lines;

    public ScoreboardAdapter() {
        this.lines = new ArrayList<>();
        title = "Not Set";
    }

    public void add(int index, String string) {
        if(index > lines.size()) {
            Bukkit.getConsoleSender().sendMessage(index + " is smaller than the maximum index: " + lines.size());
            return;
        }

        lines.add(index, CC.translate(string));
    }

    public void add(String string) {
        add(lines.size(), string);
    }

}
