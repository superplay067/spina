package com.spina.app;

import android.content.Context;
import android.content.SharedPreferences;

public final class SpinaPrefs {
    private static final String PREFS = "spina_prefs";
    private static final String ACTIVE = "active";
    private static final String MODE = "mode";
    private static final String MINUTES = "minutes";

    public static final int MODE_TIMER = 0;
    public static final int MODE_STANDARD = 1;
    public static final int MODE_HYBRID = 2;

    private SpinaPrefs() {}

    private static SharedPreferences prefs(Context c) {
        return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static boolean isActive(Context c) {
        return prefs(c).getBoolean(ACTIVE, false);
    }

    public static void setActive(Context c, boolean active) {
        prefs(c).edit().putBoolean(ACTIVE, active).apply();
    }

    public static int getMode(Context c) {
        return prefs(c).getInt(MODE, MODE_STANDARD);
    }

    public static void setMode(Context c, int mode) {
        prefs(c).edit().putInt(MODE, mode).apply();
    }

    public static int getMinutes(Context c) {
        return prefs(c).getInt(MINUTES, 15);
    }

    public static void setMinutes(Context c, int minutes) {
        prefs(c).edit().putInt(MINUTES, minutes).apply();
    }
}
