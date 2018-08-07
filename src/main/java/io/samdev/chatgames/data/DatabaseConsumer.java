package io.samdev.chatgames.data;

import java.sql.SQLException;

@FunctionalInterface
public interface DatabaseConsumer<T>
{
    void accept(T data) throws SQLException;
}
