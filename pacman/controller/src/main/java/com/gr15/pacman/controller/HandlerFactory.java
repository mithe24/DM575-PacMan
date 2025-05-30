package com.gr15.pacman.controller;

import java.util.Map;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Utility class for creating event handlers used in JavaFX applications.
 * This class provides factory methods to simplify the creation
 * of action and key event handlers.
 * All methods are static and this class cannot be instantiated.
 */
public final class HandlerFactory {

    /**
     * Creates an {@link EventHandler} for {@link ActionEvent}
     * that simply runs the given action.
     *
     * @param action the action to run when the event is triggered
     * @return the event handler
     */
    public static EventHandler<ActionEvent> createHandler(Runnable action) {
        return event -> action.run();
    }

    /**
     * Creates an {@link EventHandler} for {@link ActionEvent}
     * that uses a supplier to get input,
     * and then passes it to a {@link Consumer} action.
     *
     * @param inputSupplier the supplier that provides input when the event occurs
     * @param action the consumer that performs an action using the input
     * @param T the type of input supplied
     * @return the event handler
     */
    public static <T> EventHandler<ActionEvent> createHandler(
        Supplier<T> inputSupplier, Consumer<T> action) {

        return event -> {
            T input = inputSupplier.get();
            action.accept(input);
        };
    }

    /**
     * Creates an {@link EventHandler} for {@link KeyEvent} that triggers
     * the given action only when the specified {@link KeyCode} is pressed.
     *
     * @param matchKey the key code to match
     * @param action the action to run when the key is pressed
     * @return the key event handler
     */
    public static EventHandler<KeyEvent> createKeyHandler(
        KeyCode matchKey, Runnable action) {
        return event -> {
            if (event.getCode() == matchKey) {
                action.run();
            }
        };
    }

    /**
     * Creates an {@link EventHandler} for {@link KeyEvent}
     * that uses a supplier to get input, and passes it to a consumer action
     * only when the specified {@link KeyCode} is pressed.
     *
     * @param matchKey the key code to match
     * @param inputSupplier the supplier that provides input when the event occurs
     * @param action the consumer that performs an action using the input
     * @param T the type of input supplied
     * @return the key event handler
     */
    public static <T> EventHandler<KeyEvent> createKeyHandler(
        KeyCode matchKey, Supplier<T> inputSupplier, Consumer<T> action) {
        return event -> {
            if (event.getCode() == matchKey) {
                action.accept(inputSupplier.get());
            }
        };
    }

    /**
     * Creates an {@link EventHandler} for {@link KeyEvent}
     * that looks up a {@link Runnable} from the provided map
     * based on the pressed {@link KeyCode}, and runs it if found.
     *
     * @param actions a map of key codes to runnable actions
     * @return the key event handler
     */
    public static EventHandler<KeyEvent> createKeyHandler(
        Map<KeyCode, Runnable> actions) {
        return event -> {
            Runnable action = actions.get(event.getCode());
            if (action != null) {
                action.run();
            }
        };
    }

    /** Private constructor to prevent external instantiation */
    private HandlerFactory() {}
}
