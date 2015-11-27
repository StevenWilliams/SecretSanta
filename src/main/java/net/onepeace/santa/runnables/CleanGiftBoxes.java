package net.onepeace.santa.runnables;

import net.onepeace.santa.Gift;
import net.onepeace.santa.SecretSanta;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CleanGiftBoxes implements Runnable {
    private SecretSanta plugin;
    public CleanGiftBoxes(SecretSanta plugin) {
        this.plugin = plugin;
    }
    @Override
    public void run() {
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            ItemStack[] items = player.getInventory().getContents();
            for(ItemStack item : items) {
                if (Gift.isGiftBox(item) && Gift.getGift(plugin, player, item) == null) {
                    player.getInventory().remove(item);
                }
            }
        }
    }
}
