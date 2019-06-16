package net.draycia.treasurefish.commands;

import net.draycia.treasurefish.TreasureFish;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TreasureFishCommand implements CommandExecutor {

    private TreasureFish main;

    public TreasureFishCommand(TreasureFish main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("treasurefish.reload")) {
                main.reloadConfig();
                main.reloadTreasures();

                String message = main.getConfig().getString("PluginReloaded");
                message = ChatColor.translateAlternateColorCodes('&', message);

                sender.sendMessage(message);
            }
        }

        return true;
    }
}
