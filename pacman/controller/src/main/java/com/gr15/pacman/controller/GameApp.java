package com.gr15.pacman.controller;

import com.gr15.pacman.model.GameState;
import com.gr15.pacman.model.GameStateBuilder;
import com.gr15.pacman.view.ViewManager;
import com.gr15.pacman.view.screen.GameView;
import com.gr15.pacman.view.screen.MainMenuView;
import com.gr15.pacman.view.screen.PauseView;
import com.gr15.pacman.view.ViewManager.ViewKeys;
import com.gr15.pacman.controller.screen.GameController;
import com.gr15.pacman.controller.screen.MainMenuController;
import com.gr15.pacman.controller.screen.PauseController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * GameApp
 */
public class GameApp
    extends Application {

    private ViewManager viewManager = new ViewManager();
    private MainMenuView mainMenuView = new MainMenuView();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Pac-Man");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(true);

        viewManager.addView(ViewKeys.MAIN_MENU, mainMenuView);
        viewManager.showView(ViewKeys.MAIN_MENU);
        new MainMenuController(viewManager, mainMenuView);

        Scene scene = new Scene(viewManager, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
