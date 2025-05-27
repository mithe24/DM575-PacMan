package com.gr15.pacman.controller.screen;

import com.gr15.pacman.controller.AppAction;
import com.gr15.pacman.controller.HandlerFactory;
import com.gr15.pacman.view.screen.PauseView;

import javafx.scene.input.KeyCode;

/**
 * The {@code PauseController} handles user interactions on the game's pause screen.
 * 
 * <p>This includes resuming the game, quitting to the main menu,
 * or exiting the application entirely.
 * It also manages key input while the pause view is active,
 * such as using the ESC key to resume the game.</p>
 */
public class PauseController {

    /**
     * Constructs a new {@code PauseController} with the specified
     * view, game controller, and view manager.
     *
     * @param pauseView the pause screen UI
     * @param gameController the game controller to resume or control the game state
     * @throws IllegalArgumentException if pauseView or gameController is {@code null}
     */
    public PauseController(PauseView pauseView, GameController gameController) {
        if (pauseView == null) {
            throw new IllegalArgumentException("pauseView must not be null");
        }
        if (gameController == null) {
            throw new IllegalArgumentException("gameController must not be null");
        }

        pauseView.getQuitButton().setOnAction(HandlerFactory.createHandler(
            AppAction.QUIT));
        pauseView.getMainMenuButton().setOnAction(HandlerFactory.createHandler(
            AppAction.MAIN_MENU));
        pauseView.getResumeButton().setOnAction(HandlerFactory.createHandler(
            () -> gameController, AppAction.RESUME));
        pauseView.setOnKeyPressed(HandlerFactory.createKeyHandler(
            KeyCode.ESCAPE, () -> gameController, AppAction.RESUME));
    }
}
