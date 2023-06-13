package me.frandma.pvpcore.user;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
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
        for (Map.Entry<UUID, User> set : userMap.entrySet()) {
            if (set.getKey() == uuid) return set.getValue();
        }
        return null;
    }
}
