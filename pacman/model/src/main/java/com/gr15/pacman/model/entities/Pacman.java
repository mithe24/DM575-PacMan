package com.gr15.pacman.model.entities;

import com.gr15.pacman.model.Board;
import com.gr15.pacman.model.Position;
import com.gr15.pacman.model.Board.Direction;

/**
 * Pacman
 */
public class Pacman
    extends Entity {

    /* Tiles per second */
    private double speed;
    private Direction currentDirection = Direction.NONE;
    private Direction nextDirection;

    public Pacman(Position startPos, double speed, Direction startDir, double radius) {
        super(startPos, radius);
        this.speed = speed;
        this.nextDirection = startDir;
    }

    @Override
    public void move(Board board, double deltaSeconds) {
        float subTileX = super.getSubTileX();
        float subTileY = super.getSubTileY();
        double distanceToMove = speed * deltaSeconds;

        Position currentPos = super.getPosition();
        boolean canMoveNext = nextDirection != Direction.NONE 
            && board.isMovable(currentPos.offset(nextDirection));
        boolean canContinue = currentDirection != Direction.NONE 
            && board.isMovable(currentPos.offset(currentDirection));

        Direction direction = canMoveNext ? nextDirection 
            : (canContinue ? currentDirection : Direction.NONE);

        if (direction == Direction.NONE) {
            /* Snap to center if not moving */
            double centerX = 0.5;
            double centerY = 0.5;
            double distToCenterX = Math.abs(subTileX - centerX);
            double distToCenterY = Math.abs(subTileY - centerY);

            if (distToCenterX < distanceToMove) {
                subTileX = (float)centerX;
            } else {
                subTileX += (subTileX < centerX ? 1 : -1) * distanceToMove;
            }

            if (distToCenterY < distanceToMove) {
                subTileY = (float)centerY;
            } else {
                subTileY += (subTileY < centerY ? 1 : -1) * distanceToMove;
            }

            super.setSubTileX(subTileX);
            super.setSubTileY(subTileY);
            return;
        }

        boolean verticalMove = direction == Direction.UP 
            || direction == Direction.DOWN;
        int directionSign = (direction == Direction.UP 
            || direction == Direction.LEFT) ? -1 : 1;

        /* Determine primary and secondary sub-tile axes */
        double subTilePrimary = verticalMove ? subTileY : subTileX;
        double subTileSecondary = verticalMove ? subTileX : subTileY;
        double center = Math.floor(subTileSecondary) + 0.5;
        double distanceToCenter = Math.abs(subTileSecondary - center);

        /* First, move to center if needed */
        if (distanceToCenter > 0.01) {
            double moveToCenter = Math.min(distanceToMove, distanceToCenter);
            if (subTileSecondary < center) {
                subTileSecondary += moveToCenter;
            } else {
                subTileSecondary -= moveToCenter;
            }
            distanceToMove -= moveToCenter;
        }

        /* Apply secondary axis back */
        if (verticalMove) {
            subTileX = (float)subTileSecondary;
        } else {
            subTileY = (float)subTileSecondary;
        }

        /* Now, attempt to move along primary axis */
        while (distanceToMove > 0.0001) {
            double maxStep = directionSign > 0 ? 1.0 - subTilePrimary 
                : subTilePrimary;
            double step = Math.min(distanceToMove, maxStep);
            double newSubTilePrimary = subTilePrimary + directionSign * step;


            if ((direction == Direction.RIGHT && newSubTilePrimary >= 1.0) 
                || (direction == Direction.LEFT && newSubTilePrimary <= 0.0) 
                || (direction == Direction.DOWN && newSubTilePrimary >= 1.0) 
                || (direction == Direction.UP && newSubTilePrimary <= 0.0)) {

                Position newPos = currentPos.offset(direction);
                if (board.isMovable(newPos)) {
                    currentPos = newPos;
                    subTilePrimary = directionSign > 0 ? 0.0 : 1.0;
                } else {
                    currentDirection = Direction.NONE;
                    subTilePrimary = directionSign > 0 ? 1.0 : 0.0;
                    break;
                }
            } else {
                subTilePrimary = newSubTilePrimary;
            }

            distanceToMove -= step;
        }

        /* Assign back primary axis */
        if (verticalMove) {
            subTileY = (float)subTilePrimary;
        } else {
            subTileX = (float)subTilePrimary;
        }

        /* Update direction state */
        if (canMoveNext) {
            currentDirection = nextDirection;
            nextDirection = Direction.NONE;
        }

        /* Update final state */
        super.setSubTileX(subTileX);
        super.setSubTileY(subTileY);
        super.setPosition(currentPos);
    }

    /* Getters and setters */
    public double getSpeed() { return this.speed; }
    public Direction getDirection() { return this.currentDirection; }
    public void setSpeed(double newSpeed) { this.speed = newSpeed; }
    public void setDirection(Direction newDir) { this.nextDirection = newDir; }
}
