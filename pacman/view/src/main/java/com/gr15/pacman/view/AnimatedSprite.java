package com.gr15.pacman.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Represents an animated sprite that cycles through multiple image frames over time.
 * 
 * <p> This class extends {@link Sprite} and adds support for animation by switching
 * between a sequence of images (frames) at a fixed interval (frame time).
 * The animation progresses by calling the {@link #update(double)} method
 * with the time delta since the last update. </p>
 * 
 * <p>The sprite is rendered using the current frame image when
 * {@link #render(GraphicsContext)} is called.</p>
 */
public class AnimatedSprite extends Sprite {

    /** The array of frames (images) used for the animation. */
    private Image[] frames;

    /** The index of the current frame in the animation. */
    private int currentFrame = 0;

    /** The duration (in seconds) each frame is shown. */
    private double frameTime = 0.5;

    /** The time (in seconds) since the last frame update. */
    private double timeSinceLastFrame = 0.0;

    /** Boolean to keep track if going forward or backwards through the array */
    private boolean forward = true;

    /**
     *
     * @param x the X-coordinate of the sprite's top-left corner
     * @param y the Y-coordinate of the sprite's top-left corner
     * @param width the width of the sprite
     * @param height the height of the sprite
     * @param frames the array of {@link Image} objects representing animation frames
     * @throws IllegalArgumentException if image is {@code null} or if
     *      - {@code frames} is null or empty
     *      - x is less than 0
     *      - y is less than 0
     *      - width is less than 0
     *      - height is less than 0
     */
    public AnimatedSprite(Image[] frames, double x, double y,
            double width, double height) {
        super(frames[0], x, y, width, height);
        if (frames == null || frames.length == 0) {
            throw new IllegalArgumentException(
                "Animation frames must not be null or empty");
        }
        this.frames = frames;
    }

    /**
     * Updates the animation using a ping-pong effect (forward and backward loop).
     * 
     * <p>If the accumulated time since the last frame exceeds the {@code frameTime},
     * the current frame index is incremented to show the next frame in the sequence.</p>
     * 
     * @param deltaSeconds the time in seconds since the last update call
     */
    public void update(double deltaSeconds) {
        timeSinceLastFrame += deltaSeconds;
        if (timeSinceLastFrame >= frameTime) {
            if (forward) {
                currentFrame++;
                if (currentFrame >= frames.length - 1) {
                    currentFrame = frames.length - 1;
                    forward = false;
                }
            } else {
                currentFrame--;
                if (currentFrame <= 0) {
                    currentFrame = 0;
                    forward = true;
                }
            }

            super.setImage(frames[currentFrame]);
            timeSinceLastFrame = 0.0;
        }
    }
}
