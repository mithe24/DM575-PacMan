package com.gr15.pacman.view.screen;

import javafx.scene.Parent;

/**
 * An abstract base class for all views managed by the {@link ViewManager}.
 * Each view must implement the lifecycle methods {@code onEnter} and {@code onExit}.
 */
public abstract class BaseView
    extends Parent {
    
    /**
     * Called when the view becomes visible and is added to the scene.
     * Implement this method to initialize or refresh the view content.
     */
    public abstract void onEnter();

    /**
     * Called when the view is about to be removed from the scene.
     * Implement this method to handle any cleanup or state-saving tasks.
     */
    public abstract void onExit();
}
