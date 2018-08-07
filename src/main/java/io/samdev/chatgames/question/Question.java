package io.samdev.chatgames.question;

import io.samdev.chatgames.util.Chat;
import io.samdev.chatgames.util.UtilServer;
import io.samdev.chatgames.util.UtilString;
import io.samdev.chatgames.util.UtilTime;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Question
{
    private final String name;
    private final List<String> question;
    private final List<String> answers;
    private final boolean caseSensitive;
    private final Map<RewardType, String> rewards;

    Question(String name, List<String> question, List<String> answers, boolean caseSensitive, List<String> rewards)
    {
        this.name = name;
        this.question = question;
        this.answers = answers;
        this.caseSensitive = caseSensitive;
        this.rewards = Question.parseRewards(rewards);
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
        rewards.forEach((type, command) -> type.execute(formatCommand(command, player, time), player));
    }

    private String formatCommand(String command, Player player, long time)
    {
        return UtilString.formatMessage(command,
            "player", player.getName(),
            "question", name,
            "time", UtilTime.formatSeconds(time)
        );
    }

    private static Map<RewardType, String> parseRewards(List<String> rawRewards)
    {
        Map<RewardType, String> rewards = new HashMap<>();

        for (String rawReward : rawRewards)
        {
            String[] split = rawReward.split(" ");

            RewardType type;

            try
            {
                type = RewardType.valueOf(split[0].toUpperCase().replaceAll("\\[(.*)]", "$1"));
            }
            catch (IllegalArgumentException ex)
            {
                UtilServer.getLogger().severe("Invalid question type: " + split[0].toUpperCase());
                continue;
            }

            String command = UtilString.join(split, " ", 1);

            rewards.put(type, command);
        }

        return rewards;
    }
}
