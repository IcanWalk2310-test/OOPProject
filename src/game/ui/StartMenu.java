package game.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StartMenu {

    public StartMenu() {
        // No parameters needed anymore
    }

    public Scene createScene() {

        // Background
        ImageView bg = UIUtils.loadImageView("start_bg.png", 800, 600, false);

        Button startBtn = new Button("Start Game");
        startBtn.setOnAction(e -> SceneManager.showCharacterCreation());

        VBox v = new VBox(20);
        v.setAlignment(Pos.CENTER);
        v.getChildren().addAll(new Text("Turn-Based RPG Demo"), startBtn);

        StackPane root = new StackPane(bg, v);

        return new Scene(root, 800, 600);
    }
}
