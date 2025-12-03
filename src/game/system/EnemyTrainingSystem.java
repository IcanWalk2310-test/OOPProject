package game.system;

import game.core.Enemy;
import game.core.Stat;
import game.data.EnemiesData;
import java.util.Random;

public class EnemyTrainingSystem {

    private final Random random;

    public EnemyTrainingSystem() {
        this.random = new Random();
    }

    /**
     * Train an enemy based on weighted probabilities.
     * Specialized stat has 60% chance, other stats have 20% each.
     * Uses the enemy's ORIGINAL specialization from EnemiesData.
     * Returns the stat that was trained.
     */
    public String trainEnemy(Enemy enemy, int amount) {
        if (enemy == null || amount <= 0) {
            return null;
        }
        
        Stat stats = enemy.getStats();
        String specializedStat = EnemiesData.getEnemySpecialization(enemy.getName());
        
        // Roll for which stat to train
        double roll = random.nextDouble(); // 0.0 to 1.0
        
        String trainedStat;
        
        if (specializedStat.equals("STRENGTH")) {
            // STR specialist: 60% STR, 20% AGI, 20% INT
            if (roll < 0.6) {
                stats.increaseStrength(amount);
                trainedStat = "STRENGTH";
            } else if (roll < 0.8) {
                stats.increaseAgility(amount);
                trainedStat = "AGILITY";
            } else {
                stats.increaseIntelligence(amount);
                trainedStat = "INTELLIGENCE";
            }
        } else if (specializedStat.equals("AGILITY")) {
            // AGI specialist: 20% STR, 60% AGI, 20% INT
            if (roll < 0.2) {
                stats.increaseStrength(amount);
                trainedStat = "STRENGTH";
            } else if (roll < 0.8) {
                stats.increaseAgility(amount);
                trainedStat = "AGILITY";
            } else {
                stats.increaseIntelligence(amount);
                trainedStat = "INTELLIGENCE";
            }
        } else {
            // INT specialist: 20% STR, 20% AGI, 60% INT
            if (roll < 0.2) {
                stats.increaseStrength(amount);
                trainedStat = "STRENGTH";
            } else if (roll < 0.4) {
                stats.increaseAgility(amount);
                trainedStat = "AGILITY";
            } else {
                stats.increaseIntelligence(amount);
                trainedStat = "INTELLIGENCE";
            }
        }
        
        return trainedStat;
    }

    /**
     * Train an enemy in a specific stat (manual override).
     * Returns true if successful, false otherwise.
     */
    public boolean trainEnemyStat(Enemy enemy, String stat, int amount) {
        if (enemy == null || stat == null || amount <= 0) {
            return false;
        }

        switch(stat.toUpperCase()) {
            case "STR":
            case "STRENGTH":
                enemy.getStats().increaseStrength(amount);
                return true;

            case "AGI":
            case "AGILITY":
                enemy.getStats().increaseAgility(amount);
                return true;

            case "INT":
            case "INTELLIGENCE":
                enemy.getStats().increaseIntelligence(amount);
                return true;

            default:
                return false;
        }
    }

    /**
     * Get the enemy's current stats after any training.
     */
    public Stat getStats(Enemy enemy) {
        if (enemy == null) return null;
        return enemy.getStats();
    }

    /**
     * Get the enemy's specialized stat as a string.
     * Useful for displaying which stat the enemy is training.
     */
    public String getSpecializedStat(Enemy enemy) {
        if (enemy == null) return "UNKNOWN";
        
        Stat stats = enemy.getStats();
        int str = stats.getStrength();
        int agi = stats.getAgility();
        int intel = stats.getIntelligence();
        
        if (str > agi && str > intel) {
            return "STRENGTH";
        } else if (agi > str && agi > intel) {
            return "AGILITY";
        } else if (intel > str && intel > agi) {
            return "INTELLIGENCE";
        }
        
        // Default if tied
        return "STRENGTH";
    }

    /**
     * Convenience method: train enemy strength.
     */
    public boolean trainStrength(Enemy enemy, int amount) {
        return trainEnemyStat(enemy, "STRENGTH", amount);
    }

    /**
     * Convenience method: train enemy agility.
     */
    public boolean trainAgility(Enemy enemy, int amount) {
        return trainEnemyStat(enemy, "AGILITY", amount);
    }

    /**
     * Convenience method: train enemy intelligence.
     */
    public boolean trainIntelligence(Enemy enemy, int amount) {
        return trainEnemyStat(enemy, "INTELLIGENCE", amount);
    }
}