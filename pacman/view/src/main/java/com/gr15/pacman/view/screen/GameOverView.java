package com.gr15.pacman.view.screen;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Simple Game Over screen with message and button.
 */
public class GameOverView
    extends BaseView {

    private final VBox root = new VBox();

    private final Label gameOverLabel = new Label("Game Over!");
    private final Button mainMenuButton = new Button("Return to Main Menu");
    private final Button playAgainButton = new Button("Play again");
    private final Label scoreLabel;

    public GameOverView(int score) {
        scoreLabel = new Label("Score: " + score);

        gameOverLabel.setTextFill(Color.RED);
        gameOverLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));

        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 24));

        mainMenuButton.setFont(Font.font("Arial", 18));
        playAgainButton.setFont(Font.font("Arial", 18));

        root.getChildren().addAll(gameOverLabel, scoreLabel, mainMenuButton, playAgainButton);
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
