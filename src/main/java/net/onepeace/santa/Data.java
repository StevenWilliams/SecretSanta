package net.onepeace.santa;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class Data {
    private FileConfiguration data;
    private SecretSanta plugin;
    public Data(SecretSanta plugin) {
        this.plugin = plugin;
        data = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "data.yml"));
    }
    public long getLastDraw() {
        return data.getLong("lastDraw", plugin.getConfig().getLong("first-last-draw"));
    }
    public long getNextDraw() {
        return getLastDraw() + plugin.getConfig().getLong("interval");
    }
    public void setLastDraw(Long time) {
        data.set("lastDraw", System.currentTimeMillis());
    }
    public void setLastDraw() {
        setLastDraw(System.currentTimeMillis());
    }
    public int getCurrentRound() {
        if(data.getInt("current-round", -1) == -1) {
            data.set("current-round", 1);
        }
        return data.getInt("current-round");
    }
    public List<Gift> getGiftsToClaim(Player player) {
        Set<String> giftIDs = data.getConfigurationSection("gifts." + player.getUniqueId()).getKeys(false);
        List<Gift> gifts = new ArrayList<>();
        for(String giftID : giftIDs) {
            Gift gift = new Gift(plugin, player, giftID);
            gifts.add(gift);
        }
        return gifts;
    }

    public boolean giftExistsToClaim(Player player, String giftID) {
        return (data.getConfigurationSection("gifts." + player.getUniqueId() + "." + giftID) != null);
    }

    public void claimGift(Player player, String giftID) {
        ConfigurationSection section = data.getConfigurationSection("gifts." + player.getUniqueId() + "." + giftID);
        ItemStack item = section.getItemStack("item");
        player.getInventory().addItem(item);
        section.getParent().set(giftID, null);
    }

    public void addGiftToClaim(Player player, int round, ItemStack item) {
        int giftID = data.getConfigurationSection("gifts." + player.getUniqueId()).getKeys(false).size(); //if no gifts yet, should be 0
        data.set("gifts." + player.getUniqueId() + "." + giftID + ".round", round);
        data.set("gifts." + player.getUniqueId() + "." + giftID + ".item", item);
    }

    public void addGiftToRound(Player player, ItemStack item) {
        data.set("round." + player.getUniqueId() + ".items." + getGiftsInRound(player.getUniqueId()), item);
    }

    public HashMap<Player, Integer> getPlayersInRound() {
        data.getConfigurationSection("round").getKeys(false);
    }

    public List<ItemStack> getGiftsInRound(UUID uuid) {
        List<ItemStack> items = new ArrayList<>();
        if(data.getConfigurationSection("round." + uuid.toString() + ".items") != null) {
            Set<String> itemKeys = data.getConfigurationSection("round." + uuid.toString() + ".items").getKeys(false);
            for(String itemKey : itemKeys) {
                ItemStack item = data.getItemStack("round." + uuid.toString() + ".items." + itemKey);
                items.add(item);
            }
        }
        return items;
    }
    public int getGiftsInRoundAmount(UUID uuid) {
        if(data.getConfigurationSection("round." + uuid.toString() + ".items") != null) {
            return data.getConfigurationSection("round." + uuid.toString() + ".items").getKeys(false).size();
        }
        return 0;
    }

    public List<ItemStack> getGiftsInRound() {
        List<ItemStack> items = new ArrayList<>();
        Set<String> playersUUIDs = data.getConfigurationSection("round").getKeys(false);
        for(String playerUUID : playersUUIDs) {
            List<ItemStack> playersGifts = getGiftsInRound(UUID.fromString(playerUUID));
            items.addAll(playersGifts);
        }
        return items;
    }


    public int setNextRound() {
        int nextround = getCurrentRound() + 1;
        data.set("current-round", nextround);
        return nextround;
    }

}
