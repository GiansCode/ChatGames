package io.samdev.chatgames.command.sub;

import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.command.SubCommand;
import io.samdev.chatgames.util.Message;
import io.samdev.chatgames.util.Permission;
import org.bukkit.command.CommandSender;

import java.util.concurrent.atomic.AtomicInteger;

public class CommandTop extends SubCommand
{
    public CommandTop(ChatGames plugin)
    {
        super(plugin, null, Permission.COMMAND_TOP, "top", "leaderboard");
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        AtomicInteger count = new AtomicInteger();

        plugin.getDataManager().getLeaderboard().forEach(record ->
            Message.COMMAND_TOP_FORMAT.send(sender,
                "position", count.incrementAndGet(),
                "player", record.getPlayerName(),
                "wins", record.getWins()
            )
        );
    }
}
