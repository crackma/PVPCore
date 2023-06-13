package me.frandma.pvpcore.user;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class UserManager {

    @Getter
    private Map<UUID, User> userMap = new HashMap<>();

    private List<User> userList = new ArrayList<>();

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
