package net.onepeace.santa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Gift {
    public static final String giftBoxName = ChatColor.GOLD + "Secret Santa Gift";
    public static final String idPrefix = ChatColor.WHITE + "" + ChatColor.ITALIC + "Gift ID: ";
    public static final String playerPrefix = ChatColor.WHITE + "" + ChatColor.ITALIC + "Player ID: ";
    private final SecretSanta plugin;
    private final String giftID;
    private final UUID playerGivenTo;
    public Gift(SecretSanta plugin, UUID uuid, String giftID) {
        this.plugin = plugin;
        this.playerGivenTo = uuid;
        this.giftID = giftID;
    }

    public static Gift getGift(SecretSanta plugin, ItemStack item) {
        if (!isGiftBox(plugin, item)) return null;

        String id = item.getItemMeta().getLore().get(0).substring(idPrefix.length());
        String uuidShortID = item.getItemMeta().getLore().get(1).substring(playerPrefix.length());

        String uuid = plugin.getData().getUUID(uuidShortID);

        if (uuid == null) return null;

        if (plugin.getData().giftExistsToClaim(UUID.fromString(uuid), id)) {
            return new Gift(plugin, UUID.fromString(uuid), id);
        }
        return null;
    }

    public static boolean isGiftBox(SecretSanta plugin, ItemStack item) {
        if (item.getItemMeta().getDisplayName() == null || !item.getItemMeta().getDisplayName().equals(giftBoxName))
            return false;
        if (item.getItemMeta().getLore().get(0) == null || !item.getItemMeta().getLore().get(0).startsWith(idPrefix))
            return false;
        return !(item.getItemMeta().getLore().get(1) == null || !item.getItemMeta().getLore().get(1).startsWith(playerPrefix));
    }

    public UUID getGiftOwner() {
        return playerGivenTo;
    }

    public String getGiftID() {
        return giftID;
    }

    public ItemStack claim() {
        return plugin.getData().claimGift(playerGivenTo, giftID);
    }

    public ItemStack getGiftBoxItem() {
        ItemStack giftBox = new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());

        SkullMeta skullMeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
        skullMeta.setOwner(plugin.getConfig().getString("gift-box-player"));
        skullMeta.setDisplayName(giftBoxName);

        List<String> lore = new ArrayList<>();
        lore.add(idPrefix + giftID);

        String UUIDShortID = plugin.getData().getShortID(playerGivenTo);

        lore.add(playerPrefix + UUIDShortID);
        lore.add(ChatColor.GOLD + "Right click to open!");

        skullMeta.setLore(lore);

        giftBox.setItemMeta(skullMeta);

        return giftBox;
    }

    public void setGiftBoxGiven() {
        plugin.getData().setBoxGiven(playerGivenTo, giftID);
    }

    public boolean giftBoxGiven() {
        return plugin.getData().getBoxGiven(playerGivenTo, giftID);
    }

    //TODO: if gift is transferrable, add gift for player to lore.

}
