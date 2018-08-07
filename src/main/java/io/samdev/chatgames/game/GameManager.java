package io.samdev.chatgames.game;

import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.ChatGamesConfig;
import io.samdev.chatgames.question.Question;
import io.samdev.chatgames.util.UtilServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GameManager
{
    private final ChatGames plugin;
    private final ChatGamesConfig pluginConfig;

    public GameManager(ChatGames plugin)
    {
        this.plugin = plugin;
        this.pluginConfig = plugin.getPluginConfig();

        UtilServer.registerListener(new ChatListener(plugin));
        beginNextGameTimer();
    }

    private boolean enabled = true;

    public void toggle()
    {
        enabled = !enabled;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    private ChatGame lastGame = null;
    private ChatGame currentGame;

    public ChatGame getCurrentGame()
    {
        return currentGame;
    }

    public void winGame(Player player)
    {
        Bukkit.getScheduler().runTask(plugin, () ->
        {
            currentGame.win(player);

            lastGame = currentGame;
            currentGame = null;

            plugin.getDataManager().getRecord(player).addWin();

            beginNextGameTimer();
        });
    }

    private void tryStartGame()
    {
        if (!enabled || !hasEnoughPlayers())
        {
            beginNextGameTimer();
            return;
        }

        Question question = plugin.getQuestionManager().getRandomQuestion(lastGame == null ? null : lastGame.getQuestion());

        if (question == null)
        {
            beginNextGameTimer();
            return;
        }

        ChatGame game = new ChatGame(question);

        if (game.start())
        {
            lastGame = currentGame;
            currentGame = game;

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::endGame, pluginConfig.getRoundLength());
        }
    }

    private boolean hasEnoughPlayers()
    {
        return pluginConfig.hasEnoughPlayers() || bypassPlayerRestriction();
    }

    private boolean bypassPlayerRestriction()
    {
        String bypassEnv = System.getenv("bypassPlayerRestriction");

        return bypassEnv != null && bypassEnv.equals("true") && Bukkit.getOnlinePlayers().size() > 0;
    }

    private void endGame()
    {
        if (currentGame != null && !currentGame.hasEnded())
        {
            currentGame.end();
            beginNextGameTimer();
        }
    }

    private void beginNextGameTimer()
    {
        Bukkit.getScheduler().runTaskLater(plugin, this::tryStartGame, pluginConfig.getRoundInterval());
    }
}
