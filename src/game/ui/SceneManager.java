package game.ui;

import game.core.Player;
import game.core.Enemy;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.ArrayList;

public class SceneManager {

    private static Stage stage;
    private static int width;
    private static int height;

    public static void init(Stage primaryStage, int w, int h) {
        stage = primaryStage;
        width = w;
        height = h;
    }

    public static void showStartMenu() {
        StartMenu menu = new StartMenu(); // constructor is now parameterless
        stage.setScene(menu.createScene());
    }

    public static void showCharacterCreation() {
        CharacterCreation cc = new CharacterCreation(width, height);
        stage.setScene(new Scene(cc, width, height));
    }

    public static void showTrainingScreen(Player player) {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy("Goblin",  "AGILITY"));
        enemies.add(new Enemy("Orc",     "STRENGTH"));
        enemies.add(new Enemy("Skeleton","INTELLIGENCE"));

        TrainingScreen ts = new TrainingScreen(player, enemies);
        stage.setScene(ts.createScene());
    }

    public static void showBattleScreen(Player player, Enemy enemy) {
        BattleScreen bs = new BattleScreen(player, enemy);
        stage.setScene(bs.createScene());
    }

    public static void showEndScreen(boolean playerWon) {
        EndScreen es = new EndScreen(width, height, playerWon);
        stage.setScene(new Scene(es, width, height));
    }
}
