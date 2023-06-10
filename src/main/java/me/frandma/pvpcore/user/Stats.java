package me.frandma.pvpcore.user;

import lombok.Getter;
import lombok.Setter;

public class Stats {

    @Getter
    @Setter
    private int kills, deaths, streak, gems;

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
