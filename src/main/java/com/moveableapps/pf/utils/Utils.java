package com.moveableapps.pf.utils;

import java.nio.file.Path;

public class Utils {

    public static String getDatabasePath() {
        String homeDirectory = System.getenv("HOME");
        if (homeDirectory == null) {
            throw new RuntimeException("Unable to get environment variable HOME");
        }

        String xdgDataHome = System.getenv("XDG_DATA_HOME");
        if (xdgDataHome == null || xdgDataHome.isBlank()) {
            xdgDataHome = Path.of(homeDirectory, ".local", "share").toString();
        }
        return Path.of(xdgDataHome, "pf").toString();
    }
}
