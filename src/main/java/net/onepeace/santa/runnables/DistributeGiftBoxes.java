package net.onepeace.santa.runnables;

import net.onepeace.santa.Gift;
import net.onepeace.santa.SecretSanta;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class DistributeGiftBoxes extends BukkitRunnable {
    private final SecretSanta plugin;

    public DistributeGiftBoxes(SecretSanta plugin) {
        this.plugin = plugin;
    }

    public static void giveGiftBoxes(SecretSanta plugin, final Player player) {
        List<Gift> gifts = plugin.getData().getGiftsToClaim(player.getUniqueId());
        for (Gift gift : gifts) {
            if (!gift.giftBoxGiven()) {
                player.getInventory().addItem(gift.getGiftBoxItem());
                gift.setGiftBoxGiven();
                player.sendMessage(SecretSanta.msgPrefix + "You have received a gift!");
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                player.updateInventory();
            }
        }.runTaskLater(plugin, 1);
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            giveGiftBoxes(plugin, player);
        }
    }
}
