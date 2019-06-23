package io.samdev.chatgames.command;

import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.util.Message;
import io.samdev.chatgames.util.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand
{
    protected final ChatGames plugin;

    private final Message usageMessage;
    private final Permission permission;
    private final String[] aliases;

    public SubCommand(ChatGames plugin, Message usageMessage, Permission permission, String... aliases)
    {
        this.plugin = plugin;

        assert aliases.length != 0 : "at least one alias must be provided";

        this.usageMessage = usageMessage;
        this.permission = permission;
        this.aliases = aliases;
    }

    boolean isAlias(String input)
    {
        for (String alias: aliases)
        {
            if (alias.equalsIgnoreCase(input))
            {
                return true;
            }
        }

        return false;
    }

    boolean hasPermission(CommandSender sender, boolean inform)
    {
        return permission.has(sender, inform);
    }

    protected void sendUsage(CommandSender sender)
    {
        usageMessage.send(sender);
    }

    protected boolean playerCheck(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            Message.NO_CONSOLE.send(sender);
            return false;
        }

        return true;
    }

    public abstract void execute(CommandSender sender, String[] args);
}
