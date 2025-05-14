package com.gr15.pacman.model.entities;

import com.gr15.pacman.model.Board;
import com.gr15.pacman.model.Position;

/**
 * Represents an abstract entity with a position
 * and collision radius on a game board.
 *
 * <p>
 * This class stores both tile-based {@link Position}
 * and fine-grained sub-tile coordinates
 * to allow smooth movement within and across grid tiles.
 * </p>
 *
 * <p>
 * The full world coordinates are computed by combining
 * the integer {@code Position} with
 * {@code subTileX} and {@code subTileY}, allowing sub-tile precision
 * useful for animation or collision detection.
 * </p>
 *
 * <p>
 * Subclasses must implement the {@code move} method
 * to define entity-specific behavior over time.
 * </p>
 */
public abstract class Entity {

    private Position position;
    private float subTileX = 0.5f;
    private float subTileY = 0.5f;

    private double radius;

    public enum Direction { UP, DOWN, LEFT, RIGHT, NONE };
    
    /**
     * Constructs an Entity with the specified starting position and radius.
     *
     * @param startPos The initial tile-based position of the entity.
     * @param radius The collision radius of the entity.
     * @throws IllegalArgumentException if any parameter is invalid:
     *         - startPos is null
     *         - radius is less than or equal to zero
     */
    public Entity(Position startPos, double radius) {
        if (startPos == null) { throw new IllegalArgumentException("startPos cannot be null"); }
        if (radius <= 0) { throw new IllegalArgumentException("radius must be positive"); }
        this.radius = radius;
        this.position = startPos;
    }

    /**
     * Updates the entity's position based on game logic and elapsed time.
     *
     * @param board The board the entity is moving on.
     * @param deltaSeconds The time elapsed since the last update, in seconds.
     */
    public abstract void move(Board board, double deltaSeconds);


                            /* Getters */
    /**
     * Gets the tile-based position of the entity.
     *
     * @return The current tile position.
     */
    public Position getPosition() { return this.position; }

    /**
     * Gets the entity's sub-tile X offset.
     *
     * @return The sub-tile X coordinate.
     */
    public float getSubTileX() { return this.subTileX; }

    /**
     * Gets the entity's sub-tile Y offset.
     *
     * @return The sub-tile Y coordinate.
     */
    public float getSubTileY() { return this.subTileY; }

    /**
     * Gets the radius of the entity.
     *
     * @return The collision radius.
     */
    public double getRadius() { return this.radius; }

    /**
     * Gets the full X coordinate, including sub-tile offset.
     *
     * @return The entity's X position as a double.
     */
    public double getPositionX() { return position.x() + subTileX; }

    /**
     * Gets the full Y coordinate, including sub-tile offset.
     *
     * @return The entity's Y position as a double.
     */
    public double getPositionY() { return position.y() + subTileY; }


                            /* Setters */
    /**
     * Sets the entity's tile-based position.
     *
     * @param newPos The new tile position to set.
     */
    public void setPosition(Position newPos) { this.position = newPos; }

    /**
     * Sets the entity's sub-tile X offset.
     *
     * @param newX The new sub-tile X coordinate.
     */
    public void setSubTileX(float newX) { this.subTileX = newX; }

    /**
     * Sets the entity's sub-tile Y offset.
     *
     * @param newY The new sub-tile Y coordinate.
     */
    public void setSubTileY(float newY) { this.subTileY = newY; }

    /**
     * Sets the entity's collision radius.
     *
     * @param newRad The new radius value.
     */
    public void setRadius(double newRad) { this.radius = newRad; }
}
