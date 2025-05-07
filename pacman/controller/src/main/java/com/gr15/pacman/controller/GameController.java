package com.gr15.pacman.controller;

import com.gr15.pacman.model.GameState;
import com.gr15.pacman.model.entities.Entity.Direction;
import com.gr15.pacman.view.GameView;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

/**
 * GameController
 */
public class GameController {

    private GameState gameState;
    private GameView gameView;

    private AnimationTimer gameLoop;
    private long lastUpdate = 0;

    public GameController(GameState gameState, GameView gameView) {
        this.gameState = gameState;
        this.gameView = gameView;
        gameView.setFill(Color.BLACK);

        setupEventHandlers();
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

    private void setupEventHandlers() {
        gameView.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> gameState.getPacman().setDirection(Direction.UP);
                case DOWN -> gameState.getPacman().setDirection(Direction.DOWN);
                case LEFT -> gameState.getPacman().setDirection(Direction.LEFT);
                case RIGHT -> gameState.getPacman().setDirection(Direction.RIGHT);
                default -> {}
            }
        });
    }

    public void startGameLoop() {
        gameLoop.start();
    }

    public void stopGameLoop() {
        lastUpdate = 0;
        gameLoop.stop();
    }
}
