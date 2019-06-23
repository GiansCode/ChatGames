package io.samdev.chatgames.question;

import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.util.Chat;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class QuestionManager
{
    private final ChatGames plugin;

    public QuestionManager(ChatGames plugin)
    {
        this.plugin = plugin;

        loadQuestions();
    }

    private final List<Question> questions = new ArrayList<>();

    public Question getRandomQuestion(Question lastQuestion)
    {
        if (questions.size() == 0)
        {
            return null;
        }
        else if (questions.size() == 1)
        {
            return questions.get(0);
        }

        Question question;

        do
        {
            question = questions.get(ThreadLocalRandom.current().nextInt(questions.size()));
        } while (question == lastQuestion);

        return question;
    }

    public void loadQuestions()
    {
        questions.clear();

        YamlConfiguration questionConfig = plugin.getQuestionConfig();
        ConfigurationSection questionsSection = questionConfig.getConfigurationSection("questions");

        for (String key : questionsSection.getKeys(false))
        {
            loadQuestion(questionsSection.getConfigurationSection(key));
        }
    }

    private void loadQuestion(ConfigurationSection section)
    {
        String name = section.getName();

        List<String> question = section.getStringList("question");
        question.replaceAll(Chat::colour);

        List<String> answers = section.getStringList("answers");
        boolean caseSensitive = section.getBoolean("case_sensitive");

        List<String> actions = section.getStringList("actions");

        questions.add(new Question(name, question, answers, caseSensitive, actions));
    }
}
