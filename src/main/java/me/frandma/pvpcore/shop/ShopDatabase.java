package me.frandma.pvpcore.shop;

import me.frandma.pvpcore.PVPCorePlugin;
import me.frandma.pvpcore.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ShopDatabase {
    private static Connection connection;

    public ShopDatabase() {
        try {
            //make the connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + new File(PVPCorePlugin.getInstance().getDataFolder(), "shop.db"));

            //create table
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS shop(" +
                            "name varchar(36) NOT NULL," +
                            "description varchar(255) NOT NULL," +
                            "displayItem varchar(36) NOT NULL," +
                            "inventorySlot int NOT NULL," +
                            "items varchar(255) NOT NULL);"
            ))
            {
                preparedStatement.execute();
            }
        } catch (SQLException | ClassNotFoundException exception) {
            Bukkit.getLogger().severe(exception.toString());
        }
    }

    public static CompletableFuture<Void> insertCategory(Category category) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO shop (name, description, displayItem, inventorySlot, items) VALUES (?,?,?,?,?);")) {
                preparedStatement.setString(1, category.getName());
                preparedStatement.setString(2, category.getDescription());
                preparedStatement.setString(3, category.getDisplayItem().toString());
                preparedStatement.setInt(4, category.getInventorySlot());
                preparedStatement.setString(5, category.toBase64());
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

    public static CompletableFuture<Void> deleteCategory(String categoryName) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM shop WHERE name = ?;")) {
                preparedStatement.setString(1, categoryName);
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

    protected static CompletableFuture<Set<Category>> fetchCategories() {
        CompletableFuture<Set<Category>> future = CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT name, description, displayItem, inventorySlot, items FROM shop;")) {
                preparedStatement.execute();
                ResultSet rs = preparedStatement.getResultSet();
                Set<Category> categorySet = new HashSet<>();
                while (rs.next()) {
                    Category category = new Category(rs.getString(1), rs.getString(2), Material.valueOf(rs.getString(3)), rs.getInt(4), rs.getString(5));
                    categorySet.add(category);
                }
                return categorySet;
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