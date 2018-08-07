package io.samdev.chatgames.data;

import java.sql.SQLException;

@FunctionalInterface
public interface DatabaseRunnable
{
    void run() throws SQLException;
}
