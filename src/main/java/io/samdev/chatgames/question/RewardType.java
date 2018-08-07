package io.samdev.chatgames.question;

import io.samdev.chatgames.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public enum RewardType
{
    CONSOLE(
        (command, player) -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
    ),
    PLAYER(
        (command, player) -> player.performCommand(command)
    ),
    BROADCAST(
        (command, player) -> Chat.broadcast(command)
    )
    ;

    private final BiConsumer<String, Player> function;

    RewardType(BiConsumer<String, Player> function)
    {
        this.function = function;
    }

    void execute(String command, Player player)
    {
        function.accept(command, player);
    }
}
