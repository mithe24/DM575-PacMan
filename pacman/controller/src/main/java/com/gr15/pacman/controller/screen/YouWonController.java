package com.gr15.pacman.controller.screen;

import com.gr15.pacman.controller.AppAction;
import com.gr15.pacman.controller.HandlerFactory;
import com.gr15.pacman.view.screen.YouWonView;

/**
 * The {@code YouWonController} handles interactions on the main menu screen.
 */
public class YouWonController {

    /**
     * Constructs a new {@code GameOverController} with the given gameOverView.
     *
     * @param youWonView the view representing the you won UI
     * @param gameController the controller of the current game
     * @throws IllegalArgumentException if GameOverView is {@code null} or
     *      {@link GameOverController} is null.
     */
    public YouWonController(YouWonView youWonView, GameController gameController) {
        if (youWonView == null) {
            throw new IllegalArgumentException("youWonView must not be null");
        }
        if (gameController == null) {
            throw new IllegalArgumentException("gameController must not be null");
        }

        youWonView.getMainMenuButton().setOnAction(HandlerFactory.createHandler(
            AppAction.MAIN_MENU));
        youWonView.getPlayAgainButton().setOnAction(HandlerFactory.createHandler(
            () -> gameController,
            AppAction.PLAY_AGAIN));
    }
}
