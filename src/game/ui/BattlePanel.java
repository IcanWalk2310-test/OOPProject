package game.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class BattlePanel extends VBox {

    private final ImageView enemyIcon;
    private final TextArea battleMessages;

    public BattlePanel(String enemyName) {
        setSpacing(10);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);

        enemyIcon = new ImageView(new Image("file:src/game/ui/Assets/enemy_" + enemyName.toLowerCase() + ".png"));
        enemyIcon.setFitWidth(80);
        enemyIcon.setFitHeight(80);

        battleMessages = new TextArea();
        battleMessages.setPrefHeight(200);
        battleMessages.setEditable(false);
        battleMessages.setWrapText(true);

        getChildren().addAll(enemyIcon, battleMessages);
    }

    public void addMessage(String msg) {
        battleMessages.appendText(msg + "\n");
    }
}
