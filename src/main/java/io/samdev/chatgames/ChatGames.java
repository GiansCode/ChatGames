package io.samdev.chatgames;

import io.samdev.chatgames.command.ChatGamesCommand;
import io.samdev.chatgames.data.DataManager;
import io.samdev.chatgames.data.SqlDataManager;
import io.samdev.chatgames.game.GameManager;
import io.samdev.chatgames.placeholder.PlaceholderHook;
import io.samdev.chatgames.question.QuestionManager;
import io.samdev.chatgames.util.Message;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main class for the ChatGames plugin
 *
 * @author Mas
 * @since 1.0
 */
public class ChatGames extends JavaPlugin
{
    private DataManager dataManager;
    private ChatGamesConfig pluginConfig;
    private YamlConfiguration questionConfig;
    private QuestionManager questionManager;
    private GameManager gameManager;

    @Override
    public void onEnable()
    {
        preInit();

        loadPluginConfig();
        loadQuestionConfig();

        getCommand("chatgames").setExecutor(new ChatGamesCommand(this));

        new PlaceholderHook(this).register();

        dataManager = new SqlDataManager(this);
        questionManager = new QuestionManager(this);
        gameManager = new GameManager(this);
    }

    @Override
    public void onDisable()
    {
        getDataManager().closeConnection();
    }

    public boolean canPlay(Player player)
    {
        return getDataManager().getRecord(player).isToggled() && getPluginConfig().isAllowedWorld(player.getWorld());
    }

    public void reload()
    {
        Message.init(this);

        loadPluginConfig();
        loadQuestionConfig();

        getQuestionManager().loadQuestions();
    }

    private void preInit()
    {
        saveDefaultConfig();
        saveResourceIfNew("questions.yml");

        Message.init(this);
    }

    private void loadPluginConfig()
    {
        reloadConfig();

        pluginConfig = new ChatGamesConfig(getConfig());
    }

    private void loadQuestionConfig()
    {
        questionConfig = saveResourceIfNew("questions.yml");
    }

    private YamlConfiguration saveResourceIfNew(String name)
    {
        File file = new File(getDataFolder(), name);

        if (!file.exists())
        {
            saveResource(name, false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public ChatGamesConfig getPluginConfig()
    {
        return pluginConfig;
    }

    public YamlConfiguration getQuestionConfig()
    {
        return questionConfig;
    }

    public DataManager getDataManager()
    {
        return dataManager;
    }

    public QuestionManager getQuestionManager()
    {
        return questionManager;
    }

    public GameManager getGameManager()
    {
        return gameManager;
    }
}
