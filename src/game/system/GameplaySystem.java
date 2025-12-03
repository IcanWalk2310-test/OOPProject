package game.system;

import game.core.Player;
import game.core.Skill;
import game.core.Profession;
import game.core.Enemy;
import game.data.EnemiesData;
import game.data.SkillsData;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameplaySystem {

    private final PlayerSystem playerSystem;
    private final TrainingSystem trainingSystem;
    private final EnemyTrainingSystem enemyTrainingSystem;
    private final BattleSystem battleSystem;
    private final SkillSystem skillSystem;
    private final SessionTracker sessionTracker;
    private final Random random;

    private Skill[] playerSkills;
    private Enemy[] enemies;
    private Enemy[] enemyTemplates;

    public GameplaySystem() {
        this.skillSystem = new SkillSystem();
        this.playerSystem = new PlayerSystem();
        this.trainingSystem = new TrainingSystem();
        this.enemyTrainingSystem = new EnemyTrainingSystem();
        this.battleSystem = new BattleSystem(skillSystem);
        this.sessionTracker = new SessionTracker();
        this.random = new Random();
    }

    // ======== INITIALIZATION ========
    public boolean initialize(String playerName, Profession profession) {
        if (playerSystem.hasPlayer()) return false;

        playerSystem.createPlayer(playerName, profession);
        playerSkills = SkillsData.getSkillsForProfession(profession);

        enemyTemplates = EnemiesData.getAllEnemyTypes();
        enemies = createShuffledEnemies();
        return true;
    }

    private Enemy[] createShuffledEnemies() {
        List<Enemy> enemyList = new ArrayList<>();
        for (Enemy template : enemyTemplates) {
            enemyList.add(EnemiesData.createEnemyCopy(template));
        }
        Collections.shuffle(enemyList, random);
        return enemyList.toArray(new Enemy[0]);
    }

    public void reset() {
        playerSystem.reset();
        playerSkills = null;
        if (enemyTemplates != null) {
            enemies = createShuffledEnemies();
        }
    }

    // ======== TRAINING ========
    public boolean train(String statToTrain, int amount) {
        Player player = playerSystem.getPlayer();
        if (player == null) return false;
        return trainingSystem.train(player, statToTrain, amount);
    }

    public String trainEnemy(int enemyIndex, int amount) {
        if (!validateEnemyIndex(enemyIndex)) return null;
        Enemy enemy = enemies[enemyIndex];
        return enemyTrainingSystem.trainEnemy(enemy, amount);
    }

    public String getEnemySpecializedStat(int enemyIndex) {
        if (!validateEnemyIndex(enemyIndex)) return "UNKNOWN";
        return enemyTrainingSystem.getSpecializedStat(enemies[enemyIndex]);
    }

    // ======== STATS ========
    public int[] getPlayerPrimaryStats() {
        Player player = playerSystem.getPlayer();
        if (player == null) return new int[]{0, 0, 0};
        return new int[]{player.getStats().getStrength(), player.getStats().getAgility(), player.getStats().getIntelligence()};
    }

    public int[] getEnemyPrimaryStats(int enemyIndex) {
        if (!validateEnemyIndex(enemyIndex)) return new int[]{0, 0, 0};
        Enemy enemy = enemies[enemyIndex];
        return new int[]{enemy.getStats().getStrength(), enemy.getStats().getAgility(), enemy.getStats().getIntelligence()};
    }

    public int getRandomTrainingCycles() {
        return 3 + random.nextInt(5);
    }

    // ======== BATTLE ========
    public void prepareBattle(int enemyIndex) {
        Player player = playerSystem.getPlayer();
        if (player != null) {
            player.resetAllCooldowns();
            player.getStats().fullHeal();
        }
        if (validateEnemyIndex(enemyIndex)) {
            Enemy enemy = enemies[enemyIndex];
            enemy.resetAllCooldowns();
            enemy.getStats().fullHeal();
        }
    }

    public int playerAttack(int skillIndex, int enemyIndex) {
        if (!validateSkillIndex(skillIndex) || !validateEnemyIndex(enemyIndex)) return -1;
        Player player = playerSystem.getPlayer();
        Enemy enemy = enemies[enemyIndex];
        Skill skill = playerSkills[skillIndex];

        // Check skill readiness
        if (!skill.canUse(player)) return -1;

        int damage = skill.use(player, enemy);
        return damage;
    }

    public int enemyAttack(int enemyIndex) {
        if (!validateEnemyIndex(enemyIndex)) return 0;
        Player player = playerSystem.getPlayer();
        Enemy enemy = enemies[enemyIndex];
        if (player == null) return 0;
        return battleSystem.enemyAttack(player, enemy);
    }

    // ======== COOLDOWN MANAGEMENT ========
    public int getPlayerSkillCooldown(int skillIndex) {
        if (!validateSkillIndex(skillIndex)) return 0;
        Player player = playerSystem.getPlayer();
        if (player == null) return 0;
        return player.getSkillCooldown(playerSkills[skillIndex]);
    }

    public void tickPlayerCooldowns() {
        Player player = playerSystem.getPlayer();
        if (player != null) player.tickAllCooldowns();
    }

    public void tickEnemyCooldowns(int enemyIndex) {
        if (!validateEnemyIndex(enemyIndex)) return;
        Enemy enemy = enemies[enemyIndex];
        if (enemy.hasSkills()) enemy.tickAllCooldowns();
    }

    // ======== SIMULATION ========
    public boolean simulateBattle(int enemyIndex) {
        if (!validateEnemyIndex(enemyIndex)) return false;
        Player player = playerSystem.getPlayer();
        Enemy enemy = enemies[enemyIndex];
        if (player == null) return false;
        return battleSystem.simulateBattle(player, enemy, playerSkills);
    }

    public boolean canUseSkill(int skillIndex) {
        if (!validateSkillIndex(skillIndex)) return false;
        Player player = playerSystem.getPlayer();
        if (player == null) return false;
        return playerSkills[skillIndex].canUse(player);
    }

    // ======== SESSION TRACKING ========
    public void recordVictory() {
        Player player = playerSystem.getPlayer();
        if (player == null) return;
        sessionTracker.recordGame(true, player.getProfession().name(),
                player.getStats().getStrength(), player.getStats().getAgility(), player.getStats().getIntelligence());
    }

    public void recordDefeat() {
        Player player = playerSystem.getPlayer();
        if (player == null) return;
        sessionTracker.recordGame(false, player.getProfession().name(),
                player.getStats().getStrength(), player.getStats().getAgility(), player.getStats().getIntelligence());
    }

    public void showStatistics() {
        sessionTracker.printStats();
    }

    public String getMotivationalMessage() {
        return sessionTracker.getMotivationalMessage();
    }

    // ======== VALIDATION ========
    private boolean validateSkillIndex(int skillIndex) {
        return playerSkills != null && skillIndex >= 0 && skillIndex < playerSkills.length;
    }

    private boolean validateEnemyIndex(int enemyIndex) {
        return enemies != null && enemyIndex >= 0 && enemyIndex < enemies.length;
    }

    // ======== ACCESSORS ========
    public Player getPlayer() { return playerSystem.getPlayer(); }
    public Skill[] getPlayerSkills() { return playerSkills; }
    public Enemy[] getEnemies() { return enemies; }
    public BattleSystem getBattleSystem() { return battleSystem; }
    public SkillSystem getSkillSystem() { return skillSystem; }
    public TrainingSystem getTrainingSystem() { return trainingSystem; }
    public EnemyTrainingSystem getEnemyTrainingSystem() { return enemyTrainingSystem; }
    public PlayerSystem getPlayerSystem() { return playerSystem; }
    public SessionTracker getSessionTracker() { return sessionTracker; }
}