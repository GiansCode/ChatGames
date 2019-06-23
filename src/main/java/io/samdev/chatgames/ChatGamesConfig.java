package io.samdev.chatgames;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ChatGamesConfig
{
    ChatGamesConfig(FileConfiguration config)
    {
        ConfigurationSection settings = config.getConfigurationSection("settings");

        roundInterval = settings.getInt("round_interval") * 20L;
        roundLength = settings.getInt("round_length") * 20L;

        playersNeeded = settings.getInt("players_needed");

        disallowedWorlds = settings.getStringList("disallowed_worlds").stream()
            .map(Bukkit::getWorld)
            .filter(Objects::nonNull)
            .collect(toSet());
    }

    private final long roundInterval;

    public long getRoundInterval()
    {
        return roundInterval;
    }

    private final long roundLength;

    public long getRoundLength()
    {
        return roundLength;
    }

    private final int playersNeeded;

    public boolean hasEnoughPlayers()
    {
        return Bukkit.getOnlinePlayers().size() >= playersNeeded;
    }

    private final Set<World> disallowedWorlds;

    boolean isAllowedWorld(World world)
    {
        return !disallowedWorlds.contains(world);
    }
}
