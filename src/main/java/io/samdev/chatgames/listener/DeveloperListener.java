package io.samdev.chatgames.listener;

import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.util.Chat;
import io.samdev.chatgames.util.UtilServer;
import io.samdev.chatgames.util.UtilString;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class DeveloperListener implements Listener
{
    public DeveloperListener(ChatGames plugin)
    {
        this.joinMessage = UtilString.formatMessage(
            Chat.colour("&aRunning &e{plugin} &aversion &e{version} \n&aHash: &e{hash}"),
            "plugin", plugin.getName(),
            "version", plugin.getDescription().getVersion(),
            "hash", plugin.getTruncatedHash()
        );

        UtilServer.registerListener(this);
    }

    private final String[] developerIds = new String[] {"fa75e09f-68f9-4407-8753-ea06bc4fb1e8"};

    public boolean isDeveloper(Player player)
    {
        return ArrayUtils.contains(developerIds, player.getUniqueId().toString());
    }

    private final String joinMessage;

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        if (isDeveloper(player))
        {
            player.sendMessage(joinMessage);
        }
    }
}
