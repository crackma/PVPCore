package me.crackma.pvpcore.enchanting;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.crackma.pvpcore.user.User;

@Getter
public class UserEnchantEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();
	private final User user;
	private final Enchantment enchantment;
	private final int enchantmentPower;
	public UserEnchantEvent(User user, Enchantment enchantment, int enchantmentPower) {
		this.user = user;
		this.enchantment = enchantment;
		this.enchantmentPower = enchantmentPower;
	}
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
