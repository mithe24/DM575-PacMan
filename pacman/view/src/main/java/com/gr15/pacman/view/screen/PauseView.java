package com.gr15.pacman.view.screen;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Represents the main menu view of the application, containing UI elements
 * such as buttons for starting a new game, resuming a game, and exiting.
 * 
 * This view extends {@link BaseView} and lays out the buttons vertically
 * centered using a {@link VBox}.
 */
public class PauseView
    extends BaseView {

    /** The root layout container for this view. */
    private VBox root = new VBox();

    /*************************************************************
     *                      UI ELEMENTS                          *
     *************************************************************/

    /** Button resume an existing game. */
    private Button resumeButton = new Button("Resume");

    /** Button for exiting to main menu */
    private Button mainMenuButton = new Button("Main Menu");

    /** Button to exit the game. */
    private Button quitButton = new Button("Quit");
    
    /**
     * Constructs a new {@code PauseView} and initializes the layout with
     * three main buttons: Resume, Main Menu, and Exit Game. The layout is
     * vertically centered.
     */
    public PauseView() {
        root.getChildren().addAll(resumeButton, mainMenuButton, quitButton);
        root.setAlignment(Pos.CENTER);
        this.getChildren().add(root);
    }

    /**
     * Called when this view becomes active. Requests focus for input handling.
     */
    @Override
    public void onEnter() {
        this.requestFocus();
    }

    /**
     * Called when the view is exited. This method is part of the
     * view lifecycle and can be overridden to implement specific behavior.
     */
    @Override
    public void onExit() {
        /* No behavior on exit */
    }

    /*************************************************************
     *                          GETTERS                          *
     *************************************************************/

    /**
     * Returns the button used to resume an existing game.
     *
     * @return the resume button
     */
    public Button getResumeButton() { return this.resumeButton; }

    /**
     * Returns the button used to quit to main menu.
     *
     * @return the quit button
     */
    public Button getMainMenuButton() { return this.mainMenuButton; }

    /**
     * Returns the button used to exit the game.
     *
     * @return the quit button
     */
    public Button getQuitButton() { return this.quitButton; }
}
