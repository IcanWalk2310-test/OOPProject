package game.system;

import game.core.Enemy;
import game.core.Player;
import game.core.Skill;

public class EnemyBattleAI {

    /**
     * Determines which skill the enemy should use based on their AI behavior.
     * Returns the skill index to use, or -1 if no skill is available (use basic attack).
     */
    public int chooseSkill(Enemy enemy, Player player) {
        if (enemy == null || player == null || !enemy.hasSkills()) {
            return -1;
        }

        String enemyName = enemy.getName();
        Skill[] skills = enemy.getSkills();

        // Route to specific AI based on enemy type
        if (enemyName.equals("Killer Bunny")) {
            return killerBunnyAI(enemy, skills);
        } else if (enemyName.equals("Minotaur")) {
            return minotaurAI(enemy, player, skills);
        } else if (enemyName.equals("Mindflayer")) {
            return mindflayerAI(enemy, player, skills);
        }

        // Default: use first available skill
        return defaultAI(enemy, skills);
    }

    /**
     * Killer Bunny AI: Aggressive burst damage
     * Priority: Ultimate (2) > 2nd Skill (1) > Basic (0)
     * Always tries to use highest damage skill available.
     */
    private int killerBunnyAI(Enemy enemy, Skill[] skills) {
        // Try ultimate first (index 2)
        if (skills.length > 2 && enemy.getSkillCooldown(skills[2]) == 0) {
            return 2;
        }

        // Try 2nd skill (index 1)
        if (skills.length > 1 && enemy.getSkillCooldown(skills[1]) == 0) {
            return 1;
        }

        // Fall back to basic attack (index 0)
        if (skills.length > 0 && enemy.getSkillCooldown(skills[0]) == 0) {
            return 0;
        }

        return -1; // No skills available
    }

    /**
     * Minotaur AI: Strategic burst with execute potential
     * - First turn: ALWAYS use ultimate
     * - If ultimate can kill player: use it
     * - Otherwise: cycle between basic (0) and 2nd skill (1)
     */
    private int minotaurAI(Enemy enemy, Player player, Skill[] skills) {
        if (skills.length < 3) return defaultAI(enemy, skills);

        Skill ultimate = skills[2];
        Skill secondSkill = skills[1];
        Skill basic = skills[0];

        // Check if ultimate is ready
        boolean ultimateReady = enemy.getSkillCooldown(ultimate) == 0;

        if (ultimateReady) {
            // Calculate if ultimate can kill player
            int ultimateDamage = ultimate.getBaseDamage() + enemy.getStats().getStrength();
            int playerHp = player.getStats().getHp();

            // Use ultimate if it can kill or on first turn (we assume first turn if ultimate is ready and no pattern established)
            if (ultimateDamage >= playerHp) {
                return 2; // Execute!
            }

            // First turn logic: if player is at full HP, it's likely first turn
            if (playerHp == player.getStats().getMaxHp()) {
                return 2; // Use ultimate on first turn
            }
        }

        // Conservative rotation: 2nd skill if available, otherwise basic
        if (enemy.getSkillCooldown(secondSkill) == 0) {
            return 1;
        }

        if (enemy.getSkillCooldown(basic) == 0) {
            return 0;
        }

        return -1;
    }

    /**
     * Mindflayer AI: Intelligent spellcaster with adaptive strategy
     * RECOMMENDED BEHAVIOR:
     * - Analyzes player HP percentage to decide aggression level
     * - High HP (>70%): Uses 2nd skill and basic to poke, saves ultimate
     * - Medium HP (40-70%): Mixes all skills, looks for ultimate opportunity
     * - Low HP (<40%): Aggressive burst, prioritizes ultimate for the kill
     * - Always maintains mana/cooldown efficiency due to high INT
     */
    private int mindflayerAI(Enemy enemy, Player player, Skill[] skills) {
        if (skills.length < 3) return defaultAI(enemy, skills);

        Skill ultimate = skills[2];      // Mind Shatter (45 dmg, 5 CD)
        Skill secondSkill = skills[1];   // Psychic Blast (20 dmg, 3 CD)
        Skill basic = skills[0];         // Mind Spike (7 dmg, 0 CD)

        // Calculate player HP percentage
        int playerHp = player.getStats().getHp();
        int playerMaxHp = player.getStats().getMaxHp();
        double hpPercent = (double) playerHp / playerMaxHp;

        // Calculate potential damage
        int ultimateDamage = ultimate.getBaseDamage() + enemy.getStats().getStrength();
        int secondDamage = secondSkill.getBaseDamage() + enemy.getStats().getStrength();

        boolean ultimateReady = enemy.getSkillCooldown(ultimate) == 0;
        boolean secondReady = enemy.getSkillCooldown(secondSkill) == 0;

        // LOW HP PHASE: Aggressive finisher (<40% HP)
        if (hpPercent < 0.4) {
            // Try to finish with ultimate
            if (ultimateReady) {
                return 2;
            }
            // Use 2nd skill for high damage
            if (secondReady) {
                return 1;
            }
            // Spam basic if others are on cooldown
            return 0;
        }

        // MEDIUM HP PHASE: Calculated aggression (40-70% HP)
        if (hpPercent < 0.7) {
            // If ultimate can set up a kill combo (player HP low enough)
            if (ultimateReady && (playerHp - ultimateDamage) < secondDamage * 2) {
                return 2; // Ultimate to set up finish
            }

            // Prefer 2nd skill for steady pressure
            if (secondReady) {
                return 1;
            }

            // Use ultimate if 2nd skill is on cooldown and ultimate is up
            if (ultimateReady) {
                return 2;
            }

            // Basic attack as filler
            return 0;
        }

        // HIGH HP PHASE: Conservative, saving ultimate (>70% HP)
        // Use 2nd skill for poke damage
        if (secondReady) {
            return 1;
        }

        // Only use ultimate if 2nd skill is on cooldown AND ultimate has been held for a while
        // This creates an "impatient" feel where it will eventually use ultimate even at high HP
        if (ultimateReady && !secondReady) {
            return 2; // "Might as well use it" logic
        }

        // Default to basic attack
        return 0;
    }

    /**
     * Default AI: Simple priority system
     * Uses first available skill from highest index to lowest.
     */
    private int defaultAI(Enemy enemy, Skill[] skills) {
        for (int i = skills.length - 1; i >= 0; i--) {
            if (enemy.getSkillCooldown(skills[i]) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Helper method to check if a skill can potentially kill the player.
     */
    public boolean canExecute(Enemy enemy, Player player, Skill skill) {
        if (enemy == null || player == null || skill == null) {
            return false;
        }

        int damage = skill.getBaseDamage() + enemy.getStats().getStrength();
        return damage >= player.getStats().getHp();
    }

    /**
     * Get AI behavior description for display purposes.
     */
    public String getAIDescription(String enemyName) {
        return switch (enemyName) {
            case "Killer Bunny" -> "Aggressive: Prioritizes burst damage";
            case "Minotaur" -> "Strategic: Uses ultimate for executes";
            case "Mindflayer" -> "Adaptive: Adjusts strategy based on your HP";
            default -> "Standard: Uses available skills";
        };
    }
}