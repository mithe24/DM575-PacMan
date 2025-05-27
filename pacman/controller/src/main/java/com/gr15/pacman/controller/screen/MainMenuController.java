package com.gr15.pacman.controller.screen;

import com.gr15.pacman.controller.AppAction;
import com.gr15.pacman.controller.HandlerFactory;
import com.gr15.pacman.view.screen.MainMenuView;

/**
 * The {@code MainMenuController} handles interactions on the main menu screen.
 */
public class MainMenuController {

    /**
     * Constructs a new {@code MainMenuController} with the given
     * view manager and main menu view.
     *
     * @param mainMenuView the view representing the main menu UI
     * @throws IllegalArgumentException if mainMenuView is {@code null}
     */
    public MainMenuController(MainMenuView mainMenuView) {
        if (mainMenuView == null) {
            throw new IllegalArgumentException("mainMenuView must not be null");
        }

        mainMenuView.getNewGameButton().setOnAction(HandlerFactory.createHandler(
            () -> this.getClass().getResourceAsStream("/exampleConfig.json"),
            AppAction.NEW_GAME));
        mainMenuView.getExitButton().setOnAction(HandlerFactory.createHandler(
            AppAction.QUIT));
    }
}
