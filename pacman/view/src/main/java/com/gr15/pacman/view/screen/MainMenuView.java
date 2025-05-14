package com.gr15.pacman.view.screen;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.layout.VBox;

/**
 * MainMenuView
 */
public class MainMenuView
    extends VBox {

    private Button newGameButton = new Button("New Game");
    private Button resumeButton = new Button("Resume");
    private Button quitButton = new Button("Exit Game");
    
    public MainMenuView() {
        resumeButton.setDisable(true);
        getChildren().addAll(resumeButton, newGameButton, quitButton);
        setAlignment(Pos.CENTER);
    }

    public Button getNewGameButton() { return this.newGameButton; }
    public Button getResumeButton() { return this.resumeButton; }
    public Button getQuitButton() { return this.quitButton; }
}
