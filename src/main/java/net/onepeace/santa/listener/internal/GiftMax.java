package net.onepeace.santa.listener.internal;

import net.onepeace.santa.SecretSanta;
import net.onepeace.santa.events.GiftGiveEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class GiftMax implements Listener {
    private final SecretSanta plugin;

    public GiftMax(SecretSanta plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void giftMax(GiftGiveEvent event) {
        int giftMax = plugin.getConfig().getInt("giftMax");
        if(plugin.getData().getGiftsInRoundAmount(event.getGifter().getUniqueId()) >= giftMax) {
            event.getGifter().sendMessage(SecretSanta.msgPrefix + ChatColor.RED + "You can only give up to " + giftMax + " gifts!");
            event.setCancelled(true);
        }
    }
}
