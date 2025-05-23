package com.gr15.pacman.model;

import java.util.List;

import com.gr15.pacman.model.Board.TileType;
import com.gr15.pacman.model.entities.Entity.Direction;

/**
 * Represents a coordinate (x, y) on a 2D grid.
 * Used to denote positions on the game board
 * for entities such as Pacman and ghosts.
 *
 * This record provides utility methods for movement, bounds checking,
 * distance calculation, neighboring positions.
 *
 * @param x the horizontal coordinate (column)
 * @param y the vertical coordinate (row)
 */
public record Position(int x, int y) {

    /**
     * Returns a new {@code Position} offset from this one
     * in the specified direction.
     *
     * @param dir the direction to offset by (must not be {@code null})
     * @return a new position one tile in the given direction
     * @throws IllegalArgumentException if {@code dir} is {@code null}
     */
    public Position offset(Direction dir) {
        if (dir == null) {
            throw new IllegalArgumentException("dir must not be null");
        }
        return switch(dir) {
            case UP     -> new Position(x, y-1);
            case DOWN   -> new Position(x, y+1);
            case LEFT   -> new Position(x-1, y);
            case RIGHT  -> new Position(x+1, y);
            default     -> this;
        };
    }
    /**
     * Checks if this position is within the bounds of the provided 2D board.
     *
     * @param board the tile board to check against (must not be {@code null})
     * @return {@code true} if the position is within the board, {@code false} otherwise
     * @throws IllegalArgumentException if {@code board} is {@code null}
     */
    public boolean inBounds(TileType[][] board) {
        if (board == null) {
            throw new IllegalArgumentException("board must not be null");
        }
        return y >= 0 && y < board.length
            && x >= 0 && x < board[y].length; 
    }

    /**
     * Calculates the Euclidean distance from this position to another.
     *
     * @param other the other position to compare to (must not be {@code null})
     * @return the straight-line distance between this position and the other
     * @throws IllegalArgumentException if {@code other} is {@code null}
     */
    public double distance(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        return Math.hypot(other.x - x, other.y - y);
    }

    /**
     * Returns a list of the four neighbor positions (up, down, left, right).
     * Diagonal neighbors are not included.
     *
     * @return a list of adjacent positions surrounding this one
     */
    public List<Position> neighbors() {
        return List.of(
            new Position(x, y+1),
            new Position(x, y-1),
            new Position(x-1, y),
            new Position(x+1, y));
    }
}
