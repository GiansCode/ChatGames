package io.samdev.chatgames.data;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface DataManager
{
    void closeConnection();

    PlayerRecord getRecord(Player player);

    void getRecordSafe(String playerName, Consumer<PlayerRecord> consumer);

    void getRecordAsync(UUID uuid, Consumer<PlayerRecord> consumer);

    void updateWins(PlayerRecord record);

    void updateToggle(PlayerRecord record);

    void insertNewRecord(UUID uuid);

    List<PlayerRecord> getLeaderboard();
}
