package game.ui;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class BattlePanel extends VBox {

    private ImageView enemyIcon;
    private TextArea battleMessages;

    public BattlePanel() {
        setSpacing(10);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);

        enemyIcon = new ImageView(new Image("file:src/game/ui/Assets/enemy.png"));
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
