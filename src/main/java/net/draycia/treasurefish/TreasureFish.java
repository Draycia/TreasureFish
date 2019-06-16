package net.draycia.treasurefish;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.util.player.UserManager;
import net.draycia.treasurefish.commands.TreasureChanceCommand;
import net.draycia.treasurefish.commands.TreasureFishCommand;
import net.draycia.treasurefish.listeners.EntityListener;
import net.draycia.treasurefish.listeners.FishingListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public final class TreasureFish extends JavaPlugin {

    private final ArrayList<UUID> treasureMobs = new ArrayList<>();
    private final ArrayList<FishReward> fishRewards = new ArrayList<>();

    private final ArrayList<Integer> taskIds = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadTreasures();

        getCommand("treasurechance").setExecutor(new TreasureChanceCommand(this));
        getCommand("treasurefish").setExecutor(new TreasureFishCommand(this));

        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        getServer().getPluginManager().registerEvents(new FishingListener(this), this);
    }

    @Override
    public void onDisable() {
        getTreasureMobs().clear();
        getFishRewards().clear();

        // Stop all tasks in case plugin is disabled via plugman or reload
        for (Integer id : getTaskIds()) {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

    public void reloadTreasures() {
        getFishRewards().clear();

        ConfigurationSection treasureSection = getConfig().getConfigurationSection("TreasureDrops");

        if (treasureSection == null) {
            getLogger().warning("TreasureDrops section not found!");
            return;
        }

        for (String key : treasureSection.getKeys(false)) {
            Material material = Material.getMaterial(key.toUpperCase());

            if (material == null) {
                getLogger().warning("There's no material with the name [" + key + "]");
                continue;
            }

            ConfigurationSection section = treasureSection.getConfigurationSection(key);

            if (section == null) {
                getLogger().warning("There's no section for the key [" + key + "]");
                continue;
            }

            int minAmount = section.getInt("MinAmount");
            int maxAmount = section.getInt("MaxAmount");

            double chance = section.getDouble("Chance");

            getFishRewards().add(new FishReward(material, minAmount, maxAmount, chance));
        }
    }

    public double getTreasureMobChance(Player player) {
        // Check if the player's data is loaded in
        McMMOPlayer mmoPlayer = UserManager.getPlayer(player);

        if (mmoPlayer == null) {
            return 1;
        }

        int level = mmoPlayer.getSkillLevel(PrimarySkillType.FISHING);
        double chancePerLevel = getConfig().getDouble("PercentagePerLevel");

        return level * chancePerLevel;
    }

    public ArrayList<UUID> getTreasureMobs() {
        return treasureMobs;
    }

    public ArrayList<FishReward> getFishRewards() {
        return fishRewards;
    }

    public ArrayList<Integer> getTaskIds() {
        return taskIds;
    }
}
