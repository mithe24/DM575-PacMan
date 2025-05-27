package com.gr15.pacman.model.entities.searching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

import com.gr15.pacman.model.Position;

/**
 * Implements the Breadth-First Search (BFS) algorithm for finding
 * the shortest path between two positions on the game board.
 */
public class BreadthFirstSearch
    implements SearchStrategy {

    /**
     * Performs a BFS to find the shortest path from the start to the goal.
     *
     * @param start The starting position of the ghost.
     * @param goal The target position
     *      (e.g., Pacman's current location or a scatter target).
     * @param isWalkable A function that determines
     *      whether a given tile can be walked on.
     * @return A list of positions representing the path from start to goal,
     *      excluding the start position. Returns null if no path is found.
     */
    @Override
    public List<Position> search(Position start, Position goal,
        Function<Position, Boolean> isWalkable) {
        if (start == null) {
            throw new IllegalArgumentException("Start position must not be null");
        }
        if (goal == null) {
            throw new IllegalArgumentException("Goal position must not be null");
        }
        if (isWalkable == null) {
            throw new IllegalArgumentException("isWalkable must not be null");
        }

        Queue<Position> queue = new LinkedList<>();
        Map<Position, Position> cameFrom = new HashMap<>();
        Set<Position> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            Position current = queue.poll();

            if (current.equals(goal)) {
                List<Position> path = new ArrayList<>();
                while (cameFrom.get(current) != null) {
                    path.add(0, current);
                    current = cameFrom.get(current);
                }

                return path;
            }

            for (Position neighbor : current.neighbors()) {
                if (!visited.contains(neighbor) && isWalkable.apply(neighbor)) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        
        return null;
    }
}
