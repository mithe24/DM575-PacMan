package com.gr15.pacman.view;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

/**
 * The {@code ResourceManager} class is a singleton responsible
 * for managing {@link Image} resources used in the application.
 * It provides caching to avoid loading the same {@link Image} multiple times,
 * improving performance and resource efficiency.
 *
 * If an {@link Image} cannot be found at the specified path,
 * a default "missing texture" {@link Image} is returned.
 */

public class ResourceManager {

    /** Singleton instance of ResourceManager. */
    private static ResourceManager instance;

    /** Cache to store loaded images keyed by their file path. */
    private Map<String, Image> resources = new HashMap<>();

    /** Image to be used when the requested image is not found. */
    private Image missingTexture = new Image(
        this.getClass().getResourceAsStream("/gameAssets/missingTexture.png"));

    /**
     * Private constructor to prevent external instantiation
     * and enforce singleton pattern.
     */
    private ResourceManager() {}

    /**
     * Returns the singleton instance of {@code ResourceManager}.
     * If it does not exist yet, it is created.
     *
     * @return the singleton instance of {@code ResourceManager}.
     */
    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }

        return instance;
    }

    /**
     * Returns an {@link Image} corresponding to the specified resource path.
     * If the {@link Image} has already been loaded before,
     * it is returned from the cache. Otherwise, it is loaded
     * and added to the cache.
     * If loading fails (e.g., if the file does not exist),
     * the default "missing texture" {@link Image} is returned.
     *
     * @param path the path to the image resource, relative to the classpath
     * @return the loaded {@link Image}, or a default image if the resource could not be found
     * @throws IllegalArgumentException if path is {@code null}
     */
    public Image getTexture(String path) {
        if (path == null) {
            throw new IllegalArgumentException("path must not be null");
        }
        if (resources.containsKey(path)) {
            return resources.get(path);
        } else {
            try {
                Image texture = new Image(
                    this.getClass().getResourceAsStream(path));
                resources.put(path, texture);
                return texture;
            } catch (NullPointerException e) {
                return missingTexture;
            }
        }
    }
}
