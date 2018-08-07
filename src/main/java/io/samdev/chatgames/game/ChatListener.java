package io.samdev.chatgames.game;

import io.samdev.chatgames.ChatGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

class ChatListener implements Listener
{
    private final ChatGames plugin;

    ChatListener(ChatGames plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event)
    {
        ChatGame game = plugin.getGameManager().getCurrentGame();
        Player player = event.getPlayer();

        if (game == null || game.hasEnded() || !plugin.canPlay(player))
        {
            return;
        }

        String answer = event.getMessage();

        if (game.getQuestion().isValidAnswer(answer))
        {
            plugin.getGameManager().winGame(player);
        }
    }
}
