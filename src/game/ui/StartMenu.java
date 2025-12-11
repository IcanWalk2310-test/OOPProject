package game.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StartMenu {

<<<<<<< Updated upstream
    private final Stage stage;

    public StartMenu(Stage stage) {
        this.stage = stage;
=======
    public StartMenu() {
        // No parameters needed
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    }

    public Scene createScene() {
        ImageView bg = UIUtils.loadImageView("start_bg.png", 800, 600, false);

        Button startBtn = new Button("Start Adventure");
        startBtn.setOnAction(e -> SceneManager.showCharacterCreation());

        VBox v = new VBox(20);
        v.setAlignment(Pos.CENTER);
        v.getChildren().addAll(new Text("Turn-Based RPG Demo"), startBtn);

        StackPane root = new StackPane(bg, v);
        return new Scene(root, 800, 600);
    }
}
