package me.crackma.pvpcore.kits;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.shop.Category;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class KitDatabase {

    private static MongoCollection<Document> collection;

    public KitDatabase(PVPCorePlugin plugin, MongoDatabase mongoDatabase) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(plugin.getConfig().getString("kit_collection"));
        this.collection = collection;
    }

    public static CompletableFuture<Void> insertKit(Kit kit) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            Document document = new Document();
            document.put("_id", kit.getName());
            document.put("cooldown", kit.getCooldown());
            document.put("inventorySlot", kit.getInventorySlot());
            document.put("displayItem", kit.getDisplayItem());
            document.put("items", kit.getItemsAsBase64());
            collection.insertOne(document);
            return null;
        });
        future.exceptionally(exception -> {
            exception.printStackTrace();
            return null;
        });
        return future;
    }

    protected static CompletableFuture<Set<Kit>> fetchKits() {
        CompletableFuture<Set<Kit>> future = CompletableFuture.supplyAsync(() -> {
            FindIterable<Document> documents = collection.find();
            Set<Kit> categorySet = new HashSet<>();
            documents.forEach(document -> {
                Kit kit = new Kit(
                        document.getString("_id"),
                        document.getInteger("cooldown"),
                        document.getInteger("inventorySlot"),
                        document.getString("displayItem"),
                        document.getString("items")
                );
                categorySet.add(kit);
            });
            return categorySet;
        });
        future.exceptionally(exception -> {
            exception.printStackTrace();
            return null;
        });
        return future;
    }

    public static CompletableFuture<Void> deleteKit(String kitName) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            collection.deleteOne(Filters.eq("_id", kitName));
            return null;
        });
        future.exceptionally(exception -> {
            exception.printStackTrace();
            return null;
        });
        return future;
    }
}