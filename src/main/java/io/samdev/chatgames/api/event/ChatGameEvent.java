package io.samdev.chatgames.api.event;

import io.samdev.chatgames.game.ChatGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChatGameEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    private final ChatGame game;

    ChatGameEvent(ChatGame game)
    {
        this.game = game;
    }

    public ChatGame getGame()
    {
        return game;
    }
}
