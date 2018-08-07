package io.samdev.chatgames.command.sub;

import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.command.SubCommand;
import io.samdev.chatgames.data.PlayerRecord;
import io.samdev.chatgames.util.Message;
import io.samdev.chatgames.util.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandWins extends SubCommand
{
    public CommandWins(ChatGames plugin)
    {
        super(plugin, Message.COMMAND_WINS_USAGE, Permission.COMMAND_WINS, "wins");
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (args.length > 2)
        {
            sendUsage(sender);
            return;
        }

        if (args.length == 1 && Permission.COMMAND_WINS_OTHERS.has(sender))
        {
            plugin.getDataManager().getRecordSafe(args[0], record ->
            {
                if (record == null)
                {
                    Message.COMMAND_WINS_NOT_FOUND.send(sender);
                    return;
                }

                Message.COMMAND_WINS_OTHERS.send(sender,
                    "player", record.getPlayerName(),
                    "wins", record.getWins()
                );
            });

            return;
        }

        if (playerCheck(sender))
        {
            PlayerRecord record = plugin.getDataManager().getRecord((Player) sender);

            Message.COMMAND_WINS_SELF.send(sender,
                "wins", record.getWins()
            );
        }
    }
}
