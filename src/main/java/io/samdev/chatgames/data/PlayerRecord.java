package io.samdev.chatgames.data;

import io.samdev.chatgames.ChatGames;
import org.bukkit.Bukkit;

import java.util.UUID;

public class PlayerRecord
{
    private final ChatGames plugin;

    private final UUID uuid;

    PlayerRecord(ChatGames plugin, UUID uuid, int wins, boolean toggled)
    {
        this.plugin = plugin;

        this.uuid = uuid;
        this.wins = wins;
        this.toggled = toggled;
    }

    public String getPlayerName()
    {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    public UUID getPlayerUuid()
    {
        return uuid;
    }

    private boolean toggled;

    public boolean toggle()
    {
        toggled = !toggled;
        plugin.getDataManager().updateToggle(this);

        return toggled;
    }

    public boolean isToggled()
    {
        return toggled;
    }

    private int wins;

    public void addWin()
    {
        wins++;

        plugin.getDataManager().updateWins(this);
    }

    public int getWins()
    {
        return wins;
    }
}
