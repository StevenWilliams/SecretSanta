package net.onepeace.santa.runnables;

import net.onepeace.santa.SecretSanta;
import org.bukkit.scheduler.BukkitRunnable;

public class DistributeGiftsTimer extends BukkitRunnable {
    private final SecretSanta plugin;

    public DistributeGiftsTimer(SecretSanta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() >= plugin.getData().getNextDraw()) {
            new DistributeGifts(plugin).run();
            plugin.getData().setLastDraw(plugin.getData().getNextDraw());
        }
    }
}
