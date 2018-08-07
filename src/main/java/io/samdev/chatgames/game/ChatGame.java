package io.samdev.chatgames.game;

import io.samdev.chatgames.api.event.ChatGameEndEvent;
import io.samdev.chatgames.api.event.ChatGameStartEvent;
import io.samdev.chatgames.api.event.ChatGameWinEvent;
import io.samdev.chatgames.question.Question;
import io.samdev.chatgames.util.Message;
import io.samdev.chatgames.util.UtilPlayer;
import io.samdev.chatgames.util.UtilServer;
import org.bukkit.entity.Player;

public class ChatGame
{
    private final Question question;
    private final long started;

    ChatGame(Question question)
    {
        this.question = question;
        this.started = System.currentTimeMillis();
    }

    private boolean ended = false;

    boolean hasEnded()
    {
        return ended;
    }

    public Question getQuestion()
    {
        return question;
    }

    boolean start()
    {
        if (UtilServer.callEvent(new ChatGameStartEvent(this)).isCancelled())
        {
            return false;
        }

        question.broadcastQuestion();
        return true;
    }

    void end()
    {
        ended = true;

        UtilServer.callEvent(new ChatGameEndEvent(this));
        Message.GAME_END.broadcast("answer", question.getValidAnswer());
    }

    void win(Player winner)
    {
        if (!UtilServer.callEvent(new ChatGameWinEvent(this, winner)).isCancelled())
        {
            ended = true;

            // Try to account for player ping as best as we can
            // in order to give a closer value to the "true" time
            // it took the player to correctly answer the challenge
            question.executeRewards(winner, (System.currentTimeMillis() - UtilPlayer.getPing(winner)) - started);
        }
    }
}
