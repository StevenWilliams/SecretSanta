package net.onepeace.santa;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class Data {
    private final SecretSanta plugin;
    private FileConfiguration data;
    private File file;

    public Data(SecretSanta plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        if (file == null) {
            file = new File(plugin.getDataFolder(), "data.yml");
            if (!file.exists()) try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        data = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        if (data == null || file == null) {
            return;
        }
        try {
            data.save(file);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + file, ex);
        }
    }

    public long getLastDraw() {
        return data.getLong("lastDraw", plugin.getConfig().getLong("first-last-draw"));
    }

    public void setLastDraw(Long time) {
        data.set("lastDraw", System.currentTimeMillis());
        this.save();
    }

    public long getNextDraw() {
        return getLastDraw() + plugin.getConfig().getLong("interval");
    }

    public long getTimeUntil() {
        return getNextDraw() - System.currentTimeMillis();
    }

    public String getTimeUntilFormatted() {
        return SecretSanta.formatTime(getTimeUntil());
    }

    public void setLastDraw() {
        setLastDraw(System.currentTimeMillis());
    }

    public int getCurrentRound() {
        if (data.getInt("current-round", -1) == -1) {
            data.set("current-round", 1);
        }
        this.save();
        return data.getInt("current-round");
    }

    public List<Gift> getGiftsToClaim(UUID uuid) {
        List<Gift> gifts = new ArrayList<>();

        if (data.getConfigurationSection("gifts." + uuid.toString()) == null) return gifts;
        Set<String> giftIDs = data.getConfigurationSection("gifts." + uuid.toString()).getKeys(false);

        for (String giftID : giftIDs) {
            Gift gift = new Gift(plugin, uuid, giftID);
            gifts.add(gift);
        }
        return gifts;
    }

    public boolean giftExistsToClaim(UUID uuid, String giftID) {
        return (data.getConfigurationSection("gifts." + uuid.toString() + "." + giftID) != null);
    }

    public ItemStack claimGift(UUID uuid, String giftID) {
        ConfigurationSection section = data.getConfigurationSection("gifts." + uuid.toString() + "." + giftID);
        ItemStack item = section.getItemStack("item");

        section.getParent().set(giftID, null);
        this.save();
        return item;
    }

    public void setBoxGiven(UUID uuid, String giftID) {
        data.set("gifts." + uuid.toString() + "." + giftID + ".given", true);
        this.save();
    }

    public boolean getBoxGiven(UUID uuid, String giftID) {
        return data.getBoolean("gifts." + uuid.toString() + "." + giftID + ".given", false);
    }

    public void addGiftToClaim(UUID uuid, int round, ItemStack item) {
        if (data.getConfigurationSection("gifts." + uuid.toString()) == null)
            data.createSection("gifts." + uuid.toString());
        int giftID = data.getConfigurationSection("gifts." + uuid.toString()).getKeys(false).size(); //if no gifts yet, should be 0
        data.set("gifts." + uuid.toString() + "." + giftID + ".round", round);
        data.set("gifts." + uuid.toString() + "." + giftID + ".item", item);
        this.save();
    }

    public String getShortID(UUID uuid) {
        if (data.getConfigurationSection("uuids") == null) data.createSection("uuids");
        Set<String> shortKeys = data.getConfigurationSection("uuids").getKeys(false);
        for (String shortKey : shortKeys) {
            if (data.getString("uuids." + shortKey).equals(uuid.toString())) return shortKey;
        }
        return setShortID(uuid);
    }

    public String getUUID(String shortID) {
        if (data.getString("uuids." + shortID) != null) {
            return data.getString("uuids." + shortID);
        }
        return null;
    }

    public String setShortID(UUID uuid) {
        if (data.getConfigurationSection("uuids") == null) data.createSection("uuids");
        int shortID = data.getConfigurationSection("uuids").getKeys(false).size();
        data.set("uuids." + String.valueOf(shortID), uuid.toString());
        this.save();
        return String.valueOf(shortID);
    }

    public void addGiftToRound(Player player, ItemStack item) {
        data.set("round." + player.getUniqueId() + ".items." + getGiftsInRound(player.getUniqueId()).size(), item);
        this.save();
    }

    public HashMap<UUID, Integer> getPlayersInRound() {
        HashMap<UUID, Integer> playersWithCount = new HashMap<>();
        if (data.getConfigurationSection("round") == null) return playersWithCount;
        Set<String> playersInRound = data.getConfigurationSection("round").getKeys(false);
        for (String playerUUID : playersInRound) {
            int playerCount = getGiftsInRound(UUID.fromString(playerUUID)).size();
            playersWithCount.put(UUID.fromString(playerUUID), playerCount);
        }
        return playersWithCount;
    }

    public List<ItemStack> getGiftsInRound(UUID uuid) {
        List<ItemStack> items = new ArrayList<>();
        if (data.getConfigurationSection("round." + uuid.toString() + ".items") != null) {
            Set<String> itemKeys = data.getConfigurationSection("round." + uuid.toString() + ".items").getKeys(false);
            for (String itemKey : itemKeys) {
                ItemStack item = data.getItemStack("round." + uuid.toString() + ".items." + itemKey);
                items.add(item);
            }
        }
        return items;
    }

    public int getGiftsInRoundAmount(UUID uuid) {
        if (data.getConfigurationSection("round." + uuid.toString() + ".items") != null) {
            return data.getConfigurationSection("round." + uuid.toString() + ".items").getKeys(false).size();
        }
        return 0;
    }

    public List<ItemStack> getGiftsInRound() {
        List<ItemStack> items = new ArrayList<>();
        if (data.getConfigurationSection("round") == null) return items;
        Set<String> playersUUIDs = data.getConfigurationSection("round").getKeys(false);
        for (String playerUUID : playersUUIDs) {
            List<ItemStack> playersGifts = getGiftsInRound(UUID.fromString(playerUUID));
            items.addAll(playersGifts);
        }
        return items;
    }


    public int setNextRound() {
        int nextround = getCurrentRound() + 1;
        data.set("current-round", nextround);
        data.set("round", null);
        this.save();
        return nextround;

    }

}
