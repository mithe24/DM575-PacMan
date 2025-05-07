package com.gr15.pacman.model.entities;

import com.gr15.pacman.model.Board;
import com.gr15.pacman.model.Position;

/**
 * Represents the Pacman character in the game.
 * Handles movement logic including direction changes, wall collisions,
 * snapping to tile centers, and updating position on the game board.
 */
public class Pacman
    extends Entity {

    /* Javadoc for attributes,
     * since movement is a little complex */

    /** Movement speed in tiles per second. */
    private double speed;

    /** Current direction of movement. */
    private Direction currentDirection = Direction.NONE;

    /** Desired direction to change to, if possible. */
    private Direction nextDirection;

    /**
     * Constructs a new Pacman instance.
     *
     * @param startPos the starting tile {@link Position} of Pacman.
     * @param speed the movement speed in tiles per second.
     * @param startDir the initial movement {@link Direction}.
     * @param radius the radius of Pacman collision.
     */
    public Pacman(Position startPos, double speed, Direction startDir, double radius) {
        super(startPos, radius);
        this.speed = speed;
        this.nextDirection = startDir;
    }

    /**
     * Updates Pacman's {@link Position} based on the time elapsed
     * and the game {@link Board} state.
     * Handles turning, snapping to tile center, 
     * and tile boundary transitions.
     *
     * @param board the game {@link Board}, used to determine valid movement.
     * @param deltaSeconds time in seconds since the last update in seconds.
     */
    @Override
    public void move(Board board, double deltaSeconds) {
        float subTileX = super.getSubTileX();
        float subTileY = super.getSubTileY();
        double distanceToMove = speed * deltaSeconds;

        Position currentPos = super.getPosition();
        boolean canMoveNext = canMove(board, currentPos, nextDirection);
        boolean canContinue = canMove(board, currentPos, currentDirection);

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
        if (distanceToCenter > 0.01) {
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

        while (distanceToMove > 0.0001) {
            /* Capping movement distance to a single tile */
            double maxStep = directionSign > 0 ? 1.0 - subPrimary : subPrimary;
            double step = Math.min(distanceToMove, maxStep);
            double newSubPrimary = subPrimary + directionSign * step;

            if (crossedTileBoundary(direction, newSubPrimary)) {
                Position nextPos = currentPos.offset(direction);
                if (board.isMovable(nextPos)) {
                    currentPos = nextPos;
                    subPrimary = directionSign > 0 ? 0.0 : 1.0;
                } else {
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

        /* If hit wall, set direction to none */
        if (canMoveNext) {
            currentDirection = nextDirection;
            nextDirection = Direction.NONE;
        }

        /* Setting new position */
        super.setSubTileX(subTileX);
        super.setSubTileY(subTileY);
        super.setPosition(currentPos);
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
        return dir == Direction.UP || dir == Direction.DOWN;
    }

    /**
     * Helper function that gets the sign of movement for a {@link Direction}.
     *
     * @param dir the direction.
     * @return -1 for UP or LEFT and 1 for DOWN or RIGHT.
     */
    private int getDirectionSign(Direction dir) {
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
        float subTileX = super.getSubTileX();
        float subTileY =super.getSubTileY();
        double center = 0.5;

        subTileX = (float)snapAxisToCenter(subTileX, center, distanceToMove);
        subTileY = (float)snapAxisToCenter(subTileY, center, distanceToMove);

        super.setSubTileX(subTileX);
        super.setSubTileY(subTileY);
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
