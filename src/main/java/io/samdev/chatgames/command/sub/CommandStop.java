package io.samdev.chatgames.command.sub;

import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.command.SubCommand;
import io.samdev.chatgames.util.Message;
import io.samdev.chatgames.util.Permission;
import org.bukkit.command.CommandSender;

public class CommandStop extends SubCommand
{
    public CommandStop(ChatGames plugin)
    {
        super(plugin, null, Permission.COMMAND_STOP, "stop", "disable");
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (!plugin.getGameManager().isEnabled())
        {
            Message.COMMAND_STOP_ALREADY_DISABLED.send(sender);
            return;
        }

        plugin.getGameManager().toggle();
        Message.COMMAND_STOP_EXECUTED.send(sender);
    }
}
