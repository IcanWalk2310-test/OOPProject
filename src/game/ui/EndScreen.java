package game.ui;

import game.core.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class EndScreen extends StackPane {

    private final int width;
    private final int height;
    private final boolean playerWon;
    private final Player player;

    public EndScreen(int width, int height, boolean playerWon, Player player) {
        this.width = width;
        this.height = height;
        this.playerWon = playerWon;
        this.player = player;
        build();
    }

    private void build() {
        setPrefSize(width, height);

        // Background
        StackPane bg = new StackPane();
        bg.setStyle("-fx-background-color: black;");

        VBox v = new VBox(20);
        v.setAlignment(Pos.CENTER);

        // Victory/Defeat text
        Text result = new Text(playerWon ? "🎉 VICTORY!" : "💀 DEFEAT");
        result.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: yellow;");

        // Player stats summary
        Text stats = new Text(
                "Your Stats:\n" +
                "  STR: " + player.getStat().getSTR() +
                " | AGI: " + player.getStat().getAGI() +
                " | INT: " + player.getStat().getINT() +
                "\n  HP: " + player.getStat().getHP() +
                "/" + player.getMaxHP()
        );
        stats.setStyle("-fx-font-size: 18px; -fx-fill: white;");

        // Back to menu
        Button menuBtn = new Button("Back to Menu");
        menuBtn.setOnAction(e -> SceneManager.showStartMenu());

        v.getChildren().addAll(result, stats, menuBtn);
        getChildren().addAll(bg, v);
    }
}
