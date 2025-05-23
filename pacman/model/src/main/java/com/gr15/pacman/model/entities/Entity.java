package com.gr15.pacman.model.entities;

import com.gr15.pacman.model.Board;
import com.gr15.pacman.model.Position;

/**
 * Represents an abstract entity with a position
 * and collision radius on a game board.
 *
 * <p> This class stores both tile-based {@link Position}
 * and fine-grained sub-tile coordinates
 * to allow smooth movement within and across grid tiles. </p>
 *
 * <p> The full world coordinates are computed by combining
 * the integer {@code Position} with
 * {@code subTileX} and {@code subTileY}, allowing sub-tile precision
 * useful for animation or collision detection. </p>
 *
 * <p> Handles movement logic including direction changes, wall collisions,
 * snapping to tile centers, and updating position on the game board. </p>
 */
public abstract class Entity {

    private Position position;
    private float subTileX = 0.5f;
    private float subTileY = 0.5f;

    private double radius;

    private Direction currentDirection = Direction.UP;
    private Direction nextDirection = Direction.NONE;
    private double speed;

    public enum Direction { UP, DOWN, LEFT, RIGHT, NONE };
    
    /**
     * Constructs an Entity with the specified starting position and radius.
     *
     * @param startPos The initial tile-based position of the entity.
     * @param radius The collision radius of the entity.
     * @throws IllegalArgumentException if any parameter is invalid:
     *         - startPos is null
     *         - radius is less than or equal to zero
     *         - speed is less than zero
     */
    public Entity(Position startPos, double radius, double speed) {
        if (startPos == null) {
            throw new IllegalArgumentException("startPos cannot be null");
        }
        if (radius <= 0) {
            throw new IllegalArgumentException("radius must be positive");
        }
        if (speed < 0) {
            throw new IllegalArgumentException("Speed must be a positive number");
        }
        this.radius = radius;
        this.position = startPos;
        this.speed = speed;
    }

    /**
     * Updates Pacman's {@link Position} based on the time elapsed
     * and the game {@link Board} state.
     * Handles turning, snapping to tile center, 
     * and tile boundary transitions.
     *
     * @param board the game {@link Board}, used to determine valid movement.
     * @param deltaSeconds time in seconds since the last update in seconds.
     * @throws IllegalArgumentException if board is {@code null}
     */
    public void move(Board board, double deltaSeconds) {
        if (board == null) {
            throw new IllegalArgumentException("board must not be null");
        }
        double distanceToMove = speed * deltaSeconds;

        boolean canMoveNext = canMove(board, position, nextDirection);
        boolean canContinue = canMove(board, position, currentDirection);

        Direction direction = decideDirection(canMoveNext, canContinue);
        if (direction == Direction.NONE) {
            snapToCenter(distanceToMove);
            return; /* Returning early, since no more updating required */
        }

        boolean verticalMove = isVertical(direction);
        int directionSign = getDirectionSign(direction);

        double subPrimary = verticalMove ? subTileY : subTileX;
        double subSecondary = verticalMove ? subTileX : subTileY;

        double center = Math.floor(subSecondary) + 0.5;
        double distanceToCenter = Math.abs(subSecondary - center);

        /* Snap to secondary axis center first */
        if (distanceToCenter > 0.00003) {
            double moveToCenter = Math.min(distanceToMove, distanceToCenter);
            subSecondary += (subSecondary < center ? 1 : -1) * moveToCenter;
            distanceToMove -= moveToCenter;
        }

        /* Storing new secondry axis sub-tile position */
        if (verticalMove) {
            subTileX = (float)subSecondary;
        } else {
            subTileY = (float)subSecondary;
        }

        while (distanceToMove > 0.00003) {
            /* Capping movement distance to a single tile */
            double maxStep = directionSign > 0 ? 1.0 - subPrimary : subPrimary;
            double step = Math.min(distanceToMove, maxStep);
            double newSubPrimary = subPrimary + directionSign * step;

            /* Handling crossing tile boundery */
            if (crossedTileBoundary(direction, newSubPrimary)) {
                Position nextPos = position.offset(direction);
                if (board.isMovable(nextPos)) {
                    position = nextPos;
                    subPrimary = directionSign > 0 ? 0.0 : 1.0;
                } else {
                    /* If hit wall, set direction to none */
                    currentDirection = Direction.NONE;
                    subPrimary = directionSign > 0 ? 1.0 : 0.0;
                    break; /* stopping at wall */
                }
            } else {
                subPrimary = newSubPrimary;
            }

            /* If there's still longer to go */
            distanceToMove -= step;
        }

        /* Storing primary axis sub-tile position */
        if (verticalMove) {
            subTileY = (float)subPrimary;
        } else {
            subTileX = (float)subPrimary;
        }

        if (canMoveNext && distanceToMove < 0.0003) {
            currentDirection = nextDirection;
            nextDirection = Direction.NONE;
        }
    }

