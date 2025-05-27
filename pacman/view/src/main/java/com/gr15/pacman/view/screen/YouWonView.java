package com.gr15.pacman.view.screen;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class YouWonView
    extends BaseView {
    
    private VBox root = new VBox();

    private final Label winLabel = new Label("You Win!");
    private final Button playAgainButton = new Button("Play again");
    private final Button mainMenuButton = new Button("Main Menu");
    private final Label scoreLabel;

    public YouWonView(int score) {
        scoreLabel = new Label("Score: " + score);

        winLabel.setTextFill(Color.RED);
        winLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));

        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 24));

        mainMenuButton.setFont(Font.font("Arial", 18));
        playAgainButton.setFont(Font.font("Arial", 18));

        root.getChildren().addAll(winLabel, scoreLabel, mainMenuButton, playAgainButton);
        this.getChildren().add(root);
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
    public Button getPlayAgainButton() { return playAgainButton; }
}
