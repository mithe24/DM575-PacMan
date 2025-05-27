package com.gr15.pacman.model.entities.searching;

import java.util.List;
import java.util.function.Function;

import com.gr15.pacman.model.Position;

/**
 * Interface for defining pathfinding algorithms used by ghosts to
 * navigate the game board.
 */
public interface SearchStrategy {

    /**
     * Searches for a path from the start position to the goal position,
     * considering only walkable tiles.
     *
     * @param start The starting position of the search.
     * @param goal The target position to reach.
     * @param isWalkable A function that determines if a given position is walkable
     *      (not a wall, in bounds, etc.).
     * @return A list of positions representing the shortest path to the goal,
     *      excluding the start position. Returns null if no path exists.
     */
    List<Position> search(Position start, Position goal,
        Function<Position, Boolean> isWalkable);
}
