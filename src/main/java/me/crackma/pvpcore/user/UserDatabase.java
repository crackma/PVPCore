package me.crackma.pvpcore.user;

import static com.mongodb.client.model.Filters.eq;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import me.crackma.pvpcore.PVPCorePlugin;

public class UserDatabase {
  private MongoCollection<Document> collection;
  public UserDatabase(PVPCorePlugin plugin, MongoDatabase mongoDatabase) {
    collection = mongoDatabase.getCollection(plugin.getConfig().getString("mongodb.user_collection"));
  }
  public User getOrCreate(UUID uuid) {
    if (collection.find(eq("_id", uuid.toString())).first() == null) {
      Document document = new Document();
      document.put("_id", uuid.toString());
      document.put("kills", 0);
      document.put("deaths", 0);
      document.put("streak", 0);
      document.put("bestStreak", 0);
      document.put("gems", 0);
      document.put("kitCooldowns", null);
      collection.insertOne(document);
      return new User(uuid, new Stats(0, 0, 0, 0, 0, ""));
    }
    Document document = collection.find(eq("_id", uuid.toString())).first();
    if (document == null) return null;
    //getordefault defeats my urge to randomly delete values from the database
    return new User(uuid, new Stats(
        (int)document.getOrDefault("kills", 0),
        (int)document.getOrDefault("deaths", 0),
        (int)document.getOrDefault("streak", 0),
        (int)document.getOrDefault("bestStreak", 0),
        (int)document.getOrDefault("gems", 0),
        (String)document.getOrDefault("kitCooldowns", null))
    );
  }
  public CompletableFuture<Stats> get(UUID uuid) {
    CompletableFuture<Stats> future = CompletableFuture.supplyAsync(() -> {
      Document document = collection.find(eq("_id", uuid.toString())).first();
      if (document == null) return null;
      return new Stats(
          document.getInteger("kills"),
          document.getInteger("deaths"),
          document.getInteger("streak"),
          document.getInteger("bestStreak"),
          document.getInteger("gems"),
          document.getString("kitCooldowns")
      );
    });
    future.exceptionally(exception -> {
      exception.printStackTrace();
      return null;
    });
    return future;
  }
  public CompletableFuture<FindIterable<Document>> getAllDocuments() {
    CompletableFuture<FindIterable<Document>> future = CompletableFuture.supplyAsync(() -> {
      FindIterable<Document> iterable = collection.find();
      return iterable;
    });
    future.exceptionally(exception -> {
      exception.printStackTrace();
      return null;
    });
    return future;
  }
  public CompletableFuture<Void> updateOne(User user) {
    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
      Document document = new Document();
      Stats stats = user.getStats();
      document.put("kills", stats.getKills());
      document.put("deaths", stats.getDeaths());
      document.put("streak", stats.getStreak());
      document.put("bestStreak", stats.getStreak());
      document.put("gems", stats.getGems());
      document.put("kitCooldowns", stats.getCooldownMapAsString());
      collection.replaceOne(eq(user.getUniqueId().toString()), document);
      return null;
    });
    future.exceptionally(exception -> {
      exception.printStackTrace();
      return null;
    });
    return future;
  }
}

