package com.gr15.pacman.view.screen;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * PauseView
 */
public class PauseView
    extends VBox {

    private Button resumeButton = new Button("Resume");
    private Button mainMenuButton = new Button("Main Menu");
    private Button quitButton = new Button("Quit");
    
    public PauseView() {
        getChildren().addAll(resumeButton, mainMenuButton, quitButton);
        setAlignment(Pos.CENTER);
    }

    public Button getResumeButton() { return this.resumeButton; }
    public Button getMainMenuButton() { return this.mainMenuButton; }
    public Button getQuitButton() { return this.quitButton; }
}
