package io.samdev.chatgames.util;

public final class UtilTime
{
    private UtilTime() {}

    public static String formatSeconds(long millis)
    {
        double seconds = millis / 1000.0D;

        return String.valueOf(UtilMath.round(seconds, 1));
    }
}
