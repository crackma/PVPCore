package me.crackma.pvpcore.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Utils {
    public List<String> getMaterialList(String contains) {
        List<String> materialList = new ArrayList<>();
        for (Material material : Material.values()) {
            String materialString = material.toString();
            if (!materialString.contains(contains)) continue;
            materialList.add(material.toString());
        }
        return materialList;
    }
    public String formatToDate(long time) {
        Duration duration = Duration.ofMillis(time);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
    }
}
