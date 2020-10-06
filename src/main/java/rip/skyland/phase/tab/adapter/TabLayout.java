package rip.skyland.phase.tab.adapter;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TabLayout {

    @Setter
    private TabLayout layout;

    private static List<TabLayout> layouts = new ArrayList<>();

    private Table<Integer, Integer, TabEntry> strings;

    public TabLayout() {
        strings = HashBasedTable.create();

        layouts.add(this);
    }

    public void add(int x, int y, String string) {
        this.add(x, y, string, -1);
    }

    public void add(int x, int y, String string, int ping) {
        strings.put(x, y, new TabEntry(string, ping));
    }
}
