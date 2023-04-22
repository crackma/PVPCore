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
    private int streak;
    @Getter
    @Setter
    private int gems;

    public Stats(int kills, int deaths, int streak, int gems) {
        this.kills = kills;
        this.deaths = deaths;
        this.streak = streak;
        this.gems = gems;
    }

    public String toString() {
        return kills + ", " + deaths + ", " + streak + ", " + gems;
    }
}
