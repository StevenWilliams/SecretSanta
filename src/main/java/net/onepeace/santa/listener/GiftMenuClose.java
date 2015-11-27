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
    private SecretSanta plugin;

    public GiftMenuClose(SecretSanta plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        final Inventory inv = e.getInventory();
        //is a sell shop
        if(inv.getName().startsWith(SecretSanta.giveGiftMenu)) {
            if(inventoryIsEmpty(inv))  player.sendMessage("No items gifted!");
            ItemStack[] contents = inv.getContents();
            for(ItemStack content : contents) {
                if(content != null) {
                    GiftGiveEvent giftGiveEvent = new GiftGiveEvent(content, player);
                    Bukkit.getServer().getPluginManager().callEvent(giftGiveEvent);
                    if(!giftGiveEvent.isCancelled()) {
                        //TODO: save data

                        player.sendMessage("Thanks for gifting " + content.getItemMeta().getDisplayName());
                    } else {
                        player.getInventory().addItem(content);
                    }
                }
            }
        }
    }
    public static boolean inventoryIsEmpty(Inventory inv){
        ItemStack[] contents = inv.getContents();
        for(ItemStack item : contents)
        {
            if(item != null)
                return false;
        }
        return true;
    }
}
