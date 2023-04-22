package me.frandma.pvpcore.user;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class UserManager {

    private List<User> userList = new ArrayList<>();

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

    public List<User> getUserList() {
        return userList;
    }
}
