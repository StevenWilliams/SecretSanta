package net.onepeace.santa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class Gift {
    private SecretSanta plugin;
    private String giftID;

    public Player getPlayer() {
        return player;
    }

    public String getGiftID() {
        return giftID;
    }

    private Player player;

    public static String giftBoxName = ChatColor.GOLD + "Secret Santa Gift";
    public static String lorePrefix = ChatColor.GOLD + "Gift id: ";

    public Gift(SecretSanta plugin, Player player, String giftID) {
        this.plugin = plugin;
        this.player = player;
        this.giftID = giftID;
    }
    public void claim() {
        plugin.getData().claimGift(player, giftID);
    }
    public ItemStack getGiftBoxItem() {
        ItemStack giftBox = new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());

        SkullMeta skullMeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
        skullMeta.setOwner(plugin.getConfig().getString("gift-box-player"));
        skullMeta.setDisplayName(giftBoxName);

        List<String> lore = new ArrayList<String>();
        lore.add(lorePrefix + giftID);
        skullMeta.setLore(lore);

        giftBox.setItemMeta(skullMeta);

        return giftBox;
    }

    public static Gift getGift(SecretSanta plugin, Player player, ItemStack item) {
        if (!item.getItemMeta().getDisplayName().equals(giftBoxName)) return null;
        if (item.getItemMeta().getLore().get(0) == null) return null;

        String id = item.getItemMeta().getLore().get(0).substring(lorePrefix.length());
        plugin.debug("getGIFT ID: " + id);

        if(plugin.getData().giftExistsToClaim(player, id)) {
            Gift gift = new Gift(plugin, player, id);
            return gift;
        }
        return null;
    }



    //TODO: if gift is transferrable, add gift for player to lore.


    public static boolean isGiftBox(ItemStack item) {
        return item.getItemMeta().getDisplayName().equals(giftBoxName);
    }
}
