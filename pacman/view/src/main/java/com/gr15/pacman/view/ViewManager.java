package com.gr15.pacman.view;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * The {@code ViewManager} class is responsible for managing and switching between 
 * different views in a JavaFX application. It extends {@code StackPane} and provides
 * methods to add, remove, check for, and display views identified by an enum key.
 */
public class ViewManager 
    extends StackPane {

    /**
     * Enumeration of all possible view keys used to identify different views.
     */
    public enum ViewKeys { MAIN_MENU, GAME_VIEW, PAUSE };

    /**
     * A map that stores views associated with their corresponding keys.
     */
    private Map<ViewKeys, Parent> views = new HashMap<>();

    /**
     * Constructs a new {@code ViewManager} with a black background.
     */
    public ViewManager() {
        setBackground(new Background(new BackgroundFill(
            Color.BLACK,
            CornerRadii.EMPTY,
            Insets.EMPTY
        )));
    }

    /**
     * Adds a new view to the manager.
     *
     * @param key  the key to identify the view.
     * @param view the view to be added.
     * @throws IllegalArgumentException if a view with the same key already exists.
     */
    public void addView(ViewKeys key, Parent view) {
        if (views.containsKey(key)) {
            throw new IllegalArgumentException(
                "View with key " + key + " already exists.");
        } else {
            views.put(key, view);
        }
    }

    /**
     * Displays the view associated with the specified key.
     *
     * @param key the key of the view to be shown.
     * @throws IllegalArgumentException if no view exists for the specified key.
     */
    public void showView(ViewKeys key) {
        if (!views.containsKey(key)) {
            throw new IllegalArgumentException(
                "No view with key " + key + " exists.");
        } else {
            getChildren().setAll(views.get(key));
        }
    }

    /**
     * Checks whether a view exists for the specified key.
     *
     * @param key the key to check.
     * @return {@code true} if a view with the key exists, {@code false} otherwise.
     */
    public boolean hasView(ViewKeys key) {
        return views.containsKey(key);
    }

    /**
     * Removes the view associated with the specified key.
     *
     * @param key the key of the view to remove.
     */
    public void removeView(ViewKeys key) {
        views.remove(key);
    }
}
