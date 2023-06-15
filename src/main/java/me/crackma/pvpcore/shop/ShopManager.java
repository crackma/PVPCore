package me.crackma.pvpcore.shop;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class ShopManager {

    @Getter
    private Set<Category> categorySet = new HashSet<>();

    public void init() {
        ShopDatabase.fetchCategories().thenAccept(categories -> {
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

    public void deleteCategory(Category category) {
        categorySet.remove(category);
    }
}
