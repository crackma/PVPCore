package me.frandma.pvpcore.user;

import me.frandma.pvpcore.PVPCore;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserDatabase {

     private static Connection connection;

    public UserDatabase() {
        try {
            //make the connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + new File(PVPCore.getInstance().getDataFolder(), "users.db"));

            //create table
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS users(" +
                            "uuid varchar(36) NOT NULL," +
                            "kills int NOT NULL," +
                            "deaths int NOT NULL," +
                            "gems int NOT NULL);"
            ))
            {
                preparedStatement.execute();
            }
        } catch (SQLException | ClassNotFoundException exception) {
            Bukkit.getLogger().severe(exception.toString());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static void createUser(UUID uuid) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT OR IGNORE INTO users (uuid,kills,deaths,gems) VALUES (?,?,?,?);")) {
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setInt(2, 0);
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, 0);
        } catch (SQLException exception) {
            Bukkit.getLogger().severe(exception.toString());
        }
    }

    private static ResultSet getResultSet(UUID e) {
        Bukkit.getLogger().info("getresultset");
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT kills, deaths, gems FROM users WHERE uuid = ?")) {
            return preparedStatement.getResultSet();
        } catch (SQLException exception) {
            Bukkit.getLogger().severe(exception.toString());
            return null;
        }
    }

    public static Stats getStats(UUID uuid) {
        Bukkit.getLogger().info("getstats");
        Stats stats = new Stats();

        CompletableFuture<ResultSet> future = CompletableFuture.supplyAsync(() -> {
            ResultSet resultSet = getResultSet(uuid);
            return resultSet;
        });

        future.exceptionally(error -> {
            error.printStackTrace();
            return null;
        });

        future.thenAccept(data -> {
            try {
                stats.setKills(data.getInt(1));
                stats.setDeaths(data.getInt(2));
                stats.setGems(data.getInt(3));
            } catch (SQLException exception) {
                Bukkit.getLogger().severe(exception.toString());
            }
        });
        return stats;
    }

    public void updateStats(User user) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE users SET kills = ?, deaths = ?, gems = ? WHERE uuid = ?")) {
            statement.setInt(1, user.getStats().getKills() + 1);
            statement.setInt(2, user.getStats().getDeaths() + 1);
            statement.setInt(2, user.getStats().getGems() + 1);
            statement.setString(4, user.getUuid().toString());
            statement.executeUpdate();
        } catch (SQLException exception) {
            Bukkit.getLogger().severe(exception.toString());
        }
    }

}

