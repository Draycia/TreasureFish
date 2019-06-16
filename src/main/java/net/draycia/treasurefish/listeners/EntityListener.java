package net.draycia.treasurefish.listeners;

import net.draycia.treasurefish.FishReward;
import net.draycia.treasurefish.TreasureFish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class EntityListener implements Listener {

    private TreasureFish main;

    public EntityListener(TreasureFish main) {
        this.main = main;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        if (main.getConfig().getBoolean("TreasureMobAttacksPlayers")) {
            return;
        }

        if (!(event.getTarget() instanceof Player)) {
            return;
        }

        if (main.getTreasureMobs().contains(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (!main.getTreasureMobs().contains(event.getEntity().getUniqueId())) {
            return;
        }

        Player killer = event.getEntity().getKiller();

        if (killer == null) {
            return;
        }

        ArrayList<ItemStack> itemsToDrop = new ArrayList<>();

        Random random = new Random();

        for (FishReward reward : main.getFishRewards()) {
            int randomInt = random.nextInt(100);

            if (randomInt < 100 - reward.getChance()) {
                continue;
            }

            int amount = random.nextInt((reward.getMaxReward() - reward.getMinReward()) + 1) + reward.getMinReward();

            itemsToDrop.add(new ItemStack(reward.getMaterial(), amount));
        }

        event.getDrops().clear();
        event.getDrops().addAll(itemsToDrop);

        main.getTreasureMobs().remove(event.getEntity().getUniqueId());
    }
}
