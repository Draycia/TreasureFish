package net.draycia.treasurefish.commands;

import net.draycia.treasurefish.TreasureFish;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TreasureChanceCommand implements CommandExecutor {

    private TreasureFish main;

    public TreasureChanceCommand(TreasureFish main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player)sender;

        double chance = main.getTreasureMobChance(player);

        String message = main.getConfig().getString("TreasureMobSpawnChanceMessage")
                .replace("{chance}", Double.toString(chance));

        message = ChatColor.translateAlternateColorCodes('&', message);

        player.sendMessage(message);

        return true;
    }
}
