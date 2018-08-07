package io.samdev.chatgames;

import io.samdev.chatgames.command.ChatGamesCommand;
import io.samdev.chatgames.data.DataManager;
import io.samdev.chatgames.data.SqlDataManager;
import io.samdev.chatgames.game.GameManager;
import io.samdev.chatgames.listener.DeveloperListener;
import io.samdev.chatgames.placeholder.PlaceholderHook;
import io.samdev.chatgames.question.QuestionManager;
import io.samdev.chatgames.util.Message;
import io.samdev.chatgames.util.UtilFile;
import io.samdev.chatgames.util.UtilString;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URL;

/**
 * Main class for the ChatGames plugin
 *
 * @author Mas
 * @since 1.0
 */
public class ChatGames extends JavaPlugin
{
    private String fileHash;

    private DeveloperListener developerListener;
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

        new PlaceholderHook(this);

        developerListener = new DeveloperListener(this);
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
        initHash();

        saveDefaultConfig();
        saveResourceIfNew("questions.yml");

        Message.init(this);
    }

    private void initHash()
    {
        fileHash = UtilFile.sha256Digest(new File("plugins", getPluginJarName()));

        getLogger().info("Hash: " + fileHash);
    }

    private String getPluginJarName()
    {
        URL url = getClassLoader().getResource("plugin.yml");
        assert url != null : "This error shouldn't happen, contact a dev";

        return url.getPath().replaceAll("^.*/(.*\\.jar).*/plugin\\.yml$", "$1");
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

    /* Public Dependency Access */
    public String getTruncatedHash()
    {
        return UtilString.truncateString(getFileHash(), 16);
    }

    public String getFileHash()
    {
        return fileHash;
    }

    public ChatGamesConfig getPluginConfig()
    {
        return pluginConfig;
    }

    public YamlConfiguration getQuestionConfig()
    {
        return questionConfig;
    }

    public DeveloperListener getDeveloperListener()
    {
        return developerListener;
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
