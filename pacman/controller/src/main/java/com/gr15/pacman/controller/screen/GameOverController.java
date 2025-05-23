package com.gr15.pacman.controller.screen;

import javafx.event.ActionEvent;

import com.gr15.pacman.view.ViewManager;
import com.gr15.pacman.view.ViewManager.ViewKeys;
import com.gr15.pacman.view.screen.GameOverView;

/**
 * GameOverController
 */
public class GameOverController {

    private GameOverView gameOverView;
    private ViewManager viewManager;

    public GameOverController(GameOverView gameOverView, ViewManager viewManager) {
        if (gameOverView == null) {
            throw new IllegalArgumentException("gameOverView must not be null");
        }
        if (viewManager == null) {
            throw new IllegalArgumentException("viewManager must not be null");
        }
        this.gameOverView = gameOverView;
        this.viewManager = viewManager;

        gameOverView.getMainMenuButton().setOnAction(this::mainMenu);
    }

    private void mainMenu(ActionEvent event) {
        viewManager.showView(ViewKeys.MAIN_MENU_VIEW);
    }
}
