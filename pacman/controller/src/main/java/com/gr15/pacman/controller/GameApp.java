package com.gr15.pacman.controller;

import com.gr15.pacman.model.GameState;
import com.gr15.pacman.model.JsonParser;
import com.gr15.pacman.view.GameView;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * GameApp
 */
public class GameApp
    extends Application {

    GameController gameController;
    GameView gameView;
    GameState gameState;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Pac-Man");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(true);

        try {
            gameState = JsonParser.getGameState("test");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load game state: " + e.getMessage());
        }

        gameView = new GameView(gameState, 8, 5);
        primaryStage.setScene(gameView);

        gameController = new GameController(gameState, gameView);
        gameController.startGameLoop();

        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
