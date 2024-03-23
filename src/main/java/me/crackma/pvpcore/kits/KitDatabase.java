package me.crackma.pvpcore.kits;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.crackma.pvpcore.PVPCorePlugin;
import org.bson.Document;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class KitDatabase {
  private PVPCorePlugin plugin;
  private static MongoCollection<Document> collection;
  public KitDatabase(PVPCorePlugin plugin, MongoDatabase mongoDatabase) {
    this.plugin = plugin;
    collection = mongoDatabase.getCollection(plugin.getConfig().getString("mongodb.kit_collection"));
  }
  public CompletableFuture<Void> insertKit(Kit kit) {
    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
      Document document = new Document();
      document.put("_id", kit.getName());
      document.put("cooldown", kit.getCooldown());
      document.put("inventorySlot", kit.getInventorySlot());
      document.put("displayItem", kit.getDisplayItem());
      document.put("items", plugin.getKitManager().serialize(kit.getItems()));
      collection.insertOne(document);
      return null;
    });
    future.exceptionally(exception -> {
      exception.printStackTrace();
      return null;
    });
    return future;
  }
  protected CompletableFuture<Set<Kit>> fetchKits() {
    CompletableFuture<Set<Kit>> future = CompletableFuture.supplyAsync(() -> {
      FindIterable<Document> documents = collection.find();
      Set<Kit> categorySet = new HashSet<>();
      documents.forEach(document -> {
        Kit kit = new Kit(
            document.getString("_id"),
            document.getInteger("cooldown"),
            document.getInteger("inventorySlot"),
            Material.valueOf(document.getString("displayItem")),
            plugin.getKitManager().deserialize(document.getString("items"))
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
  public CompletableFuture<Void> deleteKit(String kitName) {
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