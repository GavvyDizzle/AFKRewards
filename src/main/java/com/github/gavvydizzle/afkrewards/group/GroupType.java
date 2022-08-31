package com.github.gavvydizzle.afkrewards.group;

import java.util.ArrayList;

public enum GroupType {
    DOLPHIN,
    PIG;

    public final static ArrayList<String> values;

    static {
        values = new ArrayList<>();
        for (GroupType groupType : GroupType.values()) {
            values.add(groupType.name());
        }
    }
}
