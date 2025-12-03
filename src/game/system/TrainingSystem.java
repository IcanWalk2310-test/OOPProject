package game.system;

import game.core.Player;
import game.core.Stat;

public class TrainingSystem {

    /**
     * Train a specific stat by amount.
     * Returns true if training was successful, false if invalid input.
     */
    public boolean train(Player player, String stat, int amount) {
        if (player == null || stat == null || amount <= 0) {
            return false;
        }

        switch(stat.toUpperCase()) {
            case "STR":
            case "STRENGTH":
                player.getStats().increaseStrength(amount);
                return true;

            case "AGI":
            case "AGILITY":
                player.getStats().increaseAgility(amount);
                return true;

            case "INT":
            case "INTELLIGENCE":
                player.getStats().increaseIntelligence(amount);
                return true;

            default:
                return false;
        }
    }

    /**
     * Get the player's current stats after any training.
     */
    public Stat getStats(Player player) {
        if (player == null) return null;
        return player.getStats();
    }

    /**
     * Convenience method: train strength.
     */
    public boolean trainStrength(Player player, int amount) {
        return train(player, "STRENGTH", amount);
    }

    /**
     * Convenience method: train agility.
     */
    public boolean trainAgility(Player player, int amount) {
        return train(player, "AGILITY", amount);
    }

    /**
     * Convenience method: train intelligence.
     */
    public boolean trainIntelligence(Player player, int amount) {
        return train(player, "INTELLIGENCE", amount);
    }
}