package io.samdev.chatgames.util;

import io.samdev.chatgames.ChatGames;
import org.bukkit.command.CommandSender;

import java.util.List;

public enum Message
{
    NO_PERMISSION,
    NO_CONSOLE,

    COMMAND_MAIN_USAGE,
    COMMAND_MAIN_EXECUTED,

    COMMAND_RELOAD_EXECUTED,

    COMMAND_START_EXECUTED,
    COMMAND_START_ALREADY_ENABLED,

    COMMAND_STOP_EXECUTED,
    COMMAND_STOP_ALREADY_DISABLED,

    COMMAND_WINS_USAGE,
    COMMAND_WINS_NOT_FOUND,
    COMMAND_WINS_SELF,
    COMMAND_WINS_OTHERS,

    COMMAND_TOGGLE_ENABLED,
    COMMAND_TOGGLE_DISABLED,

    COMMAND_TOP_FORMAT,

    GAME_END
    ;

    private String value = name();

    public void send(CommandSender sender, Object... params)
    {
        if (value != null)
        {
            sender.sendMessage(UtilString.formatMessage(value, params));
        }
    }

    public void broadcast(Object... params)
    {
        Chat.broadcast(UtilString.formatMessage(value, params));
    }

    @SuppressWarnings("unchecked")
    public static void init(ChatGames plugin)
    {
        for (Message message : values())
        {
            Object object = plugin.getConfig().get("messages." + message.name().toLowerCase());

            if (object == null)
            {
                plugin.getLogger().severe("Value missing for message " + message.name());
                continue;
            }

            String value =
                object instanceof String ? (String) object :
                    object instanceof List ? String.join("\n", (List<String>) object) :
                    null;

            if (value == null)
            {
                plugin.getLogger().severe("Invalid data type for message " + message.name());
                continue;
            }

            message.value = Chat.colour(value);
        }
    }
}
