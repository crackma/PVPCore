package me.crackma.pvpcore.gui;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

public class GuiButton {
    @Getter
    private Function<Player, ItemStack> itemStackCreator;
    @Getter
    private Consumer<InventoryClickEvent> leftEventConsumer;
    @Getter
    private Consumer<InventoryClickEvent> rightEventConsumer;
    public GuiButton creator(Function<Player, ItemStack> itemStackCreator) {
        this.itemStackCreator = itemStackCreator;
        return this;
    }
    public GuiButton leftConsumer(Consumer<InventoryClickEvent> eventConsumer) {
        this.leftEventConsumer = eventConsumer;
        return this;
    }
    public GuiButton rightConsumer(Consumer<InventoryClickEvent> eventConsumer) {
        this.rightEventConsumer = eventConsumer;
        return this;
    }
}
