package me.crackma.pvpcore.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.crackma.pvpcore.PVPCorePlugin;
import org.bson.Document;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.mongodb.client.model.Filters.eq;

public class UserDatabase {
    private PVPCorePlugin plugin;
    private MongoCollection<Document> collection;
    public UserDatabase(PVPCorePlugin plugin, MongoDatabase mongoDatabase) {
        this.plugin = plugin;
        MongoCollection<Document> collection = mongoDatabase.getCollection(plugin.getConfig().getString("user_collection"));
        this.collection = collection;
    }
    public CompletableFuture<Void> insert(UUID uuid) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            if (collection.find(eq("_id", uuid.toString())).first() != null) return null;
            Document document = new Document();
            document.put("_id", uuid.toString());
            document.put("kills", 0);
            document.put("deaths", 0);
            document.put("streak", 0);
            document.put("gems", 0);
            document.put("kitCooldowns", null);
            collection.insertOne(document);
            return null;
        });
        future.exceptionally(exception -> {
            exception.printStackTrace();
            return null;
        });
        return future;
    }
    public CompletableFuture<Boolean> exists(UUID uuid) {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> collection.find(eq("_id", uuid.toString())).first() != null);
        future.exceptionally(exception -> {
            exception.printStackTrace();
            return null;
        });
        return future;
    }
    public CompletableFuture<Stats> get(UUID uuid) {
        CompletableFuture<Stats> future = CompletableFuture.supplyAsync(() -> {
            Document document = collection.find(eq("_id", uuid.toString())).first();
            if (document == null) return null;
            return new Stats(
                    document.getInteger("kills"),
                    document.getInteger("deaths"),
                    document.getInteger("streak"),
                    document.getInteger("gems"),
                    document.getString("kitCooldowns"),
                    plugin
            );
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

