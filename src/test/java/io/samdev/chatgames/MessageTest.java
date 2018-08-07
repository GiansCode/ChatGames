package io.samdev.chatgames;

import io.samdev.chatgames.util.UtilString;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MessageTest
{
    @Test
    public void messageTest()
    {
        String formatted = UtilString.formatMessage("Hello {player}! You have {wins} wins",
            "player", "Mas281", "wins", 200
        );

        assertTrue(formatted.equals("Hello Mas281! You have 200 wins"));
    }
}
