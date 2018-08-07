package io.samdev.chatgames.util;

import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.api.event.ChatGameEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class UtilServer
{
    private UtilServer() {}

    private static final ChatGames PLUGIN = JavaPlugin.getPlugin(ChatGames.class);

    static ChatGames getPlugin()
    {
        return PLUGIN;
    }

    public static Logger getLogger()
    {
        return PLUGIN.getLogger();
    }

    public static void registerListener(Listener listener)
    {
        Bukkit.getPluginManager().registerEvents(listener, PLUGIN);
    }

    public static <T extends ChatGameEvent> T callEvent(T event)
    {
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static void runAsync(Runnable runnable)
    {
        Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, runnable);
    }

    public static void runSync(Runnable runnable)
    {
        Bukkit.getScheduler().runTask(PLUGIN, runnable);
    }
}
