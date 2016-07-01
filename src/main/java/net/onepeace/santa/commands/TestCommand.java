package net.onepeace.santa.commands;

import net.onepeace.santa.SecretSanta;
import net.onepeace.santa.runnables.DistributeGifts;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TestCommand implements CommandExecutor {
    private final SecretSanta plugin;

    public TestCommand(SecretSanta plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String tag, String[] args) {
        if (!sender.hasPermission("secretsanta.admin")) return false;
        if (args.length < 1) {
            sender.sendMessage("distribute");
            sender.sendMessage("timeuntil");
            sender.sendMessage("player [player]");
            return true;
        }
        switch (args[0]) {
            case "distribute":
                new DistributeGifts(plugin).run();
                sender.sendMessage("distributed gifts");
                break;
            case "timeuntil":
                sender.sendMessage(String.valueOf(plugin.getData().getTimeUntil()));
                sender.sendMessage(plugin.getData().getTimeUntilFormatted());
                break;
            case "player":
                if (args.length < 2) sender.sendMessage("Please specifiy a player!");
                String playerName = args[1];
                OfflinePlayer player = plugin.getServer().getOfflinePlayer(playerName);
                sender.sendMessage("Player has given " + plugin.getData().getGiftsInRoundAmount(player.getUniqueId()) + "gifts");
        }
        return true;
    }
}
