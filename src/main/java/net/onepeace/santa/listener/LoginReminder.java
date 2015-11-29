package net.onepeace.santa.listener;

import net.onepeace.santa.SecretSanta;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.EventListener;

public class LoginReminder implements EventListener {
    private final SecretSanta plugin;

    public LoginReminder(SecretSanta plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void remind(PlayerJoinEvent e) {
        if (plugin.getConfig().getBoolean("remind-time-on-login")) {
            e.getPlayer().sendMessage(SecretSanta.msgPrefix + "Gifts will be given in " + plugin.getData().getTimeUntilFormatted());
        }
    }
}
