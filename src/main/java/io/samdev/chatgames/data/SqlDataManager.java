package io.samdev.chatgames.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.samdev.chatgames.ChatGames;
import io.samdev.chatgames.util.UtilServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class SqlDataManager implements DataManager, Listener
{
    private final ChatGames plugin;

    public SqlDataManager(ChatGames plugin)
    {
        this.plugin = plugin;

        UtilServer.registerListener(this);

        try
        {
            openConnection();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();

            UtilServer.getLogger().severe("Could not connect to MySQL, disabling plugin");
            Bukkit.getPluginManager().disablePlugin(plugin);

            return;
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::refreshLeaderboard,0L, 100L);
    }

    private final Map<UUID, PlayerRecord> records = new HashMap<>();

    private HikariDataSource pool;

    private void openConnection() throws SQLException
    {
        // Credentials
        String host = plugin.getConfig().getString("sql.host");
        int port = plugin.getConfig().getInt("sql.port");
        String database = plugin.getConfig().getString("sql.database");
        String username = plugin.getConfig().getString("sql.username");
        String password = plugin.getConfig().getString("sql.password");

        // Configuration
        HikariConfig config = new HikariConfig();
        config.setPoolName("chatgames");

        config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		
		config.addDataSourceProperty("serverName", host);
        config.addDataSourceProperty("port", port);
        config.addDataSourceProperty("databaseName", database);

		config.setUsername(username);
        config.setPassword(password);
		
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("useUnicode","true");
        config.addDataSourceProperty("characterEncoding","utf8");

        config.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() + 1);
        config.setMinimumIdle(config.getMaximumPoolSize());
        config.setMaxLifetime(1800000L);
        config.setConnectionTimeout(5000L);

        config.setConnectionTimeout(30_000L);

        pool = new HikariDataSource(config);

        createTable();
    }

    private final String createTableStatement = "CREATE TABLE IF NOT EXISTS records (uuid VARCHAR(36) PRIMARY KEY, wins INT, toggle BOOL);";

    private void createTable() throws SQLException
    {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(createTableStatement))
        {
            statement.execute();
        }
    }

    @Override
    public void closeConnection()
    {
        pool.close();
    }

    @Override
    public PlayerRecord getRecord(Player player)
    {
        return records.get(player.getUniqueId());
    }

    private final String fetchStatement = "SELECT * FROM records WHERE uuid=?";

    @Override
    public void getRecordSafe(String playerName, Consumer<PlayerRecord> consumer)
    {
        Player player = Bukkit.getPlayer(playerName);

        if (player != null)
        {
            consumer.accept(getRecord(player));
            return;
        }

        getRecordAsync(Bukkit.getOfflinePlayer(playerName).getUniqueId(), consumer);
    }

    @Override
    public void getRecordAsync(UUID uuid, Consumer<PlayerRecord> consumer)
    {
        UtilServer.runAsync(() ->
        {
            executeSql(fetchStatement, statement ->
            {
                statement.setString(1, uuid.toString());

                ResultSet results = statement.executeQuery();

                if (!results.first())
                {
                    UtilServer.runSync(() -> consumer.accept(null));
                    return;
                }

                PlayerRecord record = parseRecord(results);
                results.close();

                UtilServer.runSync(() -> consumer.accept(record));
            });
        });
    }

    private PlayerRecord parseRecord(ResultSet results) throws SQLException
    {
        UUID uuid = UUID.fromString(results.getString("uuid"));
        int wins = results.getInt("wins");
        boolean toggle = results.getBoolean("toggle");

        return new PlayerRecord(plugin, uuid, wins, toggle);
    }

    private final String updateWinsStatement = "UPDATE records SET wins=? WHERE uuid=?;";

    @Override
    public void updateWins(PlayerRecord record)
    {
        UtilServer.runAsync(() ->
        {
            executeSql(updateWinsStatement, statement ->
            {
                try
                {
                    statement.setInt(1, record.getWins());
                    statement.setString(2, record.getPlayerUuid().toString());

                    statement.execute();
                }
                catch (SQLException ex)
                {
                    UtilServer.getLogger().severe("Error updating player wins");
                    ex.printStackTrace();
                }
            });
        });
    }

    private final String updateToggleStatement = "UPDATE records SET toggle=? WHERE uuid=?;";

    @Override
    public void updateToggle(PlayerRecord record)
    {
        UtilServer.runAsync(() ->
        {
            executeSql(updateToggleStatement, statement ->
            {
                try
                {
                    statement.setBoolean(1, record.isToggled());
                    statement.setString(2, record.getPlayerUuid().toString());

                    statement.execute();
                }
                catch (SQLException ex)
                {
                    UtilServer.getLogger().severe("Error updating player wins");
                    ex.printStackTrace();
                }
            });
        });
    }

    private final String insertStatement = "INSERT INTO records (uuid, wins, toggle) VALUES (?,?,?);";

    @Override
    public void insertNewRecord(UUID uuid)
    {
        UtilServer.runAsync(() ->
        {
            executeSql(insertStatement, statement ->
            {
                statement.setString(1, uuid.toString());
                statement.setInt(2, 0);
                statement.setBoolean(3, true);

                statement.execute();
            });
        });
    }

    private volatile List<PlayerRecord> leaderboard = new ArrayList<>();

    @Override
    public List<PlayerRecord> getLeaderboard()
    {
        return leaderboard;
    }

    private final String leaderboardStatement = "SELECT * FROM records ORDER BY wins DESC LIMIT 10;";

    private void refreshLeaderboard()
    {
        UtilServer.runAsync(() ->
            executeSql(leaderboardStatement, statement ->
            {
                ResultSet results = statement.executeQuery();

                if (!results.first())
                {
                    return;
                }

                List<PlayerRecord> newLeaderboard = new ArrayList<>();

                do
                {
                    UUID uuid = UUID.fromString(results.getString("uuid"));
                    Player player = Bukkit.getPlayer(uuid);

                    if (player != null)
                    {
                        newLeaderboard.add(getRecord(player));
                    }
                    else
                    {
                        newLeaderboard.add(parseRecord(results));
                    }
                }
                while (results.next());

                leaderboard = newLeaderboard;
            })
        );
    }

    private void executeSql(String statement, DatabaseConsumer<PreparedStatement> consumer)
    {
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement))
        {
            consumer.accept(preparedStatement);
            preparedStatement.close();
        }
        catch (SQLException ex)
        {
            UtilServer.getLogger().severe("Error executing SQL");
            ex.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException
    {
        return pool.getConnection();
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event)
    {
        UUID uuid = event.getUniqueId();

        getRecordAsync(uuid, record ->
        {
            if (record == null)
            {
                record = new PlayerRecord(plugin, uuid, 0, true);
                insertNewRecord(uuid);
            }

            records.put(uuid, record);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        records.remove(event.getPlayer().getUniqueId());
    }
}