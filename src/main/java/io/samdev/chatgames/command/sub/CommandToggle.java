package io.samdev.chatgames.command.sub;

import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.command.SubCommand;
import io.samdev.chatgames.util.Message;
import io.samdev.chatgames.util.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandToggle extends SubCommand
{
    public CommandToggle(ChatGames plugin)
    {
        super(plugin, null, Permission.COMMAND_TOGGLE, "toggle");
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (!playerCheck(sender))
        {
            return;
        }

        boolean enabled = plugin.getDataManager().getRecord((Player) sender).toggle();

        if (enabled)
        {
            Message.COMMAND_TOGGLE_ENABLED.send(sender);
        }
        else
        {
            Message.COMMAND_TOGGLE_DISABLED.send(sender);
        }

    }
}
