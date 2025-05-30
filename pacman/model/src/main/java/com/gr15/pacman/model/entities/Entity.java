package com.gr15.pacman.model.entities;

import com.gr15.pacman.model.GameState.TileType;
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
 * {@code subTileX} and {@code subTileY}, allowing sub-tile precision. </p>
 *
 * <p> Handles movement logic including direction changes, wall collisions,
 * snapping to tile centers, and updating position on the game board. </p>
 */
public abstract class Entity {

    /** Epsilon is used as tolerance when comparing floating point numbers. */
    private static final double EPSILON = 1e-5;

    /** {@link Position} for tile coordinates */
    private Position position;

    /** X component of sub tile position */
    private float subTileX = 0.5f;

    /** Y component of sub tile position */
    private float subTileY = 0.5f;

    /** Radius of the entity */
    private double radius;

    /** Current {@link Direction} in which the entity is moving */
    private Direction currentDirection = Direction.NONE;

    /** Next {@link Direction} in which the entity will move */
    private Direction nextDirection = Direction.NONE;

    /** Speed of the entity, in tiles pr second */
    private double speed;

    /** Enumeration of all possible directions entities can move in. */
    public enum Direction {

        /** Direction up. */
        UP,

        /** Direction down. */
        DOWN,

        /** Direction left. */
        LEFT,

        /** Direction right. */
        RIGHT,

        /** Enum value for no direciton. */
        NONE
    };
    
    /**
     * Constructs an Entity with the specified starting position and radius.
     *
     * @param startPos The initial tile-based position of the entity.
     * @param radius The collision radius of the entity.
     * @param speed The speed of which the entity moves, in tiles per second.
     * @throws IllegalArgumentException if any parameter is invalid:
     *      - startPos is null
     *      - radius is less than or equal to zero
     *      - speed is less than zero
     */
    public Entity(Position startPos, double radius, double speed) {
        if (startPos == null) {
            throw new IllegalArgumentException("startPos cannot be null");
        }
        if (radius <= 0) {
            throw new IllegalArgumentException("radius must be positive");
        }
        if (speed < 0) {
            throw new IllegalArgumentException("Speed must be non-negative");
        }
        this.radius = radius;
        this.position = startPos;
        this.speed = speed;
    }

