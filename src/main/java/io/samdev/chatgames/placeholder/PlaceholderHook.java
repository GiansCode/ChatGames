package io.samdev.chatgames.placeholder;

import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.data.PlayerRecord;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

import java.util.List;

public class PlaceholderHook extends EZPlaceholderHook
{
    private final ChatGames plugin;

    public PlaceholderHook(ChatGames plugin)
    {
        super(plugin, "chatgames");

        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier)
    {
        if (player != null)
        {
            if (identifier.equals("wins"))
            {
                return String.valueOf(plugin.getDataManager().getRecord(player).getWins());
            }

            if (identifier.equals("toggled"))
            {
                return String.valueOf(plugin.getDataManager().getRecord(player).isToggled());
            }
        }
        else
        {
            if (identifier.startsWith("chatgames_top_name_"))
            {
                int position = Integer.parseInt(identifier.split("chatgames_top_name_")[1]);

                if (position < 1 || position > 10)
                {
                    return null;
                }

                List<PlayerRecord> leaderboard = plugin.getDataManager().getLeaderboard();

                if (position > leaderboard.size())
                {
                    return null;
                }

                return leaderboard.get(position - 1).getPlayerName();
            }

            if (identifier.startsWith("chatgames_top_wins_"))
            {
                int position = Integer.parseInt(identifier.split("chatgames_top_wins_")[1]);

                if (position < 1 || position > 10)
                {
                    return null;
                }

                List<PlayerRecord> leaderboard = plugin.getDataManager().getLeaderboard();

                if (position > leaderboard.size())
                {
                    return null;
                }

                return String.valueOf(leaderboard.get(position - 1).getWins());
            }
        }

        return null;
    }
}
