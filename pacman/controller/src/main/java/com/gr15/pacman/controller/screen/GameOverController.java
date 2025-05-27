package com.gr15.pacman.controller.screen;

import com.gr15.pacman.controller.AppAction;
import com.gr15.pacman.controller.HandlerFactory;
import com.gr15.pacman.view.screen.GameOverView;

/**
 * GameOverController
 */
public class GameOverController {

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
