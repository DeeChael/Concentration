package net.deechael.concentration;

import com.mojang.blaze3d.platform.ScreenManager;

public class ConcentrationCaching {

    public static long concentration$lastMonitor = -1;

    public static boolean concentration$cachedSize = false;
    public static boolean concentration$cachedPos = false;
    public static boolean concentration$cacheSizeLock = false;

    public static int concentration$cachedX = 0;
    public static int concentration$cachedY = 0;
    public static int concentration$cachedWidth = 0;
    public static int concentration$cachedHeight = 0;
}
