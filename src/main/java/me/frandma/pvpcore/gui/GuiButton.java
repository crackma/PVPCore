package me.frandma.pvpcore.gui;

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
    private Consumer<InventoryClickEvent> eventConsumer;

    public GuiButton creator(Function<Player, ItemStack> itemStackCreator) {
        this.itemStackCreator = itemStackCreator;
        return this;
    }

    public GuiButton consumer(Consumer<InventoryClickEvent> eventConsumer) {
        this.eventConsumer = eventConsumer;
        return this;
    }

}
