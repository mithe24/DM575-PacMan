package com.gr15.pacman.controller;

import com.gr15.pacman.model.GameConfigBuilder;
import com.gr15.pacman.controller.screen.GameController;
import com.gr15.pacman.model.GameConfig;
import com.gr15.pacman.model.GameState;
import com.gr15.pacman.view.ViewManager;
import com.gr15.pacman.view.ViewManager.ViewKeys;
import com.gr15.pacman.view.screen.GameView;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * A utility class that defines common application-level actions
 * used across views and controllers.
 * These actions are typically passed to UI event handlers (e.g., buttons, key presses).
 */
public final class AppAction {

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
     * Action to resume a paused game by
     * showing the game view and starting the game loop.
     *
     * @param gameController the current game controller instance
     */
    public static final Consumer<GameController> RESUME = (gameController) -> {
        VIEW_MANAGER.showView(ViewKeys.GAME_VIEW);
        gameController.startGameLoop();
    };

    /**
     * Action to start a new game using configuration from an InputStream.
     * It sets up the game state, view, and controller, and starts the game loop.
     *
     * @param input the input stream to load game configuration from a JSON file
     */
    public static final Consumer<InputStream> NEW_GAME = (input) -> {
        GameConfig config = GameConfigBuilder.fromJson(input);
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
     * @param gameController the current game controller instance
     */
    public static final Consumer<GameController> PLAY_AGAIN = (gameController) -> {
        VIEW_MANAGER.showView(ViewKeys.GAME_VIEW);
        gameController.resetGame();
        gameController.startGameLoop();
    };
    
    /** Private constructor to prevent external instantiation */
    private AppAction() {}
}
