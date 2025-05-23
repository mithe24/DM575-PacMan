package com.gr15.pacman.view.screen;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class YouWonView
    extends BaseView {
    
    private VBox root = new VBox(20);

    private final Label winMessage = new Label("🎉 You Win! 🎉");
    private final Label finalScoreLabel;
    private final Button mainMenuButton = new Button("Main Menu");

    public YouWonView(int score) {
        finalScoreLabel = new Label("Final Score: " + score);

        winMessage.setStyle("-fx-font-size: 36px; -fx-text-fill: white;");
        finalScoreLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        mainMenuButton.setPrefWidth(200);



        root.setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: black;");
        this.getChildren().addAll(winMessage, finalScoreLabel, mainMenuButton);
    }

    @Override
    public void onEnter() {
        /* No specific behavior */
    }    

    @Override
    public void onExit() {
        /* No specific behavior */
    }

    public Button getMainMenuButton() { return mainMenuButton; }
}
