package me.frandma.pvpcore.kits;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class KitManager {

    @Getter
    List<Kit> kitList = new ArrayList<>();

    public void addKit(Kit kit) {
        kitList.add(kit);
    }
}
