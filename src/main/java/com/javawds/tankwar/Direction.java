package com.javawds.tankwar;

import java.awt.*;

public enum Direction {
    UP("U"),
    DOWN("D"),
    LEFT("L"),
    RIGHT("R"),

    LEFT_UP("LU"),
    RIGHT_UP("RU"),
    LEFT_DOWN("LD"),
    RIGHT_DOWN("RD");

    Direction(String abbrev) {
        this.abbrev = abbrev;
    }

    private final String abbrev;
    Image getImage(String prefix) {
        return Tools.getImage(prefix + abbrev + ".gif");
    }
}
