package com.gr15.pacman.controller.screen;

import com.gr15.pacman.model.GameState;
import com.gr15.pacman.model.GameStateBuilder;
import com.gr15.pacman.view.ViewManager;
import com.gr15.pacman.view.screen.GameView;
import com.gr15.pacman.view.screen.MainMenuView;
import com.gr15.pacman.view.screen.PauseView;

import javafx.event.ActionEvent;

import com.gr15.pacman.view.ViewManager.ViewKeys;

/**
 * The {@code MainMenuController} handles interactions on the main menu screen.
 * 
 * <p>This includes starting a new game, resuming an existing game,
 * or exiting the application.
 * It also manages setting up new
 * game views and controllers when a new game is launched.</p>
 */
public class MainMenuController {

    /** Manages switching between different views/screens in the application. */
    private final ViewManager viewManager;

    /** The main menu view UI. */
    private final MainMenuView mainMenuView;

    /** The active game view instance, created when starting or resuming a game. */
    private GameView gameView;

    /** The active game controller instance, used to control the game loop. */
    GameController gameController;

    /**
     * Constructs a new {@code MainMenuController} with the given
     * view manager and main menu view.
     *
     * @param viewManager the manager responsible for view navigation
     * @param mainMenuView the view representing the main menu UI
     */
    public MainMenuController(ViewManager viewManager, MainMenuView mainMenuView) {
        this.viewManager = viewManager;
        this.mainMenuView = mainMenuView;
        initializeButtons();
    }

    /**
     * Initializes event handlers for the main menu buttons.
     * 
     * <p>This includes setting actions for
     * starting a new game, resuming a game, and exiting.</p>
     */
    private void initializeButtons() {
        mainMenuView.getResumeButton().setDisable(true);
        mainMenuView.getNewGameButton().setOnAction(this::startNewGame);
        mainMenuView.getExitButton().setOnAction(this::exitGame);
        mainMenuView.getResumeButton().setOnAction(this::resumeGame);
    }

    /**
     * Handles the event when the "Resume" button is clicked.
     * 
     * <p>This resumes the game by switching back to the game view
     * and starting the game loop.</p>
     *
     * @param event the action event triggered by the button
     */
    private void resumeGame(ActionEvent event) {
        if (!viewManager.hasView(ViewKeys.GAME_VIEW) || gameController == null) {
            startNewGame(event);
        }

        viewManager.showView(ViewKeys.GAME_VIEW);
        gameController.startGameLoop();
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
        GameState gameState = GameStateBuilder.fromJson(
            this.getClass().getResourceAsStream("/testGameState.json"));

        /* Creating new views */
        gameView = new GameView(gameState);

        /* Creaing new controllers */
        gameController = new GameController(gameState, 
            gameView, viewManager);

        /* Removing potentiel old views */
        viewManager.removeView(ViewKeys.GAME_VIEW);
        viewManager.removeView(ViewKeys.PAUSE);

        /* Adding new views */
        viewManager.addView(ViewKeys.GAME_VIEW, gameView);

        /* Enabling resume button */
        mainMenuView.getResumeButton().setDisable(false);

        /* Adding pause menu */
        PauseView pauseView = new PauseView();
        new PauseController(pauseView, gameController, viewManager);
        viewManager.addView(ViewKeys.PAUSE, pauseView);

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
