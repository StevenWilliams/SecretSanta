package net.onepeace.santa.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class GiftGiveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private ItemStack item;
    private Player gifter;

    public GiftGiveEvent(ItemStack item, Player gifter) {
        this.item = item;
        this.gifter = gifter;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ItemStack getItem() {
        return item;
    }

    public Player getGifter() {
        return gifter;
    }
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}
