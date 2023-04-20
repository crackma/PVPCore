package me.frandma.pvpcore.user;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class User {

    @Getter
    private UUID uuid;

    @Getter
    @Setter
    private Stats stats;

    public User(UUID uuid, Stats stats) {
        this.uuid = uuid;
        this.stats = stats;
    }
}
