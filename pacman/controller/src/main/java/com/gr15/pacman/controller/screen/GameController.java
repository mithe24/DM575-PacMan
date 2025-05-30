package com.gr15.pacman.controller.screen;

import java.util.Map;

import com.gr15.pacman.controller.AppAction;
import com.gr15.pacman.controller.HandlerFactory;
import com.gr15.pacman.model.GameState;
import com.gr15.pacman.model.entities.Entity.Direction;
import com.gr15.pacman.view.screen.GameView;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

/**
 * The {@code GameController} class manages the core game loop,
 * user input handling, and interaction between the game state and the view.
 * 
 * <p> It uses a JavaFX {@link AnimationTimer} to continuously update
 * the game state and render the game view at regular intervals. </p>
 */
public class GameController {

    /** Reference to the current game state. */
    private final GameState gameState;

    /** The main game loop. */
    private final AnimationTimer gameLoop;

    /** Timestamp of the last update, used to calculate elapsed time. */
    private long lastUpdate = 0;

    /**
     * Constructs a new {@code GameController} with the specified game state,
     * game view, and view manager.
     *
     * @param gameState the state of the game
     * @param gameView the view that renders the game
     * @throws IllegalArgumentException if GameState or gameView is {@code null}
     */
    public GameController(GameState gameState, GameView gameView) {
        if (gameState == null) {
            throw new IllegalArgumentException("gameState must not be null");
        }
        if (gameView == null) {
            throw new IllegalArgumentException("gameView must not be null");
        }

        this.gameState = gameState;

        Map<KeyCode, Runnable> keyBindings = Map.of(
            KeyCode.UP, () -> gameState.getPacman().setDirection(Direction.UP),
            KeyCode.DOWN, () -> gameState.getPacman().setDirection(Direction.DOWN),
            KeyCode.LEFT, () -> gameState.getPacman().setDirection(Direction.LEFT),
            KeyCode.RIGHT, () -> gameState.getPacman().setDirection(Direction.RIGHT),
            KeyCode.PAGE_UP, () -> gameView.changeZoom(0.1),
            KeyCode.PAGE_DOWN, () -> gameView.changeZoom(-0.1),
            KeyCode.ESCAPE, () -> AppAction.PAUSE.accept(GameController.this)
        );
        gameView.setOnKeyPressed(HandlerFactory.createKeyHandler(keyBindings));

        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return; /* returning early, since no time have elapsed */
                }

                if (gameState.isWon()) {
                    AppAction.YOU_WON.accept(
                        (Integer)gameState.getScore(), GameController.this);
                } else if(gameState.gameOver()) {
                    AppAction.GAME_OVER.accept(
                        (Integer)gameState.getScore(), GameController.this);
                }

                double deltaSeconds = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                gameState.update(deltaSeconds);
                gameView.renderGame(deltaSeconds);
            }
        };
    }

    /**
     * Resets game to initial state.
     */
    public void resetGame() {
        gameState.resetGame();
    }

    /**
     * Starts the game loop, beginning the update and render cycle.
     */
    public void startGameLoop() {
        gameLoop.start();
    }

    /**
     * Stops the game loop.
     */
    public void stopGameLoop() {
        lastUpdate = 0;
        gameLoop.stop();
    }
}
