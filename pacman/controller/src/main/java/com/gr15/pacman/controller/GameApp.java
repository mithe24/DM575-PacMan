package com.gr15.pacman.controller;

import java.io.InputStream;

import com.gr15.pacman.model.GameState;
import com.gr15.pacman.model.GameStateBuilder;
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

        InputStream inputStream = this.getClass()
            .getResourceAsStream("/testGameState.json");
        GameState gameState = GameStateBuilder.fromJson(inputStream);
        inputStream.close();
        int tileWidth = gameState.getBoard().getWidth();
        int tileHeight = gameState.getBoard().getHeight();
        gameView = new GameView(gameState,tileWidth, tileHeight);
        primaryStage.setScene(gameView);

        gameController = new GameController(gameState, gameView);
        gameController.startGameLoop();

        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
