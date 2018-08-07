package io.samdev.chatgames.command.sub;

import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.command.SubCommand;
import io.samdev.chatgames.util.Message;
import io.samdev.chatgames.util.Permission;
import org.bukkit.command.CommandSender;

public class CommandReload extends SubCommand
{
    public CommandReload(ChatGames plugin)
    {
        super(plugin, null, Permission.COMMAND_RELOAD, "reload", "rl");
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        plugin.reload();
        Message.COMMAND_RELOAD_EXECUTED.send(sender);
    }
}
