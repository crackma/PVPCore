package me.crackma.pvpcore.gui;

import java.util.function.Consumer;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

@Getter
public class GuiButton {
  private ItemStack itemStack;
  private Consumer<InventoryClickEvent> leftEventConsumer;
  private Consumer<InventoryClickEvent> rightEventConsumer;
  public GuiButton creator(ItemStack itemStack) {
    this.itemStack = itemStack;
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
