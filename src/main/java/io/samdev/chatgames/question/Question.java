package io.samdev.chatgames.question;

import io.samdev.actionutil.ActionUtil;
import io.samdev.chatgames.util.Chat;
import io.samdev.chatgames.util.UtilString;
import io.samdev.chatgames.util.UtilTime;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Question
{
    private final String name;
    private final List<String> question;
    private final List<String> answers;
    private final boolean caseSensitive;
    private final List<String> actions;

    Question(String name, List<String> question, List<String> answers, boolean caseSensitive, List<String> actions)
    {
        this.name = name;
        this.question = question;
        this.answers = answers;
        this.caseSensitive = caseSensitive;
        this.actions = actions;
    }

    public void broadcastQuestion()
    {
        Chat.broadcast(question);
    }

    public String getValidAnswer()
    {
        return answers.get(0);
    }

    public boolean isValidAnswer(String message)
    {
        for (String answer : answers)
        {
            if (caseSensitive ? message.equals(answer) : message.equalsIgnoreCase(answer))
            {
                return true;
            }
        }

        return false;
    }

    public void executeRewards(Player player, long time)
    {
        List<String> formattedActions = new ArrayList<>(actions)
            .stream()
            .map(action -> formatAction(action, player, time))
            .collect(toList());

        ActionUtil.executeActions(player, formattedActions);
    }

    private String formatAction(String action, Player player, long time)
    {
        return UtilString.formatMessage(action,
            "player", player.getName(),
            "question", name,
            "time", UtilTime.formatSeconds(time)
        );
    }
}
