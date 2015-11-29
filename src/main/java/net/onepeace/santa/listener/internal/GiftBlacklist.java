package net.onepeace.santa.listener.internal;

import net.onepeace.santa.SecretSanta;
import net.onepeace.santa.events.GiftGiveEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class GiftBlacklist implements Listener {
    private final JavaPlugin plugin;

    public GiftBlacklist(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void materialBlackList(GiftGiveEvent event) {
        List<String> blacklistMaterials = plugin.getConfig().getStringList("blacklist-materials");
        Player player = event.getGifter();
        ItemStack item = event.getItem();
        if (blacklistMaterials.contains(item.getType().toString())) {
            player.sendMessage(SecretSanta.msgPrefix + ChatColor.RED + "You cannot give gifts of " + item.getType().toString());
            event.setCancelled(true);
        }
    }
}
