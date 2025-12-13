package game.ui;

import game.core.Enemy;
import java.util.*;

public class GameSession {

    public static Queue<Enemy> remainingEnemies;
    public static boolean lastBattleWon;

    public static void init(List<Enemy> enemies) {
        Collections.shuffle(enemies);
        remainingEnemies = new ArrayDeque<>(enemies);
        lastBattleWon = true;
    }

    public static Enemy nextEnemy() {
        return remainingEnemies.poll();
    }

    public static boolean hasMoreEnemies() {
        return !remainingEnemies.isEmpty();
    }
}
