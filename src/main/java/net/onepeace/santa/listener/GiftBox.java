package net.onepeace.santa.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class GiftBox implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        //TODO: check if player has not received any gifts, otherwise give gift.
    }
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        ItemStack[] items = e.getPlayer().getInventory().getContents();

    }
}
