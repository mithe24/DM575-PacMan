package com.gr15.pacman.view;

import java.util.HashMap;
import java.util.Map;

import com.gr15.pacman.view.screen.BaseView;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Manages multiple views in a JavaFX application by allowing switching between
 * them using unique keys. Each view is expected to extend {@link BaseView}.
 */
public class ViewManager {

    /** The root container holding the currently displayed view. */
    private StackPane rootPane = new StackPane();

    /** The currently active view. */
    private BaseView currentView;

    /**
     * Enumeration of all possible view keys used to identify different views.
     */
    public enum ViewKeys { MAIN_MENU, GAME_VIEW, PAUSE };

    /**
     * A map that stores views associated with their corresponding keys.
     */
    private Map<ViewKeys, BaseView> views = new HashMap<>();

    /**
     * Constructs a new {@code ViewManager} with a black background.
     */
    public ViewManager() {
        rootPane.setBackground(new Background(new BackgroundFill(
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
    public void addView(ViewKeys key, BaseView view) {
        if (views.containsKey(key)) {
            throw new IllegalArgumentException(
                "View with key " + key + " already exists.");
        } else {
            views.put(key, view);
        }
    }

    /**
     * Displays the view associated with the specified key.
     * Calls {@code onExit} on the current view (if any) and {@code onEnter} on the new view.
     *
     * @param key the key of the view to be shown.
     * @throws IllegalArgumentException if no view exists for the specified key.
     */
    public void showView(ViewKeys key) {
        if (!views.containsKey(key)) {
            throw new IllegalArgumentException(
                "No view with key " + key + " exists.");
        }

        if (currentView != null) {
            currentView.onExit();
            rootPane.getChildren().remove(currentView);
        }

        currentView = views.get(key);
        rootPane.getChildren().add(currentView);
        currentView.onEnter();
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

    /**
     * Returns the root node that contains the currently active view.
     * This root can be added to the JavaFX scene graph.
     *
     * @return the root {@code Parent} node.
     */
    public Parent getRoot() {
        return rootPane;
    }
}
