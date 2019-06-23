package io.samdev.chatgames.util;

import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class UtilPlayer
{
    private UtilPlayer() {}

    public static int getPing(Player player)
    {
        return ((CraftPlayer) player).getHandle().ping;
    }
}
