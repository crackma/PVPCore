package me.crackma.pvpcore.user;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

public class UserDeathEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();
	@Getter
	private final User victim;
	@Getter
	private final User attacker;
	public UserDeathEvent(User victim, User attacker) {
		this.victim = victim;
		this.attacker = attacker;
	}
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
