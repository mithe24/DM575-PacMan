package com.gr15.pacman.model;

import java.util.List;

import com.gr15.pacman.model.Board.Direction;

/**
 * Position
 */
public record Position(int x, int y) {

    public Position offset(Direction dir) {
        return switch(dir) {
            case UP     -> new Position(x, y-1);
            case DOWN   -> new Position(x, y+1);
            case LEFT   -> new Position(x-1, y);
            case RIGHT  -> new Position(x+1, y);
            default     -> this;
        };
    }

    public double distance(Position other) {
        return Math.hypot(other.x - x, other.y - y);
    }

    public boolean inBounds(int width, int height) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public List<Position> neighbors() {
        return List.of(
            new Position(x, y+1),
            new Position(x, y-1),
            new Position(x-1, y),
            new Position(x+1, y)
        );
    }
}
