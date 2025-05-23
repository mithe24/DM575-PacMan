package com.gr15.pacman.controller.screen;

import com.gr15.pacman.model.GameState;
import com.gr15.pacman.model.entities.Entity.Direction;
import com.gr15.pacman.view.ViewManager;
import com.gr15.pacman.view.screen.GameOverView;
import com.gr15.pacman.view.screen.GameView;
import com.gr15.pacman.view.screen.YouWonView;
import com.gr15.pacman.view.ViewManager.ViewKeys;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyEvent;

/**
 * The {@code GameController} class manages the core game loop,
 * user input handling, and interaction between the game state,
 * view, and view manager.
 * 
 * <p>It uses a JavaFX {@link AnimationTimer} to continuously update
 * the game state and render the game view at regular intervals.</p>
 */
public class GameController {

    /** Manages switching between different views/screens. */
    private final ViewManager viewManager;

    /** Reference to the current game state. */
    private final GameState gameState;

    /** The view responsible for rendering the game. */
    private final GameView gameView;

    /** The main game loop running. */
    private final AnimationTimer gameLoop;

    /** Timestamp of the last update, used to calculate elapsed time. */
    private long lastUpdate = 0;

    /**
     * Constructs a new {@code GameController} with the specified game state,
     * game view, and view manager.
     *
     * @param gameState the state of the game
     * @param gameView the view that renders the game
     * @param viewManager the manager responsible for switching views
     * @throws IllegalArgumentException if GameState, gameView or viewManager is {@code null}
     */
    public GameController(GameState gameState, GameView gameView,
            ViewManager viewManager) {
        if (gameState == null) {
            throw new IllegalArgumentException("gameState must not be null");
        }
        if (gameView == null) {
            throw new IllegalArgumentException("gameView must not be null");
        }
        if (viewManager == null) {
            throw new IllegalArgumentException("viewManager must not be null");
        }
        this.viewManager = viewManager;
        this.gameState = gameState;
        this.gameView = gameView;

        /* Removing potential unrelated views */
        viewManager.removeView(ViewKeys.GAME_OVER_VIEW);
        viewManager.removeView(ViewKeys.YOU_WON_VIEW);

        gameView.setOnKeyPressed(this::handleKeyEvent);

        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return; /* returning early, since no time have elapsed */
                }

                if (gameState.isWon()) {
                    YouWonView youWonView = new YouWonView(gameState.getScore());
                    viewManager.addView(ViewKeys.YOU_WON_VIEW, youWonView);
                    viewManager.showView(ViewKeys.YOU_WON_VIEW);
                    new YouWonController(youWonView, viewManager);
                    stopGameLoop();
                } else if(gameState.GameOver()) {
                    GameOverView gameOverView = new GameOverView(gameState.getScore());
                    viewManager.addView(ViewKeys.GAME_OVER_VIEW, gameOverView);
                    viewManager.showView(ViewKeys.GAME_OVER_VIEW);
                    new GameOverController(gameOverView, viewManager);
                    stopGameLoop();
                }

                double deltaSeconds = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                gameState.update(deltaSeconds);
                gameView.renderGame(deltaSeconds);
            }
        };
    }

    /**
     * Handles keyboard input for controlling the game, including
     * Pac-Man's movement and other interactions like zoom or pausing.
     *
     * @param event the key event triggered by user input
     */
    private void handleKeyEvent(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> gameState.getPacman().setDirection(Direction.UP);
            case DOWN -> gameState.getPacman().setDirection(Direction.DOWN);
            case LEFT -> gameState.getPacman().setDirection(Direction.LEFT);
            case RIGHT -> gameState.getPacman().setDirection(Direction.RIGHT);
            case PAGE_UP -> gameView.changeZoom(0.1);
            case PAGE_DOWN -> gameView.changeZoom(-0.1);
            case ESCAPE -> {
                viewManager.showView(ViewKeys.PAUSE_VIEW);
                stopGameLoop();
            }
            default -> {}
        }
    }

    /**
     * Starts the game loop, beginning the update and render cycle.
     */
    public void startGameLoop() {
        gameLoop.start();
    }

    /**
     * Stops the game loop and resets the last update timestamp.
     */
    public void stopGameLoop() {
        lastUpdate = 0;
        gameLoop.stop();
    }
}
