package com.gr15.pacman.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Represents a sprite that can be drawn onto a {@link GraphicsContext}
 * in a specific position with a specified size and rotation.
 * This class is typically used for visual entities in the game.
 * 
 * <p>A sprite is an image that can be
 * rendered, rotated, and positioned on the screen.</p>
 */
public class Sprite {

    /** The {@link Image} of the sprite */
    private Image image;

    /** The X-coordinate of the sprite's top-left corner. */
    private double x;

    /** The Y-coordinate of the sprite's top-left corner. */
    private double y;

    /** The width of the sprite */
    private double width;

    /** The height of the sprite */
    private double height;

    /** The rotation of the sprite in degrees (0-360). */
    private double rotation = 0;

    /**
     * Constructs a new {@code Sprite} with the specified
     * image, position, size, and rotation.
     *
     * @param image the image to be used as the sprite's texture
     * @param x the X-coordinate of the sprite's top-left corner
     * @param y the Y-coordinate of the sprite's top-left corner
     * @param width the width of the sprite
     * @param height the height of the sprite
     * @throws IllegalArgumentException if image is {@code null} or if
     *      - x is less than 0
     *      - y is less than 0
     *      - width is less than 0
     *      - height is less than 0
     */
    public Sprite(Image image, double x, double y, double width, double height) {
        if (image == null) {
            throw new IllegalArgumentException("image must not be be null");
        }
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("x and y must be greater than or equal 0");
        }
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("width and height must be greater than or equal 0");
        }

        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Renders the sprite onto the provided {@link GraphicsContext}
     * with the current position, size, and rotation.
     * 
     * <p>The sprite will be drawn at its current position
     * with its current rotation and size.
     * The transformation for rotation is applied around the sprite's center.</p>
     *
     * @param gc the {@link GraphicsContext} to render the sprite to
     * @throws IllegalArgumentException if gc is {@code null}
     */
    public void render(GraphicsContext gc) {
        if (gc == null) {
            throw new IllegalArgumentException("gc must not be null");
        }
        /* Saving the current transformation */
        gc.save();

        /* Getting center of sprite */
        double centerX = x + width / 2;
        double centerY = y + height / 2;

        /* Center on sprite */
        gc.translate(centerX, centerY);

        /* Applying rotation */
        gc.rotate(rotation);

        /* Moving origin back before to orignal position */
        gc.translate(-centerX, -centerY);

        /* Drawing sprite */
        gc.drawImage(image, x, y, width, height);

        /* Restoring transformation */
        gc.restore();
    }

    /**
     * Returns the image used for this sprite.
     * 
     * @return the sprite's image
     */
    public Image getImage() { return this.image; }

    /**
     * Returns the X-coordinate of the sprite's position.
     * 
     * @return the X-coordinate
     */
    public double getX() { return this.x; }

    /**
     * Returns the Y-coordinate of the sprite's position.
     * 
     * @return the Y-coordinate
     */
    public double getY() { return this.y; }

    /**
     * Returns the width of the sprite.
     * 
     * @return the width of the sprite
     */
    public double getWidth() { return this.width; }

    /**
     * Returns the height of the sprite.
     * 
     * @return the height of the sprite
     */
    public double getHeight() { return this.height; }

    /**
     * Returns the current rotation of the sprite in degrees.
     * 
     * @return the rotation in degrees
     */
    public double getRotation() { return this.rotation; }

    /**
     * Sets the rotation of the sprite. The rotation is specified in degrees,
     * and should be between 0 and 360.
     * 
     * @param newRotation the new rotation in degrees
     * @throws IllegalArgumentException if the rotation is outside the range (0, 360)
     */
    public void setRotation(double newRotation) {
        this.rotation = newRotation;
    }

    /**
     * Sets a new image for the sprite.
     * 
     * @param newImage the new image to set
     * @throws IllegalArgumentException if newImage is {@code null}
     */
    public void setImage(Image newImage) {
        if (newImage == null) {
            throw new IllegalArgumentException("newImage must not be null");
        }
        this.image = newImage;
    }

    /**
     * Sets a new image for the sprite.
     * 
     * @param newX the new X-coordinate
     */
    public void setX(double newX) { this.x = newX; }

    /**
     * Sets a new Y-coordinate for the sprite's position.
     * 
     * @param newY the new Y-coordinate
     */
    public void setY(double newY) { this.y = newY; }

    /**
     * Sets a new width for the sprite.
     * 
     * @param newWidth the new width
     */
    public void setWidth(double newWidth) { this.width = newWidth; }

    /**
     * Sets a new height for the sprite.
     * 
     * @param newHeight the new height
     */
    public void setHeight(double newHeight) { this.height = newHeight; }
}
