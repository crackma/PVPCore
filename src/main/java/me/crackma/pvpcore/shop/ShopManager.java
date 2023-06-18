package me.crackma.pvpcore.shop;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.crackma.pvpcore.PVPCorePlugin;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class ShopManager {

    @Getter
    private Set<Category> categorySet = new HashSet<>();

    public void init() {
        PVPCorePlugin.getShopDatabase().fetchCategories().thenAccept(categories -> {
            categorySet = categories;
        });
    }

    public void addCategory(Category category) {
        categorySet.add(category);
    }

    public Category getCategory(String name) {
        name = name.toLowerCase();
        for (Category category : categorySet) {
            String categoryName = category.getName().toLowerCase();
            if (categoryName.equals(name)) return category;
        }
        return null;
    }

    public boolean exists(Category category) {
        return categorySet.contains(category);
    }

    public void deleteCategory(Category category) {
        categorySet.remove(category);
    }
}
