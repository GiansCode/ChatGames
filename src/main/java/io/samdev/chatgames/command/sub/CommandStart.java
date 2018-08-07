package io.samdev.chatgames.command.sub;

import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.command.SubCommand;
import io.samdev.chatgames.util.Message;
import io.samdev.chatgames.util.Permission;
import org.bukkit.command.CommandSender;

public class CommandStart extends SubCommand
{
    public CommandStart(ChatGames plugin)
    {
        super(plugin, null, Permission.COMMAND_START, "start", "enable");
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (plugin.getGameManager().isEnabled())
        {
            Message.COMMAND_START_ALREADY_ENABLED.send(sender);
            return;
        }

        plugin.getGameManager().toggle();
        Message.COMMAND_START_EXECUTED.send(sender);
    }
}
