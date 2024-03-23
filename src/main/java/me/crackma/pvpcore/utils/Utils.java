package me.crackma.pvpcore.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class Utils {
    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4");
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
        String date = "";
        long days = duration.toDays();
        if (days > 0) date = date + days + "d ";
        long hours = duration.toHours() % 24;
        if (hours > 0) date = date + hours + "h ";
        long minutes = duration.toMinutes() % 60;
        if (minutes > 0) date = date + minutes + "m ";
        long seconds = duration.getSeconds() % 60;
        if (seconds > 0) date = date + seconds + "s";
        return date;
    }
    public static ItemStack itemStackCreator(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
