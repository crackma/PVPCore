package me.crackma.pvpcore.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(time);
    }
}
