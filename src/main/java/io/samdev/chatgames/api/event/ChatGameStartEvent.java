package io.samdev.chatgames.api.event;

import io.samdev.chatgames.game.ChatGame;

public final class ChatGameStartEvent extends CancellableChatGameEvent
{
    public ChatGameStartEvent(ChatGame game)
    {
        super(game);
    }
}
