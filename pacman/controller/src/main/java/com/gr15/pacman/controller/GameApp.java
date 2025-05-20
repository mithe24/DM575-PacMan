package com.gr15.pacman.controller;

import com.gr15.pacman.view.ViewManager;
import com.gr15.pacman.view.screen.MainMenuView;
import com.gr15.pacman.view.ViewManager.ViewKeys;
import com.gr15.pacman.controller.screen.MainMenuController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The {@code GameApp} class is the entry point of the Pac-Man application.
 * 
 * <p>It sets up the primary JavaFX stage, initializes the main menu view,
 * and connects it to the corresponding controller. It also manages the 
 * view system using {@link ViewManager} and sets the application window properties.</p>
 * 
 * <p>This class extends {@link javafx.application.Application} and uses 
 * JavaFX's lifecycle methods.</p>
 */
public class GameApp
    extends Application {

    /** Manages the different views (scenes) of the application. */
    private ViewManager viewManager = new ViewManager();

    /** The main menu view shown when the application starts. */
    private MainMenuView mainMenuView = new MainMenuView();

    /**
     * Starts the JavaFX application and initializes the primary stage.
     *
     * <p>This method sets up the window properties, adds the main menu view
     * to the {@link ViewManager}, and creates the {@link MainMenuController}
     * to handle user interaction.</p>
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs during startup
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        /* window properties */
        primaryStage.setTitle("Pac-Man");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(true);

        /* Adding main menu, and instantiate controller */
        viewManager.addView(ViewKeys.MAIN_MENU, mainMenuView);
        viewManager.showView(ViewKeys.MAIN_MENU);
        new MainMenuController(viewManager, mainMenuView);

        Scene scene = new Scene(viewManager.getRoot(), 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args the command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
