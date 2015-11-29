package net.onepeace.santa.listener;

import net.onepeace.santa.SecretSanta;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginReminder implements Listener {
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
