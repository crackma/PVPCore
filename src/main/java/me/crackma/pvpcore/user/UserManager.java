package me.crackma.pvpcore.user;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {
    @Getter
    private Map<UUID, User> userMap = new HashMap<>();
    public void addUser(User user) {
        userMap.put(user.getUniqueId(), user);
    }
    public boolean exists(UUID uuid) {
        return userMap.containsKey(uuid);
    }
    public User getUser(UUID uuid) {
        return userMap.get(uuid);
    }
}
