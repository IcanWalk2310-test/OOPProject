package game.ui;

import game.core.Player;
import game.core.Enemy;
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
<<<<<<< Updated upstream
<<<<<<< Updated upstream

        // Background
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
        ImageView bg = UIUtils.loadImageView("char_create_bg.png", width, height, false);

        BorderPane layout = new BorderPane();

        VBox center = new VBox(12);
        center.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Character Creation");

<<<<<<< Updated upstream
<<<<<<< Updated upstream
        // Icons placeholders
        HBox icons = new HBox(16);
        icons.setAlignment(Pos.CENTER);

=======
=======
>>>>>>> Stashed changes
        // Character display
        ImageView charDisplay = UIUtils.loadImageView("player_warrior_battle.png", 150, 150, true);

        // Profession buttons
        HBox profButtons = new HBox(16);
        profButtons.setAlignment(Pos.CENTER);
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
        Button bWar = new Button("Warrior");
        Button bMag = new Button("Mage");
        Button bRog = new Button("Rogue");

<<<<<<< Updated upstream
<<<<<<< Updated upstream
        icons.getChildren().addAll(bWar, bMag, bRog);
=======
=======
>>>>>>> Stashed changes
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
>>>>>>> Stashed changes

        // Name input
        HBox nameBox = new HBox(8);
        nameBox.setAlignment(Pos.CENTER);
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField("Hero");
        nameBox.getChildren().addAll(nameLabel, nameField);

        // Selected profession
        final Profession[] selected = {Profession.WARRIOR};

        bWar.setOnAction(e -> selected[0] = Profession.WARRIOR);
        bMag.setOnAction(e -> selected[0] = Profession.MAGE);
        bRog.setOnAction(e -> selected[0] = Profession.ROGUE);

        // Start button
        Button start = new Button("Start Adventure");
        start.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) name = "Hero";
<<<<<<< Updated upstream

<<<<<<< Updated upstream
            Stat eStat = new Stat(4, 3, 2);
            Enemy enemy = new Enemy("Goblin", eStat);

            SceneManager.showBattleScreen(player, enemy);
=======
=======

>>>>>>> Stashed changes
            Player player = new Player(name, selected[0], new Stat(5,5,5));
            SceneManager.showTrainingScreen(player);
>>>>>>> Stashed changes
        });

        center.getChildren().addAll(title, icons, nameBox, start);
        layout.setCenter(center);
        getChildren().addAll(bg, layout);
    }
}
