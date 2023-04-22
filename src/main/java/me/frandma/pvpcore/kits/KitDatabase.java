package me.frandma.pvpcore.kits;

import me.frandma.pvpcore.PVPCore;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class KitDatabase {
    private Connection connection;

    public KitDatabase() {
        try {
            //make the connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + new File(PVPCore.getInstance().getDataFolder(), "kits.db"));

            //create table
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS kits(" +
                            "name varchar(36) NOT NULL," +
                            "items int NOT NULL);"
            ))
            {
                preparedStatement.execute();
            }
        } catch (SQLException | ClassNotFoundException exception) {
            Bukkit.getLogger().severe(exception.toString());
        }
    }

    public CompletableFuture<Void> insertKit(Kit kit) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (name, items) VALUES (?,?);")) {
                preparedStatement.setString(1, kit.getName());
                preparedStatement.setString(2, kit.toBase64());
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
}
