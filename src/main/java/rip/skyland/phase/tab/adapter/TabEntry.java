package rip.skyland.phase.tab.adapter;

import lombok.Getter;
import rip.skyland.phase.util.CC;

@Getter
public class TabEntry {
    private String name;
    private int ping;

    public TabEntry(String name, int ping) {
        this.name = CC.translate(name);
        this.ping = ping;
    }

}
