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
public class MainMenuView
    extends BaseView {

    /** The root layout container for this view. */
    private VBox root = new VBox();

    /*************************************************************
     *                      UI ELEMENTS                          *
     *************************************************************/
    
    /** Button to start a new game. */
    private Button newGameButton = new Button("New Game");
    
    /** Button resume an existing game. */
    private Button resumeButton = new Button("Resume");
    
    /** Button to exit the game. */
    private Button exitButton = new Button("Exit Game");
    
    /**
     * Constructs a new {@code MainMenuView} and initializes the layout with
     * three main buttons: Resume, New Game, and Exit Game. The layout is
     * vertically centered.
     */
    public MainMenuView() {
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(resumeButton, newGameButton, exitButton);
        getChildren().add(root);
    }

    /*************************************************************
     *                          METHODS                          *
     *************************************************************/

    /**
     * Called when the view is entered. This method is part of the
     * view lifecycle and can be overridden to implement specific behavior.
     */
    @Override
    public void onEnter() {
        /* No specific behavior yet. */
    }

    /**
     * Called when the view is exited. This method is part of the
     * view lifecycle and can be overridden to implement specific behavior.
     */
    @Override
    public void onExit() {
        /* No specific behavior yet. */
    }

    /*************************************************************
     *                   GETTERS AND SETTERS                     *
     *************************************************************/

    /**
     * Returns the button used to start a new game.
     *
     * @return the new game button
     */
    public Button getNewGameButton() { return this.newGameButton; }

    /**
     * Returns the button used to resume an existing game.
     *
     * @return the resume button
     */
    public Button getResumeButton() { return this.resumeButton; }

    /**
     * Returns the button used to exit the game.
     *
     * @return the exit button
     */
    public Button getExitButton() { return this.exitButton; }
}
