package me.frandma.pvpcore.kits;

import me.frandma.pvpcore.PVPCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class KitDatabase {

    private static Connection connection;

    public KitDatabase() {
        try {
            //make the connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + new File(PVPCorePlugin.getInstance().getDataFolder(), "kits.db"));

            //create table
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS kits(" +
                            "name varchar(36) NOT NULL," +
                            "cooldown varchar(36) NOT NULL," +
                            "inventorySlot int NOT NULL," +
                            "displayItem varchar(36) NOT NULL," +
                            "items varchar(255) NOT NULL);"
            ))
            {
                preparedStatement.execute();
            }
        } catch (SQLException | ClassNotFoundException exception) {
            Bukkit.getLogger().severe(exception.toString());
        }
    }

    public static CompletableFuture<Void> insertKit(Kit kit) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO kits (name, cooldown, inventorySlot, displayItem, items) VALUES (?,?,?,?,?);")) {
                preparedStatement.setString(1, kit.getName());
                preparedStatement.setInt(2, kit.getCooldown());
                preparedStatement.setInt(3, kit.getInventorySlot());
                preparedStatement.setString(4, kit.getDisplayItem().toString());
                preparedStatement.setString(5, kit.toBase64());
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

    public static CompletableFuture<Void> deleteKit(String kitName) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM kits WHERE name = ?;")) {
                preparedStatement.setString(1, kitName);
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

    protected static CompletableFuture<List<Kit>> fetchKits() {
        CompletableFuture<List<Kit>> future = CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT name, cooldown, inventorySlot, displayItem, items FROM kits;")) {
                preparedStatement.execute();
                ResultSet rs = preparedStatement.getResultSet();
                List<Kit> kitList = new ArrayList<>();
                while (rs.next()) {
                    Kit kit = new Kit(rs.getString(1), rs.getInt(2), rs.getInt(3), Material.valueOf(rs.getString(4)), rs.getString(5));
                    kitList.add(kit);
                }
                return kitList;
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
}