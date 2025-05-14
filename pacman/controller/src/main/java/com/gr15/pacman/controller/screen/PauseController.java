package com.gr15.pacman.controller.screen;

import com.gr15.pacman.view.ViewManager;
import com.gr15.pacman.view.ViewManager.ViewKeys;
import com.gr15.pacman.view.screen.PauseView;

/**
 * PauseController
 */
public class PauseController {

    private final PauseView pauseView;
    private final GameController gameController;
    private final ViewManager viewManager;

    public PauseController(PauseView pauseView, GameController gameController,
        ViewManager viewManager) {
        this.gameController = gameController;
        this.pauseView = pauseView;
        this.viewManager = viewManager;
        initialize();
    }

    private void initialize() {
        pauseView.getQuitButton().setOnAction((e) -> { System.exit(0); });
        pauseView.getMainMenuButton().setOnAction((e) -> {
            viewManager.showView(ViewKeys.MAIN_MENU);
        });
        pauseView.getResumeButton().setOnAction((e) -> {
            viewManager.showView(ViewKeys.GAME_VIEW);
            gameController.startGameLoop();
        });
    }
}
