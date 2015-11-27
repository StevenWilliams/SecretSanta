package net.onepeace.santa.runnables;

import net.onepeace.santa.SecretSanta;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DistributeGifts extends BukkitRunnable {
    private SecretSanta plugin;
    public DistributeGifts(SecretSanta plugin) {
        this.plugin = plugin;
    }
    @Override
    public void run() {
        List<ItemStack> items = plugin.getData().getGiftsInRound();
        Collections.shuffle(items);

        HashMap<Player, Integer> playersInRound = plugin.getData().getPlayersInRound();
        Iterator it = playersInRound.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            Player player = (Player) pair.getKey();
            int giftsGiven = (int) pair.getValue();

            int currentRound = plugin.getData().getCurrentRound();
            for (int i = 0; i < giftsGiven; i++) {
                ItemStack gift = items.remove(0);
                plugin.getData().addGiftToClaim(player, currentRound, gift);
            }

            it.remove();
        }
        plugin.getData().setNextRound();
        //lastdraw is only set if in the timer.
    }
}
