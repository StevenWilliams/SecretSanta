package net.onepeace.santa.listener;

import net.onepeace.santa.Gift;
import net.onepeace.santa.SecretSanta;
import net.onepeace.santa.runnables.DistributeGiftBoxes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class GiftBox implements Listener {
    private final SecretSanta plugin;

    public GiftBox(SecretSanta plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        DistributeGiftBoxes.giveGiftBoxes(plugin, e.getPlayer());
    }

    @EventHandler
    public void use(PlayerInteractEvent e) {
        if (e.getPlayer().getItemInHand() == null || e.getItem() == null) return;
        final Player player = e.getPlayer();
        if (Gift.getGift(plugin, player.getItemInHand()) != null) {
            Gift gift = Gift.getGift(plugin, player.getItemInHand());
            ItemStack item = gift.claim();

            //player.getInventory().remove(e.getItem());


            String name = item.getItemMeta().getDisplayName() == null ? item.getType().toString().toLowerCase() : item.getItemMeta().getDisplayName();
            player.sendMessage(SecretSanta.msgPrefix + "You have received a gift of " + item.getAmount() + " " + name);
            player.setItemInHand(item);

            e.setCancelled(true);

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.updateInventory();
                }
            }.runTaskLater(plugin, 1);
        }
    }
}
