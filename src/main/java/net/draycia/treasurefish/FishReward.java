package net.draycia.treasurefish;

import org.bukkit.Material;

public class FishReward {
    private Material material;
    private int minReward;
    private int maxReward;
    private double chance;

    public FishReward(Material material, int minReward, int maxReward, double chance) {
        this.material = material;
        this.minReward = minReward;
        this.maxReward = maxReward;
        this.chance = chance;
    }

    public Material getMaterial() {
        return material;
    }

    public int getMinReward() {
        return minReward;
    }

    public int getMaxReward() {
        return maxReward;
    }

    public double getChance() {
        return chance;
    }

    @Override
    public String toString() {
        return "FishReward{" +
                "material=" + material +
                ", minReward=" + minReward +
                ", maxReward=" + maxReward +
                ", chance=" + chance +
                '}';
    }
}
