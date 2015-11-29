package net.onepeace.santa.runnables;

import net.onepeace.santa.Gift;
import net.onepeace.santa.SecretSanta;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CleanGiftBoxes extends BukkitRunnable {
    private final SecretSanta plugin;

    public CleanGiftBoxes(SecretSanta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            ItemStack[] items = player.getInventory().getContents();
            for (ItemStack item : items) {
                if (item != null) {
                    if (Gift.isGiftBox(plugin, item) && Gift.getGift(plugin, item) == null) {
                        player.sendMessage(SecretSanta.msgPrefix + ChatColor.RED + "A giftbox has been removed as it is no longer valid!");
                        player.getInventory().remove(item);
                    }
                }
            }
        }
    }
}
