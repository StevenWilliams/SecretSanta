package net.onepeace.santa.listener;

import net.onepeace.santa.SecretSanta;
import net.onepeace.santa.events.GiftGiveEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GiftMenuClose implements Listener {
    private final SecretSanta plugin;

    public GiftMenuClose(SecretSanta plugin) {
        this.plugin = plugin;
    }

    private static boolean inventoryIsEmpty(Inventory inv) {
        ItemStack[] contents = inv.getContents();
        for (ItemStack item : contents) {
            if (item != null)
                return false;
        }
        return true;
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        final Inventory inv = e.getInventory();
        if (inv.getName().startsWith(SecretSanta.giveGiftMenu)) {
            if (inventoryIsEmpty(inv)) player.sendMessage(SecretSanta.msgPrefix + "No items gifted!");
            ItemStack[] contents = inv.getContents();
            for (ItemStack content : contents) {
                if (content != null) {
                    GiftGiveEvent giftGiveEvent = new GiftGiveEvent(content, player);
                    Bukkit.getServer().getPluginManager().callEvent(giftGiveEvent);
                    if (!giftGiveEvent.isCancelled()) {
                        String name = content.getItemMeta().getDisplayName() == null ? content.getType().toString().toLowerCase() : content.getItemMeta().getDisplayName();
                        plugin.getData().addGiftToRound(player, content);
                        player.sendMessage(SecretSanta.msgPrefix + "Thanks for gifting " + name);
                    } else {
                        player.getInventory().addItem(content);
                    }
                }
            }
            e.getPlayer().sendMessage(SecretSanta.msgPrefix + "Gifts will be given out in " + plugin.getData().getTimeUntilFormatted());
        }
    }
}
