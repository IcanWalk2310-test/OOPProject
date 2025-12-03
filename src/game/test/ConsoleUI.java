package game.test;

import game.system.GameplaySystem;
import game.core.Profession;
import game.core.Skill;
import game.core.Enemy;
import game.core.Player;

import java.util.Scanner;

public class ConsoleUI {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            GameplaySystem game = new GameplaySystem();

            // Welcome & statistics
            printSeparator();
            System.out.println("WELCOME TO __________");
            printSeparator();
            game.showStatistics();
            System.out.println(game.getMotivationalMessage());
            promptEnter(scanner, "Press ENTER to begin...");

            // Character creation
            Player player = createPlayer(scanner, game);

            Skill[] skills = game.getPlayerSkills();
            Enemy[] enemies = game.getEnemies();

            System.out.println("\nâœ“ Game initialized! Prepare for your journey.");
            promptEnter(scanner, "Press ENTER to continue...");

            // Loop over enemies
            for (int enemyIndex = 0; enemyIndex < enemies.length; enemyIndex++) {
                Enemy enemy = enemies[enemyIndex];
                handleEnemyEncounter(scanner, game, player, skills, enemies, enemyIndex, enemy);
            }

            // Endgame
            System.out.println("\nALL ENEMIES DEFEATED - VICTORY!");
            game.recordVictory();
            game.showStatistics();
        }
    }

    // ========================= PLAYER CREATION =========================
    private static Player createPlayer(Scanner scanner, GameplaySystem game) {
        System.out.println("\n=== CHARACTER CREATION ===");
        System.out.print("Enter your player's name: ");
        String name = scanner.nextLine();

        Profession profession = null;
        while (profession == null) {
            System.out.print("Choose profession (WARRIOR, MAGE, ROGUE): ");
            try {
                profession = Profession.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                System.out.println("Invalid profession. Try again.");
            }
        }

        if (!game.initialize(name, profession)) {
            System.out.println("Failed to initialize game!");
            System.exit(0);
        }

        return game.getPlayer();
    }

    // ========================= ENEMY ENCOUNTER =========================
    private static void handleEnemyEncounter(Scanner scanner, GameplaySystem game, Player player,
                                             Skill[] skills, Enemy[] enemies, int enemyIndex, Enemy enemy) {

        printSeparator();
        System.out.println("ENEMY " + (enemyIndex + 1) + " of 3: " + enemy.getName());
        printSeparator();

        // Enemy info
        System.out.println("Specialization: " + game.getEnemySpecializedStat(enemyIndex));
        System.out.println("AI Behavior: " + game.getBattleSystem().getEnemyAI().getAIDescription(enemy.getName()));

        // Training phase
        int trainingCycles = game.getRandomTrainingCycles();
        System.out.println("\n=== TRAINING PHASE (" + trainingCycles + " cycles) ===");
        for (int cycle = 1; cycle <= trainingCycles; ) {
            System.out.println("\n--- Cycle " + cycle + " ---");

            System.out.print("Choose a stat to train (STR, AGI, INT): ");
            String stat = scanner.nextLine().toUpperCase();
            if (game.train(stat, 5)) {
                int[] playerStats = game.getPlayerPrimaryStats();
                System.out.println("âœ“ Training successful! Your stats: STR=" + playerStats[0] +
                        ", AGI=" + playerStats[1] + ", INT=" + playerStats[2]);

                // Train all enemies
                System.out.println("âž¤ Enemies are training...");
                for (int e = 0; e < enemies.length; e++) {
                    String trained = game.trainEnemy(e, 5);
                    int[] es = game.getEnemyPrimaryStats(e);
                    System.out.println("  â€¢ " + enemies[e].getName() + " trained " + trained +
                            " (STR=" + es[0] + ", AGI=" + es[1] + ", INT=" + es[2] + ")");
                }

                cycle++; // Increment only on success
            } else {
                System.out.println("âœ— Invalid stat! Try STR, AGI, or INT.");
            }
        }

        // Pre-battle stats
        showPreBattleStats(player, enemy, game, enemyIndex);

        // Battle phase
        System.out.println("\n=== BATTLE PHASE ===");
        game.prepareBattle(enemyIndex);
        runBattleLoop(scanner, game, player, skills, enemyIndex, enemy);
    }

    // ========================= PRE-BATTLE STATS =========================
    private static void showPreBattleStats(Player player, Enemy enemy, GameplaySystem game, int enemyIndex) {
        int[] ps = game.getPlayerPrimaryStats();
        int[] es = game.getEnemyPrimaryStats(enemyIndex);

        System.out.println("\nYOU:");
        System.out.println("  HP: " + player.getStats().getHp() + "/" + player.getStats().getMaxHp());
        System.out.println("  STR: " + ps[0] + " | AGI: " + ps[1] + " | INT: " + ps[2]);
        System.out.println("  Speed: " + player.getStats().getSpeed() +
                " | Accuracy: " + player.getStats().getAccuracy() +
                " | Evasion: " + player.getStats().getEvasion());

        System.out.println("\n" + enemy.getName().toUpperCase() + ":");
        System.out.println("  HP: " + enemy.getStats().getHp() + "/" + enemy.getStats().getMaxHp());
        System.out.println("  STR: " + es[0] + " | AGI: " + es[1] + " | INT: " + es[2]);
        System.out.println("  Speed: " + enemy.getStats().getSpeed() +
                " | Accuracy: " + enemy.getStats().getAccuracy() +
                " | Evasion: " + enemy.getStats().getEvasion());
    }

    // ========================= BATTLE LOOP =========================
    private static void runBattleLoop(Scanner scanner, GameplaySystem game, Player player,
                                      Skill[] skills, int enemyIndex, Enemy enemy) {

        int playerAP = 0;
        int enemyAP = 0;
        final int ACTION_THRESHOLD = game.getBattleSystem().getActionThreshold();
        final int MAX_TURNS = 1000;
        int turnCount = 0;

        while (!player.isDead() && !enemy.isDead() && turnCount < MAX_TURNS) {
            turnCount++;
            playerAP += player.getStats().getSpeed();
            enemyAP += enemy.getStats().getSpeed();

            // Tick cooldowns
            player.tickAllCooldowns();
            if (enemy.hasSkills()) enemy.tickAllCooldowns();

            // ------------ ENEMY INTENT ------------
            if (!enemy.isDead()) {
                String intent = game.getBattleSystem().getEnemyIntent(player, enemy);
                System.out.println("\nEnemy intends to use: " + intent);
            }

            // ------------ PLAYER TURN ------------
            if (playerAP >= ACTION_THRESHOLD) {
                playerAP -= ACTION_THRESHOLD;
                playerTurn(scanner, game, player, skills, enemy, enemyIndex);
                if (enemy.isDead()) break;
            }

            // ------------ ENEMY TURN ------------
            if (enemyAP >= ACTION_THRESHOLD && !enemy.isDead()) {
                enemyAP -= ACTION_THRESHOLD;
                int dmg = game.enemyAttack(enemyIndex);
                System.out.println("\n" + enemy.getName() + " attacked! " +
                        (dmg > 0 ? "âœ— Hit you for " + dmg + " damage!" : "âœ“ The attack missed!"));
                if (player.isDead()) {
                    System.out.println("You have been defeated!");
                    game.recordDefeat();
                    game.showStatistics();
                    System.exit(0);
                }
            }
        }

        if (enemy.isDead()) {
            System.out.println("VICTORY! " + enemy.getName() + " defeated!");
        }
    }

    // ========================= PLAYER TURN =========================
    private static void playerTurn(Scanner scanner, GameplaySystem game, Player player,
                                   Skill[] skills, Enemy enemy, int enemyIndex) {

        System.out.println("\n--- YOUR TURN ---");
        System.out.println("Your HP: " + player.getStats().getHp() + "/" + player.getStats().getMaxHp());
        System.out.println(enemy.getName() + " HP: " + enemy.getStats().getHp() + "/" + enemy.getStats().getMaxHp());

        Skill chosen = null;
        while (chosen == null) {
            System.out.println("\nAvailable skills:");
            for (int i = 0; i < skills.length; i++) {
                Skill s = skills[i];
                int cd = player.getSkillCooldown(s);
                String status = cd == 0 ? "âœ“ READY" : "â± Cooldown: " + cd + " turn" + (cd > 1 ? "s" : "");
                System.out.println((i + 1) + ". " + s.getName() + " - " + status);
            }

            System.out.print("Choose a skill (1-" + skills.length + "): ");
            try {
                int idx = Integer.parseInt(scanner.nextLine()) - 1;
                if (idx < 0 || idx >= skills.length) {
                    System.out.println("Invalid choice.");
                    continue;
                }

                Skill s = skills[idx];
                if (player.getSkillCooldown(s) > 0) {
                    System.out.println("âœ— " + s.getName() + " is on cooldown! Choose another skill.");
                    continue;
                }
                chosen = s;
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }

        int damage = chosen.use(player, enemy);
        System.out.println("âš” You used " + chosen.getName() + " for " + damage + " damage!");
    }

    // ========================= HELPERS =========================
    private static void printSeparator() {
        System.out.println("\n" + "=".repeat(60));
    }

    private static void promptEnter(Scanner scanner, String message) {
        System.out.println(message);
        scanner.nextLine();
    }
}