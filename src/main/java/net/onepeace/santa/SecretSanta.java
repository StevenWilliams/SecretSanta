package net.onepeace.santa;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public class SecretSanta extends JavaPlugin {
    public static String giveGiftMenu = "Secret Santa: Choose a gift";
    private Data data;
    @Override
    public void onEnable() {
        this.data = new Data(this);
    }
    public Data getData() {
        return this.data;
    }
    public void debug(String message) {
        if(this.getConfig().getBoolean("debug", false)) {
            getLogger().info("[DEBUG] " + message);
        }
    }
    public void distributeGifts() {


    }
}

//data: last round
//delay: milli
//new task that runs at (last round + delay), then changes last round.
//randomly gives players gifts chosen.

//onlogin: check if the player has any gifts (use big letters overlay?? boss bar?)
//TODO