    /**
     * Updates Pacman's {@link Position} based on the time elapsed
     * and the game {@link TileType} state.
     * Handles turning, and tile boundary transitions.
     * Handles moving through multiple tiles by stepping.
     *
     * @param board the game {@link TileType}, used to determine valid movement.
     * @param deltaSeconds time in seconds since the last update in seconds.
     * @throws IllegalArgumentException if board is {@code null}
     */
    public void move(TileType[][] board, double deltaSeconds) {
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
        if (distanceToCenter > EPSILON) {
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

        while (distanceToMove > EPSILON) {
            /* Capping movement distance to a single tile */
            double maxStep = directionSign > 0 ? 1.0 - subPrimary : subPrimary;
            double step = Math.min(distanceToMove, maxStep);
            double newSubPrimary = subPrimary + directionSign * step;

            /* Handling crossing tile boundery */
            if (crossedTileBoundary(direction, newSubPrimary)) {
                Position nextPos = position.offset(direction);
                if (nextPos.inBounds(board) 
                    && board[nextPos.y()][nextPos.x()] != TileType.WALL) {
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

        if (canMoveNext && distanceToCenter < EPSILON) {
            currentDirection = nextDirection;
            nextDirection = Direction.NONE;
        }
    }

    /**
     * Helper function that determines if Pacman can move 
     * in the specified direction.
     *
     * @param board the game board of {@link TileType}.
     * @param pos the current {@link Position}.
     * @param dir the direction to check.
     * @return {@code true} if the tile in the given {@link Direction} is movable.
     */
    private boolean canMove(TileType[][] board, Position pos, Direction dir) {
        assert board != null && pos != null && dir != null;
        Position next = pos.offset(dir);
        return dir != Direction.NONE && next.inBounds(board) 
            && board[next.y()][next.x()] != TileType.WALL;
    }

    /**
     * Helper function that decides the {@link Direction} to move in
     * based on current and next {@link Direction} availability.
     *
     * @param canMoveNext true if the next {@link Direction} is available.
     * @param canContinue true if continuing in the current {@link Direction} is possible.
     * @return the direction Pacman should move in.
     */
    private Direction decideDirection(boolean canMoveNext, boolean canContinue) {
        if (canMoveNext) { return nextDirection; }
        if (canContinue) { return currentDirection; }
        return Direction.NONE;
    }

    /**
     * helper function that checks if a {@link Direction} is vertical.
     *
     * @param dir the {@link Direction} to check.
     * @return {@code true} if the direction is {@link Direction.UP} or {@link Direction.DOWN}.
     */
    private boolean isVertical(Direction dir) {
        assert dir != null;
        return dir == Direction.UP || dir == Direction.DOWN;
    }

    /**
     * Helper function that gets the sign of movement for a {@link Direction}.
     *
     * @param dir the {@link Direction}.
     * @return -1 for UP or LEFT and 1 for DOWN or RIGHT.
     */
    private int getDirectionSign(Direction dir) {
        assert dir != null;
        return (dir == Direction.UP || dir == Direction.LEFT) ? -1 : 1;
    }

    /**
     * Helper function that determines if the entity has crossed a tile boundary.
     *
     * @param dir the {@link Direction} of movement.
     * @param newPrimary the new sub-tile coordinate in the movement axis.
     * @return {@code true} if a tile boundary was crossed.
     */
    private boolean crossedTileBoundary(Direction dir, double newPrimary) {
        assert dir != null;
        return (dir == Direction.UP && newPrimary <= 0.0)
            || (dir == Direction.DOWN && newPrimary >= 1.0)
            || (dir == Direction.LEFT && newPrimary <= 0.0)
            || (dir == Direction.RIGHT && newPrimary >= 1.0);
    }

    /**
     * Helper function that snaps the entity toward
     * the center of the tile when not moving.
     
     * @param distanceToMove the maximum distance the entity can move.
     */
    private void snapToCenter(double distanceToMove) {
        double center = 0.5;

        subTileX = (float)snapAxisToCenter(subTileX, center, distanceToMove);
        subTileY = (float)snapAxisToCenter(subTileY, center, distanceToMove);
    }

    /**
     * Helper function that moves a sub-tile coordinate
     * toward the center up to a maximum distance.
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

    /*************************************************************
     *                          Getters                          *
     *************************************************************/

    /**
     * Gets the tile-based {@link Position} of the entity.
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
     * Gets the current speed of the entity.
     *
     * @return the speed in tiles per second.
     */
    public double getSpeed() { return this.speed; }

    /**
     * Gets the current movement direction of the entity.
     *
     * @return the current {@link Direction}.
     */
    public Direction getDirection() { return this.currentDirection; }

    /*************************************************************
     *                          Setters                          *
     *************************************************************/

    /**
     * Sets the entity's tile-based {@link Position}.
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
    public void setSubTileX(float newX) {
        this.subTileX = newX;
    }

    /**
     * Sets the entity's sub-tile Y offset.
     *
     * @param newY The new sub-tile Y coordinate.
     */
    public void setSubTileY(float newY) {
        this.subTileY = newY;
    }

    /**
     * Sets the entity's collision radius.
     *
     * @param newRadius The new radius value.
     */
    public void setRadius(double newRadius) {
        if (newRadius <= 0) {
            throw new IllegalArgumentException("radius must be positive");
        }
        this.radius = newRadius;
    }

    /**
     * Sets a new speed for the entity.
     *
     * @param newSpeed the new speed value in tiles per second.
     */
    public void setSpeed(double newSpeed) {
        if (newSpeed < 0) {
            throw new IllegalArgumentException("Speed must be non-negative");
        }
        this.speed = newSpeed;
    }

    /**
     * Requests the entity to change direction at the next available opportunity.
     *
     * @param newDir the desired {@link Direction}.
     */
    public void setDirection(Direction newDir) {
        if (newDir == null) {
                throw new IllegalArgumentException("newDir must not be null");
        }
        this.nextDirection = newDir;
    }
}
