package io.samdev.chatgames.util;

import java.util.ArrayList;
import java.util.Arrays;

public final class UtilString
{
    private UtilString() {}

    public static String formatMessage(String value, Object... params)
    {
        if (params.length == 0)
        {
            return value;
        }

        assert params.length % 2 == 0 : "Message params unbalanced";

        String message = value;

        for (int i = 0; i < params.length - 1; i += 2)
        {
            message = message.replace("{" + params[i] + "}", String.valueOf(params[i + 1]));
        }

        return message;
    }

    public static String truncateString(String input, int length)
    {
        return input.substring(0, Math.min(input.length(), length));
    }

    public static String join(String[] array, String delimiter, int startIndex)
    {
        StringBuilder builder = new StringBuilder();

        for (int i = startIndex; i < array.length; i++)
        {
            builder.append(array[i]).append(delimiter);
        }

        return builder.toString();
    }

    public static String[] removeFirst(String[] array)
    {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(array));
        list.remove(0);

        return list.toArray(new String[0]);
    }
}