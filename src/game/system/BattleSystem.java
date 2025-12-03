package game.system;

import game.core.Player;
import game.core.Enemy;
import game.core.Skill;

public class BattleSystem {

    private final SkillSystem skillSystem;
    private final EnemyBattleAI enemyAI;
    private final int ACTION_THRESHOLD = 100;

    public BattleSystem(SkillSystem skillSystem) {
        this.skillSystem = skillSystem;
        this.enemyAI = new EnemyBattleAI();
    }

    // ========================= PLAYER ACTION ========================= //

    /**
     * Player uses a skill on an enemy.
     * Returns the damage dealt or -1 if invalid.
     * Will NOT modify cooldowns if the skill is on cooldown.
     */
    public int playerAttack(Player player, Enemy enemy, Skill skill) {
        if (player == null || enemy == null || skill == null) return -1;

        // Check if skill is ready
        if (!skillSystem.canUseSkill(player, skill)) {
            return -1; // Skill cannot be used; cooldown remains untouched
        }

        // Attempt hit
        boolean hit = attemptHit(player.getStats().getAccuracy(), enemy.getStats().getEvasion());
        int damage = 0;

        if (hit) {
            // Hit: apply skill
            damage = skill.use(player, enemy);
        } else {
            // Miss: apply damage 0
            damage = 0;

            // Apply cooldown normally for non-zero-CD skills
            if (skill.getBaseCooldown() > 0) {
                int finalCD = Math.max(1, skill.getBaseCooldown() - player.getStats().getCooldownReduction());
                player.setSkillCooldown(skill, finalCD);
            }
        }

        return damage;
    }

    // ========================= ENEMY ACTION ========================= //

    /**
     * Enemy attacks the player using AI.
     * Returns damage dealt.
     */
    public int enemyAttack(Player player, Enemy enemy) {
        if (player == null || enemy == null) return -1;

        if (!enemy.hasSkills()) {
            // No skills: basic attack
            boolean hit = attemptHit(enemy.getStats().getAccuracy(), player.getStats().getEvasion());
            int damage = hit ? enemy.getStats().getStrength() : 0;
            player.takeDamage(damage);
            return damage;
        }

        int skillIndex = enemyAI.chooseSkill(enemy, player);

        if (skillIndex < 0 || skillIndex >= enemy.getSkills().length) {
            // Basic attack fallback
            boolean hit = attemptHit(enemy.getStats().getAccuracy(), player.getStats().getEvasion());
            int damage = hit ? enemy.getStats().getStrength() : 0;
            player.takeDamage(damage);
            return damage;
        }

        Skill chosenSkill = enemy.getSkills()[skillIndex];

        if (enemy.getSkillCooldown(chosenSkill) > 0) {
            // Skill on cooldown â†’ fallback to basic attack
            boolean hit = attemptHit(enemy.getStats().getAccuracy(), player.getStats().getEvasion());
            int damage = hit ? enemy.getStats().getStrength() : 0;
            player.takeDamage(damage);
            return damage;
        }

        // Attempt hit
        boolean hit = attemptHit(enemy.getStats().getAccuracy(), player.getStats().getEvasion());
        int damage = 0;

        if (hit) {
            damage = chosenSkill.getBaseDamage() + enemy.getStats().getStrength();
            player.takeDamage(damage);
        } else {
            damage = 0;
        }

        // Apply cooldown only if skill has baseCooldown > 0
        if (chosenSkill.getBaseCooldown() > 0) {
            int finalCD = Math.max(1, chosenSkill.getBaseCooldown() - enemy.getStats().getCooldownReduction());
            enemy.setSkillCooldown(chosenSkill, finalCD);
        }

        return damage;
    }

    // ========================= HIT CHANCE ========================= //

    public boolean attemptHit(int accuracy, int evasion) {
        int hitChance = accuracy - evasion;
        hitChance = Math.max(5, Math.min(95, hitChance));
        int roll = (int)(Math.random() * 100) + 1;
        return roll <= hitChance;
    }

    // ========================= TURN READINESS ========================= //

    public boolean isPlayerTurnReady(int ap) {
        return ap >= ACTION_THRESHOLD;
    }

    public boolean isEnemyTurnReady(int ap) {
        return ap >= ACTION_THRESHOLD;
    }

    public int getActionThreshold() {
        return ACTION_THRESHOLD;
    }

    // ========================= SIMULATION ========================= //

    /**
     * Simulates an automated battle.
     * Cooldowns for all skills tick at the START of each turn.
     */
    public boolean simulateBattle(Player player, Enemy enemy, Skill[] skills) {
        if (player == null || enemy == null || skills == null || skills.length == 0) {
            return false;
        }

        int playerAP = 0;
        int enemyAP = 0;
        int maxTurns = 1000;
        int turnCount = 0;

        while (!player.isDead() && !enemy.isDead() && turnCount < maxTurns) {
            turnCount++;

            playerAP += player.getStats().getSpeed();
            enemyAP += enemy.getStats().getSpeed();

            // Tick all cooldowns at start of turn
            player.tickAllCooldowns();
            if (enemy.hasSkills()) {
                enemy.tickAllCooldowns();
            }

            // ------------ Player Turn ------------
            if (playerAP >= ACTION_THRESHOLD) {
                playerAP -= ACTION_THRESHOLD;

                // Try using the first available skill
                for (Skill skill : skills) {
                    if (skill != null && skillSystem.canUseSkill(player, skill)) {
                        playerAttack(player, enemy, skill);
                        break;
                    }
                }

                if (enemy.isDead()) return true;
            }

            // ------------ Enemy Turn ------------
            if (enemyAP >= ACTION_THRESHOLD) {
                enemyAP -= ACTION_THRESHOLD;
                enemyAttack(player, enemy);

                if (player.isDead()) return false;
            }
        }

        return !player.isDead() && enemy.isDead();
    }

    // ========================= ENEMY INTENT ========================= //

    /**
     * Returns the name of the skill the enemy plans to use on this turn.
     * If no skill is ready, returns "Basic Attack".
     */
    public String getEnemyIntent(Player player, Enemy enemy) {
        if (enemy == null || !enemy.hasSkills()) return "Basic Attack";

        int skillIndex = enemyAI.chooseSkill(enemy, player);

        if (skillIndex < 0 || skillIndex >= enemy.getSkills().length) {
            return "Basic Attack";
        }

        Skill chosenSkill = enemy.getSkills()[skillIndex];

        if (enemy.getSkillCooldown(chosenSkill) > 0) {
            return "Basic Attack";
        }

        return chosenSkill.getName();
    }

    // ========================= ENEMY AI ========================= //

    public EnemyBattleAI getEnemyAI() {
        return enemyAI;
    }
}