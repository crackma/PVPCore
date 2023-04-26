package me.frandma.pvpcore.user;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class UserManager {

    @Getter
    private Set<User> userList = new HashSet<>();

    public void addUser(UUID uuid, Stats stats) {
        userList.add(new User(uuid, stats));
    }

    public boolean exists(UUID uuid) {
        return userList.contains(uuid);
    }

    public User getUser(UUID uuid) {
        for (User user : userList) {
            if (user.getUniqueId() == uuid) return user;
        }
        return null;
    }
}
