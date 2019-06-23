package io.samdev.chatgames.command;

import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.command.sub.CommandReload;
import io.samdev.chatgames.command.sub.CommandStart;
import io.samdev.chatgames.command.sub.CommandStop;
import io.samdev.chatgames.command.sub.CommandToggle;
import io.samdev.chatgames.command.sub.CommandTop;
import io.samdev.chatgames.command.sub.CommandWins;
import io.samdev.chatgames.util.Message;
import io.samdev.chatgames.util.Permission;
import io.samdev.chatgames.util.UtilString;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public class ChatGamesCommand implements CommandExecutor
{
    private final ChatGames plugin;

    public ChatGamesCommand(ChatGames plugin)
    {
        this.plugin = plugin;
        this.subCommands = new HashSet<>();

        // Commands
        registerSubCommand(new CommandReload(plugin));

        registerSubCommand(new CommandStart(plugin));
        registerSubCommand(new CommandStop(plugin));

        registerSubCommand(new CommandWins(plugin));
        registerSubCommand(new CommandToggle(plugin));
        registerSubCommand(new CommandTop(plugin));
    }

    private final Set<SubCommand> subCommands;

    private void registerSubCommand(SubCommand subCommand)
    {
        subCommands.add(subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!Permission.COMMAND_MAIN.has(sender))
        {
            return true;
        }

        if (args.length == 0)
        {
            sendUsage(sender);
            return true;
        }

        String sub = args[0].toLowerCase();

        for (SubCommand subCommand : subCommands)
        {
            if (subCommand.isAlias(sub))
            {
                if (subCommand.hasPermission(sender, true))
                {
                    subCommand.execute(sender, UtilString.removeFirst(args));
                }

                return true;
            }
        }

        Message.COMMAND_MAIN_USAGE.send(sender);
        return true;
    }

    private void sendUsage(CommandSender sender)
    {
        Message.COMMAND_MAIN_EXECUTED.send(sender,
            "version", plugin.getDescription().getVersion(),
            "author", plugin.getDescription().getAuthors().get(0)
        );
    }
}
