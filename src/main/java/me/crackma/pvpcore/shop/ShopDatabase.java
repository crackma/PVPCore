package me.crackma.pvpcore.shop;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.crackma.pvpcore.PVPCorePlugin;
import org.bson.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ShopDatabase {
    private PVPCorePlugin plugin;
    private MongoCollection<Document> collection;
    public ShopDatabase(PVPCorePlugin plugin, MongoDatabase mongoDatabase) {
        this.plugin = plugin;
        MongoCollection<Document> collection = mongoDatabase.getCollection(plugin.getConfig().getString("shop_collection"));
        this.collection = collection;
    }
    public CompletableFuture<Void> insertCategory(Category category) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            Document document = new Document();
            document.put("_id", category.getName());
            document.put("description", category.getDescription());
            document.put("displayItem", category.getDisplayItem().toString());
            document.put("inventorySlot", category.getInventorySlot());
            document.put("items", category.getItemsAsString());
            if (plugin.getShopManager().exists(category)) {
                collection.replaceOne(Filters.eq(category.getName()), document);
            } else {
                collection.insertOne(document);
            }
            plugin.getShopManager().addCategory(category);
            return null;
        });
        future.exceptionally(exception -> {
            exception.printStackTrace();
            return null;
        });
        return future;
    }
    public CompletableFuture<Void> deleteCategory(String categoryName) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            collection.deleteOne(Filters.eq("_id", categoryName));
            return null;
        });
        future.exceptionally(exception -> {
            exception.printStackTrace();
            return null;
        });
        return future;
    }
    protected CompletableFuture<Set<Category>> fetchCategories() {
        CompletableFuture<Set<Category>> future = CompletableFuture.supplyAsync(() -> {
            FindIterable<Document> documents = collection.find();
            Set<Category> categorySet = new HashSet<>();
            documents.forEach(document -> {
                Category category = new Category(
                document.getString("_id"),
                document.getString("description"),
                document.getString("displayItem"),
                document.getInteger("inventorySlot"),
                document.getString("items")
                );
                categorySet.add(category);
            });
            return categorySet;
        });
        future.exceptionally(exception -> {
            exception.printStackTrace();
            return null;
        });
        return future;
    }
}
