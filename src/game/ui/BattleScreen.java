package game.ui;

import game.core.Player;
import game.core.Enemy;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class BattleScreen {

    private final Player player;
    private final Enemy enemy;

    public BattleScreen(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    public Scene createScene() {
        // Background
        ImageView bg = UIUtils.loadImageView("battle_bg.png", 800, 600, false);

        // Player image based on profession
        ImageView playerImg;
        switch (player.getProfession()) {
            case WARRIOR -> playerImg = UIUtils.loadImageView("player_warrior_battle.png", 150, 150, true);
            case MAGE -> playerImg = UIUtils.loadImageView("player_mage_battle.png", 150, 150, true);
            case ROGUE -> playerImg = UIUtils.loadImageView("player_rogue_battle.png", 150, 150, true);
            default -> playerImg = UIUtils.loadImageView("player_warrior_battle.png", 150, 150, true);
        }

        // Enemy image mapping
        ImageView enemyImg;
        switch (enemy.getName()) {
            case "Goblin" -> enemyImg = UIUtils.loadImageView("enemy_goblin.png", 150, 150, true);
            case "Orc" -> enemyImg = UIUtils.loadImageView("enemy_orc.png", 150, 150, true);
            case "Skeleton" -> enemyImg = UIUtils.loadImageView("enemy_skeleton.png", 150, 150, true);
            default -> enemyImg = UIUtils.loadImageView("enemy_goblin.png", 150, 150, true);
        }

        HBox characters = new HBox(50, playerImg, enemyImg);
        characters.setAlignment(Pos.CENTER);

        // HP / stats display
        VBox statsBox = new VBox(10);
        statsBox.setAlignment(Pos.CENTER);

        Label playerStats = new Label(
                player.getName() + " HP: " + player.getStat().getHP() +
                        " | STR: " + player.getStat().getSTR() +
                        " | AGI: " + player.getStat().getAGI() +
                        " | INT: " + player.getStat().getINT()
        );
        Label enemyStats = new Label(
                enemy.getName() + " HP: " + enemy.getStat().getHP() +
                        " | STR: " + enemy.getStat().getSTR() +
                        " | AGI: " + enemy.getStat().getAGI() +
                        " | INT: " + enemy.getStat().getINT()
        );

        statsBox.getChildren().addAll(playerStats, enemyStats);

        // Back button
        Button backBtn = new Button("Back to Menu");
        backBtn.setOnAction(e -> SceneManager.showStartMenu());

        VBox layout = new VBox(20, characters, statsBox, backBtn);
        layout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(bg, layout);

        return new Scene(root, 800, 600);
    }
}
