package com.gr15.pacman.controller.screen;

import com.gr15.pacman.model.GameState;
import com.gr15.pacman.model.GameStateBuilder;
import com.gr15.pacman.view.ViewManager;
import com.gr15.pacman.view.screen.GameView;
import com.gr15.pacman.view.screen.MainMenuView;
import com.gr15.pacman.view.screen.PauseView;
import com.gr15.pacman.view.ViewManager.ViewKeys;

/**
 * MainMenuController
 */
public class MainMenuController {

    private final ViewManager viewManager;
    private final MainMenuView mainMenuView;

    public MainMenuController(ViewManager viewManager, MainMenuView mainMenuView) {
        this.viewManager = viewManager;
        this.mainMenuView = mainMenuView;
        initializeButtons();
    }

    private void initializeButtons() {
        mainMenuView.getNewGameButton().setOnAction((event) -> {
            GameState gameState = GameStateBuilder.fromJson(
                this.getClass().getResourceAsStream("/testGameState.json"));
            int tileWidth = gameState.getBoard().getWidth();
            int tileHeight = gameState.getBoard().getHeight();
            GameView gameView = new GameView(gameState, tileWidth, tileHeight);
            PauseView pauseView = new PauseView();
            GameController gameController = new GameController(gameState, 
                gameView, viewManager);
            new PauseController(pauseView, gameController, viewManager);
            viewManager.removeView(ViewKeys.GAME_VIEW);
            viewManager.removeView(ViewKeys.PAUSE);
            viewManager.addView(ViewKeys.GAME_VIEW, gameView);
            viewManager.addView(ViewKeys.PAUSE, pauseView);
            viewManager.showView(ViewKeys.GAME_VIEW);
            gameController.startGameLoop();
        });
        mainMenuView.getQuitButton().setOnAction((event) -> {
            System.exit(0);
        });
    }
}
