package com.gr15.pacman.controller.screen;

import com.gr15.pacman.model.GameState;
import com.gr15.pacman.model.entities.Entity.Direction;
import com.gr15.pacman.view.ViewManager;
import com.gr15.pacman.view.screen.GameView;
import com.gr15.pacman.view.ViewManager.ViewKeys;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyEvent;

/**
 * GameController
 */
public class GameController {

    private final ViewManager viewManager;
    private final GameState gameState;
    private final GameView gameView;

    private final AnimationTimer gameLoop;
    private long lastUpdate = 0;

    public GameController(GameState gameState, GameView gameView, ViewManager viewManager) {
        this.viewManager = viewManager;
        this.gameState = gameState;
        this.gameView = gameView;

        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return; /* returning early, since no time have elapsed */
                }
                
                double deltaSeconds = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                gameState.update(deltaSeconds);
                gameView.renderGame(deltaSeconds);
            }
        };
    }

    public void handleKeyEvent(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> gameState.getPacman().setDirection(Direction.UP);
            case DOWN -> gameState.getPacman().setDirection(Direction.DOWN);
            case LEFT -> gameState.getPacman().setDirection(Direction.LEFT);
            case RIGHT -> gameState.getPacman().setDirection(Direction.RIGHT);
            case ESCAPE -> {
                viewManager.showView(ViewKeys.PAUSE);
                stopGameLoop();
            }
            default -> {}
        }
    }

    public void startGameLoop() {
        gameView.requestFocusForInput();
        gameView.setOnKeyPressed(event -> handleKeyEvent(event));

        gameLoop.start();
    }

    public void stopGameLoop() {
        lastUpdate = 0;
        gameLoop.stop();
    }
}
