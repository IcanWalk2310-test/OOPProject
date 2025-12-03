package game.data;

import game.core.Enemy;
import game.core.Stat;
import game.core.Skill;
import java.util.HashMap;
import java.util.Map;

public class EnemiesData {

    // Track each enemy's original specialization
    private static final Map<String, String> ENEMY_SPECIALIZATIONS = new HashMap<>();
    
    static {
        ENEMY_SPECIALIZATIONS.put("Killer Bunny", "AGILITY");
        ENEMY_SPECIALIZATIONS.put("Minotaur", "STRENGTH");
        ENEMY_SPECIALIZATIONS.put("Mindflayer", "INTELLIGENCE");
    }

    /**
     * Get the original specialization for an enemy by name.
     * Used by EnemyTrainingSystem to determine training probabilities.
     */
    public static String getEnemySpecialization(String enemyName) {
        return ENEMY_SPECIALIZATIONS.getOrDefault(enemyName, "STRENGTH");
    }

    /**
     * Returns all available enemy types.
     * Each enemy starts with +15 in their specialized stat and +5 in secondary stats.
     * - Killer Bunny: AGI specialist (25 STR, 35 AGI, 25 INT)
     * - Minotaur: STR specialist (35 STR, 25 AGI, 25 INT)
     * - Mindflayer: INT specialist (25 STR, 25 AGI, 35 INT)
     */
    public static Enemy[] getAllEnemyTypes() {
        return new Enemy[] {
            createKillerBunny(),
            createMinotaur(),
            createMindflayer()
        };
    }

    /**
     * Killer Bunny - AGI specialist
     * Fast, evasive, hits quickly with moderate damage
     * AI: Aggressive burst - prioritizes highest damage skill
     * Specialization: AGILITY (must match ENEMY_SPECIALIZATIONS map)
     */
    private static Enemy createKillerBunny() {
        Stat stats = new Stat(15, 30, 15); // +5 STR, +15 AGI, +5 INT from player base (20)
        
        Skill[] skills = new Skill[] {
            new Skill("Rapid Bite", null, 10, 0),         // Index 0: Basic attack (increased from 8)
            new Skill("Pounce", null, 22, 2),             // Index 1: 2nd skill (increased from 18)
            new Skill("Frenzy", null, 35, 3)              // Index 2: Ultimate (increased from 30)
        };
        
        return new Enemy("Killer Bunny", stats, skills);
    }

    /**
     * Minotaur - STR specialist
     * Tank with high HP, hits very hard but slower
     * AI: Strategic execute - uses ultimate on first turn and when it can kill
     * Specialization: STRENGTH (must match ENEMY_SPECIALIZATIONS map)
     */
    private static Enemy createMinotaur() {
        Stat stats = new Stat(30, 15, 15); // +15 STR, +5 AGI, +5 INT from player base (20)
        
        Skill[] skills = new Skill[] {
            new Skill("Axe Swing", null, 12, 0),          // Index 0: Basic attack (increased from 10)
            new Skill("Charge", null, 25, 2),             // Index 1: 2nd skill (increased from 22)
            new Skill("Earthquake", null, 42, 3)          // Index 2: Ultimate (increased from 38)
        };
        
        return new Enemy("Minotaur", stats, skills);
    }

    /**
     * Mindflayer - INT specialist
     * High accuracy, low cooldowns, adaptive strategy
     * AI: Adaptive intelligence - adjusts based on player HP percentage
     * Specialization: INTELLIGENCE (must match ENEMY_SPECIALIZATIONS map)
     */
    private static Enemy createMindflayer() {
        Stat stats = new Stat(15, 15, 30); // +5 STR, +5 AGI, +15 INT from player base (20)
        
        Skill[] skills = new Skill[] {
            new Skill("Mind Spike", null, 9, 0),          // Index 0: Basic attack (increased from 7)
            new Skill("Psychic Blast", null, 24, 3),      // Index 1: 2nd skill (increased from 20)
            new Skill("Mind Shatter", null, 50, 5)        // Index 2: Ultimate (increased from 45)
        };
        
        return new Enemy("Mindflayer", stats, skills);
    }

    /**
     * Creates a fresh copy of an enemy for training.
     * This allows each enemy to train independently during gameplay.
     */
    public static Enemy createEnemyCopy(Enemy template) {
        // Create new stat instance with same base values
        Stat newStats = new Stat(
            template.getStats().getStrength(),
            template.getStats().getAgility(),
            template.getStats().getIntelligence()
        );
        
        // Return new enemy with same name and skills but fresh stats
        return new Enemy(template.getName(), newStats, template.getSkills());
    }
}