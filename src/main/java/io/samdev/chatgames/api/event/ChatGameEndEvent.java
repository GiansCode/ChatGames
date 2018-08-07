package io.samdev.chatgames.api.event;

import io.samdev.chatgames.game.ChatGame;

public final class ChatGameEndEvent extends ChatGameEvent
{
    public ChatGameEndEvent(ChatGame game)
    {
        super(game);
    }
}
