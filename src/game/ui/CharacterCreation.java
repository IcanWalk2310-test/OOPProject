package game.ui;

import game.core.Player;
import game.core.Profession;
import game.core.Stat;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CharacterCreation extends StackPane {

    public CharacterCreation(int width, int height) {
        // Background
        ImageView bg = UIUtils.loadImageView("char_create_bg.png", width, height, false);
        BorderPane layout = new BorderPane();

        VBox center = new VBox(12);
        center.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Character Creation");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Character display placeholder (changes when profession selected)
        ImageView charDisplay = UIUtils.loadImageView("player_warrior_battle.png", 150, 150, true);

        // Profession selection buttons
        HBox profButtons = new HBox(16);
        profButtons.setAlignment(Pos.CENTER);

        Button bWar = new Button("Warrior");
        Button bMag = new Button("Mage");
        Button bRog = new Button("Rogue");
        profButtons.getChildren().addAll(bWar, bMag, bRog);

        // Update character image when button clicked
        final Profession[] selected = {Profession.WARRIOR};
        bWar.setOnAction(e -> {
            selected[0] = Profession.WARRIOR;
            charDisplay.setImage(UIUtils.loadImageView("player_warrior_battle.png", 150, 150, true).getImage());
        });
        bMag.setOnAction(e -> {
            selected[0] = Profession.MAGE;
            charDisplay.setImage(UIUtils.loadImageView("player_mage_battle.png", 150, 150, true).getImage());
        });
        bRog.setOnAction(e -> {
            selected[0] = Profession.ROGUE;
            charDisplay.setImage(UIUtils.loadImageView("player_rogue_battle.png", 150, 150, true).getImage());
        });

        // Name input
        HBox nameBox = new HBox(8);
        nameBox.setAlignment(Pos.CENTER);
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField("Hero");
        nameBox.getChildren().addAll(nameLabel, nameField);

        // Start button
        Button start = new Button("Start Adventure");
        start.setOnAction(e -> {
            String chosenName = nameField.getText().trim();
            if (chosenName.isEmpty()) chosenName = "Hero";

            Stat s = new Stat(5, 5, 5);
            Player player = new Player(chosenName, selected[0], s);

            SceneManager.showTrainingScreen(player);
        });

        center.getChildren().addAll(title, charDisplay, profButtons, nameBox, start);
        layout.setCenter(center);

        getChildren().addAll(bg, layout);
    }
}
