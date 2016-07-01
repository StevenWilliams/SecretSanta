package net.onepeace.santa;

import net.onepeace.santa.commands.GiftCommand;
import net.onepeace.santa.commands.TestCommand;
import net.onepeace.santa.listener.GiftBox;
import net.onepeace.santa.listener.GiftMenuClose;
import net.onepeace.santa.listener.LoginReminder;
import net.onepeace.santa.listener.internal.GiftBlacklist;
import net.onepeace.santa.listener.internal.GiftMax;
import net.onepeace.santa.runnables.CleanGiftBoxes;
import net.onepeace.santa.runnables.DistributeGiftBoxes;
import net.onepeace.santa.runnables.DistributeGiftsTimer;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class SecretSanta extends JavaPlugin {
    public static String giveGiftMenu = "Secret Santa: Choose a gift";
    public static String msgPrefix;

    private Data data;

    public static String formatTime(Long millis) {
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;
        long day = (millis / (1000 * 60 * 60 * 24));
        return String.format("%dd %dh %dm %ds", day, hour, minute, second);
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        msgPrefix = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("msg-prefix"));
        giveGiftMenu = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("gui-title"));
        this.data = new Data(this);
        this.getServer().getPluginManager().registerEvents(new GiftBox(this), this);
        this.getServer().getPluginManager().registerEvents(new GiftMenuClose(this), this);
        this.getServer().getPluginManager().registerEvents(new GiftBlacklist(this), this);
        this.getServer().getPluginManager().registerEvents(new GiftMax(this), this);
        this.getServer().getPluginManager().registerEvents(new LoginReminder(this), this);
        this.getCommand("secretsanta").setExecutor(new GiftCommand(this));
        this.getCommand("secretsantatest").setExecutor(new TestCommand(this));
        new DistributeGiftsTimer(this).runTaskTimer(this, 0, 200);
        new DistributeGiftBoxes(this).runTaskTimer(this, 0, 200);
        new CleanGiftBoxes(this).runTaskTimer(this, 0, 20);
    }

    public Data getData() {
        return this.data;
    }

    public void debug(String message) {
        if (this.getConfig().getBoolean("debug", false)) {
            getLogger().info("[DEBUG] " + message);
        }
    }
}

//data: last round
//delay: milli
//new task that runs at (last round + delay), then changes last round.
//randomly gives players gifts chosen.

//onlogin: check if the player has any gifts (use big letters overlay?? boss bar?)
//TODO