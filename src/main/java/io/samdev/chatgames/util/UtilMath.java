package io.samdev.chatgames.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class UtilMath
{
    private UtilMath() {}

    public static double round(double number, int precision)
    {
        return new BigDecimal(number).setScale(precision, RoundingMode.HALF_UP).doubleValue();
    }
}
