package io.samdev.chatgames.util;

import org.bukkit.command.CommandSender;

public enum Permission
{
    COMMAND_MAIN("command.main"),
    COMMAND_RELOAD("command.reload"),
    COMMAND_WINS("command.wins"),
    COMMAND_WINS_OTHERS("command.wins.others"),
    COMMAND_TOP("command.top"),
    COMMAND_START("command.start"),
    COMMAND_STOP("command.stop"),
    COMMAND_TOGGLE("command.toggle"),
    ;

    private final String node;

    Permission(String node)
    {
        this.node = "chatgames." + node;
    }

    public boolean has(CommandSender sender)
    {
        return has(sender, true);
    }

    public boolean has(CommandSender sender, boolean inform)
    {
        if (!sender.hasPermission(node))
        {
            if (inform)
            {
                Message.NO_PERMISSION.send(sender);
            }

            return false;
        }

        return true;
    }
}
