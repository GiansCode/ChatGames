package io.samdev.chatgames.api.event;

import io.samdev.chatgames.game.ChatGame;
import org.bukkit.entity.Player;

public final class ChatGameWinEvent extends CancellableChatGameEvent
{
    private final Player winner;

    public ChatGameWinEvent(ChatGame game, Player winner)
    {
        super(game);

        this.winner = winner;
    }

    public Player getWinner()
    {
        return winner;
    }
}
