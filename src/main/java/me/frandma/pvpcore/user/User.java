package me.frandma.pvpcore.user;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class User {

    @Getter
    private UUID uniqueId;

    @Getter
    @Setter
    private Stats stats;

    public User(UUID uniqueId, Stats stats) {
        this.uniqueId = uniqueId;
        this.stats = stats;
    }
}