    /**
     * Helper function that determines if Pacman can move 
     * in the specified direction.
     *
     * @param board the game {@link Board}.
     * @param pos the current {@link Position}.
     * @param dir the direction to check.
     * @return true if the tile in the given {@link Direction} is movable.
     */
    private boolean canMove(Board board, Position pos, Direction dir) {
        assert board != null && pos != null && dir != null;
        return dir != Direction.NONE && board.isMovable(pos.offset(dir));
    }

    /**
     * Helper function that decides the direction to move in
     * based on current and next direction availability.
     *
     * @param canMoveNext true if the next direction is available.
     * @param canContinue true if continuing in the current direction is possible.
     * @return the direction Pacman should move in.
     */
    private Direction decideDirection(boolean canMoveNext, boolean canContinue) {
        if (canMoveNext) { return nextDirection; }
        if (canContinue) { return currentDirection; }
        return Direction.NONE;
    }

    /**
     * helper function that checks if a direction is vertical.
     *
     * @param dir the direction to check.
     * @return true if the direction is {@link Direction.UP} or {@link Direction.DOWN}.
     */
    private boolean isVertical(Direction dir) {
        assert dir != null;
        return dir == Direction.UP || dir == Direction.DOWN;
    }

    /**
     * Helper function that gets the sign of movement for a {@link Direction}.
     *
     * @param dir the direction.
     * @return -1 for UP or LEFT and 1 for DOWN or RIGHT.
     */
    private int getDirectionSign(Direction dir) {
        assert dir != null;
        return (dir == Direction.UP || dir == Direction.LEFT) ? -1 : 1;
    }

    /**
     * Helper function that determines if Pacman has crossed a tile boundary.
     *
     * @param dir the direction of movement.
     * @param newPrimary the new sub-tile coordinate in the movement axis.
     * @return true if a tile boundary was crossed.
     */
    private boolean crossedTileBoundary(Direction dir, double newPrimary) {
        assert dir != null;
        return (dir == Direction.UP && newPrimary <= 0.0)
            || (dir == Direction.DOWN && newPrimary >= 1.0)
            || (dir == Direction.LEFT && newPrimary <= 0.0)
            || (dir == Direction.RIGHT && newPrimary >= 1.0);
    }

    /**
     * Helper function that snaps Pacman toward the center of the tile when not moving.
     *
     * @param distanceToMove the maximum distance Pacman can move.
     */
    private void snapToCenter(double distanceToMove) {
        double center = 0.5;

        subTileX = (float)snapAxisToCenter(subTileX, center, distanceToMove);
        subTileY = (float)snapAxisToCenter(subTileY, center, distanceToMove);
    }

    /**
     * Helper function that moves a sub-tile coordinate toward the center up to a maximum distance.
     *
     * @param sub the current sub-coordinate.
     * @param center the center coordinate (typically 0.5).
     * @param maxMove the maximum movement allowed.
     * @return the updated coordinate.
     */
    private double snapAxisToCenter(double sub, double center, double maxMove) {
        double dist = Math.abs(sub - center);
        if (dist < maxMove) {
            return center;
        } else {
            return sub + ((sub < center) ? 1 : -1) * maxMove;
        }
    }

                            /* Getters */
    /**
     * Gets the tile-based position of the entity.
     *
     * @return The current tile position.
     */
    public Position getPosition() { return this.position; }

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
    public double getX() { return position.x() + subTileX; }

    /**
     * Gets the full Y coordinate, including sub-tile offset.
     *
     * @return The entity's Y position as a double.
     */
    public double getY() { return position.y() + subTileY; }

    /**
     * Gets the current speed of Pacman.
     *
     * @return the speed in tiles per second.
     */
    public double getSpeed() { return this.speed; }

    /**
     * Gets the current movement direction of Pacman.
     *
     * @return the current {@link Direction}.
     */
    public Direction getDirection() { return this.currentDirection; }


                            /* Setters */
    /**
     * Sets the entity's tile-based position.
     *
     * @param newPos The new tile position to set (not {@code null})
     * @throws IllegalArgumentException if newPos is null
     */
    public void setPosition(Position newPos) {
        if (newPos == null) {
            throw new IllegalArgumentException("newPos must not be null");
        }
        this.position = newPos;
    }

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

    /**
     * Sets a new speed for Pacman.
     *
     * @param newSpeed the new speed value in tiles per second.
     */
    public void setSpeed(double newSpeed) { this.speed = newSpeed; }

    /**
     * Requests Pacman to change direction at the next available opportunity.
     *
     * @param newDir the desired direction.
     */
    public void setDirection(Direction newDir) { this.nextDirection = newDir; }
}
