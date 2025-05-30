package com.gr15.pacman.controller.screen;

import com.gr15.pacman.controller.AppAction;
import com.gr15.pacman.controller.HandlerFactory;
import com.gr15.pacman.view.screen.GameOverView;

/**
 * The {@code GameOverController} handles interactions on the main menu screen.
 */
public class GameOverController {

    /**
     * Constructs a new {@code GameOverController} with the given gameOverView.
     *
     * @param gameOverView the view representing the game over UI
     * @param gameController the controller of the current game
     * @throws IllegalArgumentException if GameOverView is {@code null} or
     *      {@link GameOverController} is null.
     */
    public GameOverController(GameOverView gameOverView, GameController gameController) {
        if (gameOverView == null) {
            throw new IllegalArgumentException("gameOverView must not be null");
        }
        if (gameController == null) {
            throw new IllegalArgumentException("gameController must not be null");
        }

        gameOverView.getMainMenuButton().setOnAction(HandlerFactory.createHandler(
            AppAction.MAIN_MENU));
        gameOverView.getPlayAgainButton().setOnAction(HandlerFactory.createHandler(
            () -> gameController,
            AppAction.PLAY_AGAIN));
    }
}
