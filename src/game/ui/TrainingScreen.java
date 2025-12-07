package game.ui;

import game.core.Player;
import game.core.Enemy;
import game.core.Stat;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class TrainingScreen {

    private final Player player;
    private final List<Enemy> enemies;
    private int currentCycle = 0;
    private final int totalCycles = 7;

    private VBox rootVBox;
    private Label statsLabel;
    private Label enemyStatsLabel;
    private Button trainStrBtn, trainAgiBtn, trainIntBtn, nextBtn;

    public TrainingScreen(Player player) {
        this.player = player;

        // Create enemies
        enemies = new ArrayList<>();
        enemies.add(new Enemy("Goblin", new Stat(20, 20, 30)));
        enemies.add(new Enemy("Orc", new Stat(25, 25, 20)));
        enemies.add(new Enemy("Skeleton", new Stat(15, 30, 20)));
    }

    public Scene createScene() {
        StackPane root = new StackPane();
        ImageView bg = UIUtils.loadImageView("battle_bg.png", 800, 600, false);

        rootVBox = new VBox(15);
        rootVBox.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Training Phase");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Player and enemy images
        ImageView playerImg = UIUtils.loadImageView("player_" + player.getProfession().name().toLowerCase() + "_battle.png", 150, 150, true);
        HBox enemyImages = new HBox(10);
        enemyImages.setAlignment(Pos.CENTER);
        for (Enemy e : enemies) {
            ImageView img = UIUtils.loadImageView("enemy_" + e.getName().toLowerCase() + ".png", 120, 120, true);
            enemyImages.getChildren().add(img);
        }

        HBox imagesBox = new HBox(50, playerImg, enemyImages);
        imagesBox.setAlignment(Pos.CENTER);

        // Player and enemy stats
        statsLabel = new Label();
        statsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        enemyStatsLabel = new Label();
        enemyStatsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        updateStatsDisplay();

        // Training buttons
        trainStrBtn = new Button("Train Strength (STR)");
        trainAgiBtn = new Button("Train Agility (AGI)");
        trainIntBtn = new Button("Train Intelligence (INT)");

        HBox trainButtons = new HBox(12, trainStrBtn, trainAgiBtn, trainIntBtn);
        trainButtons.setAlignment(Pos.CENTER);

        // Next button
        nextBtn = new Button("Next Cycle");
        nextBtn.setDisable(true);

        // Button actions
        trainStrBtn.setOnAction(e -> trainStat("STR"));
        trainAgiBtn.setOnAction(e -> trainStat("AGI"));
        trainIntBtn.setOnAction(e -> trainStat("INT"));

        nextBtn.setOnAction(e -> {
            currentCycle++;
            if (currentCycle < totalCycles) {
                trainStrBtn.setDisable(false);
                trainAgiBtn.setDisable(false);
                trainIntBtn.setDisable(false);
                nextBtn.setDisable(true);
            } else {
                SceneManager.showBattleScreen(player, enemies.get(0));
            }
        });

        rootVBox.getChildren().addAll(title, imagesBox, statsLabel, enemyStatsLabel, trainButtons, nextBtn);
        root.getChildren().addAll(bg, rootVBox);
        return new Scene(root, 800, 600);
    }

    private void trainStat(String stat) {
        switch (stat) {
            case "STR" -> player.getStats().increaseStrength(5);
            case "AGI" -> player.getStats().increaseAgility(5);
            case "INT" -> player.getStats().increaseIntelligence(5);
        }

        for (Enemy enemy : enemies) {
            enemy.getStats().increaseRandomStat(5);
        }

        updateStatsDisplay();

        trainStrBtn.setDisable(true);
        trainAgiBtn.setDisable(true);
        trainIntBtn.setDisable(true);
        nextBtn.setDisable(false);
    }

    private void updateStatsDisplay() {
        Stat s = player.getStats();
        statsLabel.setText(String.format(
                "YOUR STATS:\nSTR: %d | AGI: %d | INT: %d\nHP: %d | Speed: %d | Acc: %d | Eva: %d",
                s.getStrength(), s.getAgility(), s.getIntelligence(),
                s.getHp(), s.getSpeed(), s.getAccuracy(), s.getEvasion()
        ));

        StringBuilder enemyText = new StringBuilder("ENEMY STATS:\n");
        for (Enemy e : enemies) {
            Stat es = e.getStats();
            enemyText.append(String.format("%s: STR:%d AGI:%d INT:%d HP:%d\n",
                    e.getName(), es.getStrength(), es.getAgility(), es.getIntelligence(), es.getHp()));
        }
        enemyStatsLabel.setText(enemyText.toString());
    }
}
