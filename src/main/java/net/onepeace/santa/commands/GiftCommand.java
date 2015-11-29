package net.onepeace.santa.commands;

import net.onepeace.santa.SecretSanta;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GiftCommand implements CommandExecutor {
    private SecretSanta plugin;

    public GiftCommand(SecretSanta plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String tag, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return false;
        }
        Player player = (Player) sender;
        Inventory inventory = Bukkit.createInventory(player, 9, SecretSanta.giveGiftMenu);
        player.openInventory(inventory);
        return true;
    }
}
