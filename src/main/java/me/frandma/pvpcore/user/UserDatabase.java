package me.frandma.pvpcore.user;

import com.sun.org.apache.xpath.internal.operations.Bool;
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
                            "streak int NOT NULL," +
                            "gems int NOT NULL);"
            ))
            {
                preparedStatement.execute();
            }
        } catch (SQLException | ClassNotFoundException exception) {
            Bukkit.getLogger().severe(exception.toString());
        }
    }

    public CompletableFuture<Boolean> exists(UUID uuid) {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT uuid FROM users WHERE uuid = ?;")) {
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.execute();
                return preparedStatement.getResultSet().getString(1) != null;
            } catch (SQLException exception) {
                exception.printStackTrace();
                return null;
            }
        });
        future.exceptionally(exception -> {
            exception.printStackTrace();
            return null;
        });
        return future;
    }

    public CompletableFuture<Void> insertOrIgnoreUser(UUID uuid) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT uuid FROM users WHERE uuid = ?;")) {
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.execute();

                if (preparedStatement.getResultSet().getString(1) != null) return null;

            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (uuid,kills,deaths,streak,gems) VALUES (?,?,?,?,?);")) {
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setInt(2, 0);
                preparedStatement.setInt(3, 0);
                preparedStatement.setInt(4, 0);
                preparedStatement.setInt(5, 0);
                preparedStatement.execute();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            return null;
        });
        future.exceptionally(exception -> {
            exception.printStackTrace();
            return null;
        });
        return future;
    }

    public CompletableFuture<Stats> fetchStats(UUID uuid) {
        CompletableFuture<Stats> future = CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT kills, deaths, streak, gems FROM users WHERE uuid = ?")) {
                preparedStatement.setString(1, uuid.toString());
                ResultSet rs = preparedStatement.getResultSet();
                Stats stats = new Stats(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
                return stats;
            } catch (SQLException exception) {
                exception.printStackTrace();
                return null;
            }
        });
        future.exceptionally(exception -> {
            exception.printStackTrace();
            return null;
        });
        return future;
    }

    public CompletableFuture<Void> updateStats(User user) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE users SET kills = ?, deaths = ?, streak = ?, gems = ? WHERE uuid = ?")) {
                statement.setInt(1, user.getStats().getKills());
                statement.setInt(2, user.getStats().getDeaths());
                statement.setInt(3, user.getStats().getStreak());
                statement.setInt(4, user.getStats().getGems());
                statement.setString(5, user.getUniqueId().toString());
                statement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            return null;
        });
        future.exceptionally(exception -> {
            exception.printStackTrace();
            return null;
        });
        return future;
    }
}

