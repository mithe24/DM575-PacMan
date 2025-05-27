package com.gr15.pacman.controller.screen;

import com.gr15.pacman.controller.AppAction;
import com.gr15.pacman.controller.HandlerFactory;
import com.gr15.pacman.view.screen.YouWonView;

/**
 * YouWonController
 */
public class YouWonController {

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
