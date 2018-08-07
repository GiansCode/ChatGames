package io.samdev.chatgames.api.event;

import io.samdev.chatgames.game.ChatGame;
import org.bukkit.event.Cancellable;

public class CancellableChatGameEvent extends ChatGameEvent implements Cancellable
{
    CancellableChatGameEvent(ChatGame game)
    {
        super(game);
    }

    private boolean cancelled = false;

    @Override
    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }
}
