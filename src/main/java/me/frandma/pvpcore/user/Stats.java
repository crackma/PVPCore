package me.frandma.pvpcore.user;

import lombok.Getter;
import lombok.Setter;

public class Stats {

    @Getter
    @Setter
    private int kills;
    @Getter
    @Setter
    private int deaths;
    @Getter
    @Setter
    private int gems;

    public Stats(int kills, int deaths, int gems) {
        this.kills = kills;
        this.deaths = deaths;
        this.gems = gems;
    }
    public Stats() {
        this.kills = 0;
        this.deaths = 0;
        this.gems = 0;
    }

    public String toString() {
        return kills + ", " + deaths + ", " + gems;
    }
}
