package com.gr15.pacman.controller;

import com.gr15.pacman.model.GameConfigFactory;
import com.gr15.pacman.controller.screen.GameController;
import com.gr15.pacman.controller.screen.GameOverController;
import com.gr15.pacman.controller.screen.PauseController;
import com.gr15.pacman.controller.screen.YouWonController;
import com.gr15.pacman.model.GameConfig;
import com.gr15.pacman.model.GameState;
import com.gr15.pacman.view.ViewManager;
import com.gr15.pacman.view.ViewManager.ViewKeys;
import com.gr15.pacman.view.screen.GameOverView;
import com.gr15.pacman.view.screen.GameView;
import com.gr15.pacman.view.screen.PauseView;
import com.gr15.pacman.view.screen.YouWonView;

import java.io.InputStream;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A utility class that defines common application-level actions
 * used across controllers.
 * These actions are typically passed to UI event handlers (e.g., buttons, key presses).
 */
public final class AppAction {

    /** Manages the different views of the application. */
    private static final ViewManager VIEW_MANAGER = ViewManager.getInstance();

    /**
     * Action to immediately quit the application.
     */
    public static final Runnable QUIT = () -> {
        System.exit(0);
    };

    /**
     * Action to switch to the main menu view.
     */
    public static final Runnable MAIN_MENU = () -> {
        VIEW_MANAGER.showView(ViewKeys.MAIN_MENU_VIEW);
    };

    /**
     * Action to switch to winningview.
     *
     * @param score is the current score of type {@link Integer}.
     * @param gameController is the controller of the current game.
     */
    public static final BiConsumer<Integer, GameController> 
        YOU_WON = (score, gameController) -> {

        gameController.stopGameLoop();
        YouWonView youWonView = new YouWonView(score);
        VIEW_MANAGER.removeView(ViewKeys.YOU_WON_VIEW);
        VIEW_MANAGER.addView(ViewKeys.YOU_WON_VIEW, youWonView);
        VIEW_MANAGER.showView(ViewKeys.YOU_WON_VIEW);
        new YouWonController(youWonView, gameController);
    };

    /**
     * Action to switch to game over view.
     *
     * @param score is the current score of type {@link Integer}.
     * @param gameController is the controller of the current game.
     */
    public static final BiConsumer<Integer, GameController> 
        GAME_OVER = (score, gameController) -> {

        gameController.stopGameLoop();
        GameOverView gameOverView = new GameOverView(score);
        VIEW_MANAGER.removeView(ViewKeys.GAME_OVER_VIEW);
        VIEW_MANAGER.addView(ViewKeys.GAME_OVER_VIEW, gameOverView);
        VIEW_MANAGER.showView(ViewKeys.GAME_OVER_VIEW);
        new GameOverController(gameOverView, gameController);
    };

    /**
     * Action to switch to pause view.
     *
     * @param gameController is the controller of the current game.
     */
    public static final Consumer<GameController> PAUSE = (gameController) -> {
        gameController.stopGameLoop();
        PauseView pauseView = new PauseView();
        VIEW_MANAGER.removeView(ViewKeys.PAUSE_VIEW);
        VIEW_MANAGER.addView(ViewKeys.PAUSE_VIEW, pauseView);
        VIEW_MANAGER.showView(ViewKeys.PAUSE_VIEW);
        new PauseController(pauseView, gameController);
    };

    /**
     * Action to resume a paused game by
     * showing the game view and starting the game loop.
     *
     * @param gameController is the controller of the current game.
     */
    public static final Consumer<GameController> RESUME = (gameController) -> {
        VIEW_MANAGER.showView(ViewKeys.GAME_VIEW);
        gameController.startGameLoop();
    };

    /**
     * Action to start a new game using configuration from an InputStream.
     * It sets up the game state, view, and controller, and starts the game loop.
     *
     * @param input the {@link InputStream} to load game configuration from a JSON file
     */
    public static final Consumer<InputStream> NEW_GAME = (input) -> {
        GameConfig config = GameConfigFactory.fromJson(input);
        GameState gameState = new GameState(config);
        GameView gameView = new GameView(gameState);

        VIEW_MANAGER.removeView(ViewKeys.GAME_VIEW);
        VIEW_MANAGER.addView(ViewKeys.GAME_VIEW, gameView);
        VIEW_MANAGER.showView(ViewKeys.GAME_VIEW);
        GameController gameController = new GameController(gameState, gameView);
        gameController.startGameLoop();
    };

    /**
     * Action to reset the current game and start it again from the beginning.
     *
     * @param gameController is the controller of the current game.
     */
    public static final Consumer<GameController> PLAY_AGAIN = (gameController) -> {
        VIEW_MANAGER.showView(ViewKeys.GAME_VIEW);
        gameController.resetGame();
        gameController.startGameLoop();
    };
    
    /** Private constructor to prevent external instantiation */
    private AppAction() {}
}
