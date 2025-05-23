package com.gr15.pacman.controller.screen;

import com.gr15.pacman.model.ConfigBuilder;
import com.gr15.pacman.model.GameState;
import com.gr15.pacman.view.ViewManager;
import com.gr15.pacman.view.screen.GameView;
import com.gr15.pacman.view.screen.MainMenuView;
import com.gr15.pacman.view.screen.PauseView;

import javafx.event.ActionEvent;

import com.gr15.pacman.view.ViewManager.ViewKeys;

/**
 * The {@code MainMenuController} handles interactions on the main menu screen.
 */
public class MainMenuController {

    /** Manages switching between different views/screens in the application. */
    private final ViewManager viewManager;

    /** The main menu view UI. */
    private final MainMenuView mainMenuView;

    /**
     * Constructs a new {@code MainMenuController} with the given
     * view manager and main menu view.
     *
     * @param viewManager the manager responsible for view navigation
     * @param mainMenuView the view representing the main menu UI
     * @throws IllegalArgumentException if viewManager or mainMenuView is {@code null}
     */
    public MainMenuController(ViewManager viewManager, MainMenuView mainMenuView) {
        if (viewManager == null) {
            throw new IllegalArgumentException("viewManager must not be null");
        }
        if (mainMenuView == null) {
            throw new IllegalArgumentException("mainMenuView must not be null");
        }
        this.viewManager = viewManager;
        this.mainMenuView = mainMenuView;
        initializeButtons();
    }

    /**
     * Initializes event handlers for the main menu buttons.
     * 
     * <p>This includes setting actions for
     * starting a new game, and exiting.</p>
     */
    private void initializeButtons() {
        mainMenuView.getNewGameButton().setOnAction(this::startNewGame);
        mainMenuView.getExitButton().setOnAction(this::exitGame);
    }

    /**
     * Starts a new game by creating a new game state, views, and controllers.
     * 
     * <p>Loads the initial game state from a JSON file,
     * initializes all related components, and switches to the game view.</p>
     *
     * @param event the action event triggered by the button
     */
    private void startNewGame(ActionEvent event) {
        GameState gameState = new GameState(ConfigBuilder.fromJson(
            this.getClass().getResourceAsStream("/testConfig.json")));

        /* Creating new views */
        GameView gameView = new GameView(gameState);

        /* Creaing new controllers */
        GameController gameController = new GameController(gameState, 
            gameView, viewManager);

        /* Removing potentiel old views */
        viewManager.removeView(ViewKeys.GAME_VIEW);
        viewManager.removeView(ViewKeys.PAUSE_VIEW);

        /* Adding new views */
        viewManager.addView(ViewKeys.GAME_VIEW, gameView);

        /* Adding pause menu */
        PauseView pauseView = new PauseView();
        new PauseController(pauseView, gameController, viewManager);
        viewManager.addView(ViewKeys.PAUSE_VIEW, pauseView);

        /* Showing game view and starting game */
        viewManager.showView(ViewKeys.GAME_VIEW);
        gameController.startGameLoop();
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
}
