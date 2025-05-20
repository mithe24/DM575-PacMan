package com.gr15.pacman.controller.screen;

import com.gr15.pacman.view.ViewManager;
import com.gr15.pacman.view.ViewManager.ViewKeys;
import com.gr15.pacman.view.screen.PauseView;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;

/**
 * The {@code PauseController} handles user interactions on the game's pause screen.
 * 
 * <p>This includes resuming the game, quitting to the main menu,
 * or exiting the application entirely.
 * It also manages key input while the pause view is active,
 * such as using the ESC key to resume the game.</p>
 */
public class PauseController {

    /** The view representing the pause screen UI. */
    private final PauseView pauseView;

    /** The controller managing the main game loop. */
    private final GameController gameController;

    /** The view manager responsible for switching between different scenes. */
    private final ViewManager viewManager;

    /**
     * Constructs a new {@code PauseController} with the specified
     * view, game controller, and view manager.
     *
     * @param pauseView the pause screen UI
     * @param gameController the game controller to resume or control the game state
     * @param viewManager the view manager for switching between views
     */
    public PauseController(PauseView pauseView, GameController gameController,
        ViewManager viewManager) {
        this.gameController = gameController;
        this.pauseView = pauseView;
        this.viewManager = viewManager;
        setupEventHandlers();
    }

    /**
     * Initializes event handlers for pause screen buttons and keyboard input.
     */
    private void setupEventHandlers() {
        pauseView.getQuitButton().setOnAction(this::exitGame);
        pauseView.getMainMenuButton().setOnAction(this::quitToMainMenu);
        pauseView.getResumeButton().setOnAction(this::resumeGame);
        pauseView.setOnKeyPressed(this::handleKeyEvent);
    }

    /**
     * Handles key events while the pause view is active.
     * 
     * <p>Pressing ESC will resume the game and return to the game view.</p>
     *
     * @param event the key event triggered by user input
     */
    private void handleKeyEvent(KeyEvent event) {
        switch (event.getCode()) {
            case ESCAPE -> {
                viewManager.showView(ViewKeys.GAME_VIEW);
                gameController.startGameLoop();
            }
            default -> {}
        }
    }

    /**
     * Handles the event when the "Main Menu" button is clicked.
     * 
     * @param event the action event triggered by the button
     */
    private void quitToMainMenu(ActionEvent event) {
        viewManager.showView(ViewKeys.MAIN_MENU);
    }

    /**
     * Handles the event when the "Quit" button is clicked.
     * 
     * <p>This will exit the application immediately.</p>
     *
     * @param event the action event triggered by the button
     */
    private void exitGame(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Handles the event when the "Resume" button is clicked.
     * 
     * <p>This resumes the game by switching back to the game view
     * and starting the game loop.</p>
     *
     * @param event the action event triggered by the button
     */
    private void resumeGame(ActionEvent evnet) {
        viewManager.showView(ViewKeys.GAME_VIEW);
        gameController.startGameLoop();
    }
}
