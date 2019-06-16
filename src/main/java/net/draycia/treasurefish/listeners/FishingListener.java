package net.draycia.treasurefish.listeners;

import net.draycia.treasurefish.TreasureFish;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class FishingListener implements Listener {

    private TreasureFish main;

    public FishingListener(TreasureFish main) {
        this.main = main;
    }

    @EventHandler(ignoreCancelled = true)
    public void onFish(PlayerFishEvent event) {
        if (event.getCaught() == null) {
            return;
        }

        Player player = event.getPlayer();

        double chance = main.getTreasureMobChance(player);

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100);

        if (randomInt < 100 - chance) {
            return;
        }

        // Spawn the mob where the fishing hook landed at
        Entity treasureMob = player.getWorld().spawnEntity(event.getHook().getLocation(), EntityType.SILVERFISH);
        main.getTreasureMobs().add(treasureMob.getUniqueId());

        Vector mobVector = treasureMob.getLocation().toVector();
        Vector playerVector = player.getLocation().toVector();

        playerVector.add(new Vector(0, 1f, 0));

        Vector direction = playerVector.subtract(mobVector).normalize();

        direction.add(new Vector(0, 0.5f, 0));

        treasureMob.setVelocity(direction);

        int duration = main.getConfig().getInt("TreasureMobLifeDuration");

        String message = main.getConfig().getString("TreasureMobSpawnMessage")
                .replace("{duration}", Integer.toString(duration))
                .replace("\\n", "\n");

        message = ChatColor.translateAlternateColorCodes('&', message);

        for (Entity entity : treasureMob.getNearbyEntities(10, 10, 10)) {
            if (entity instanceof Player) {
                entity.sendMessage(message);
            }
        }

        int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
            if (main.getTreasureMobs().contains(treasureMob.getUniqueId())) {
                main.getTreasureMobs().remove(treasureMob.getUniqueId());

                if (treasureMob instanceof LivingEntity) {
                    ((LivingEntity)treasureMob).setHealth(0);
                }

                for (Entity entity : treasureMob.getNearbyEntities(20, 20, 20)) {
                    if (entity instanceof Player) {
                        String dieMsg = main.getConfig().getString("TreasureMobDespawnMessage");
                        dieMsg = ChatColor.translateAlternateColorCodes('&', dieMsg);

                        entity.sendMessage(dieMsg);
                    }
                }
            }

        }, duration * 20);

        main.getTaskIds().add(taskId);
    }
}
