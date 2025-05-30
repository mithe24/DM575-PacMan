package com.gr15.pacman.model.entities;

import java.util.List;
import com.gr15.pacman.model.GameState.TileType;
import com.gr15.pacman.model.entities.searching.SearchStrategy;
import com.gr15.pacman.model.Position;

/**
 * Represents a ghost in the Pacman game.
 * Each ghost has a specific type and uses a pathfinding strategy
 * to move toward a target position (goal).
 * The ghost's movement logic is controlled by its current goal and it's {@link SearchStrategy}.
 */
public class Ghost
    extends Entity {

    /**
     * Enum representing the four classic ghost types.
     */
    public enum GhostType {

        /** The red ghost */
        RED,

        /** The blue ghost */
        BLUE,

        /** The pink ghost */
        PINK,

        /** The orange ghost */
        ORANGE
    };

    private GhostType ghostType;
    private SearchStrategy searchStrategy;
    private Position goal;
        
    /**
     * Constructs a new Ghost instance.
     *
     * @param startPos The initial position of the ghost.
     * @param speed The movement speed of the ghost.
     * @param radius The collision radius of the ghost.
     * @param goal The goal for the ghost will move towards.
     * @param type The ghost's type (e.g., RED, BLUE, PINK, or ORANGE).
     * @param searchStrategy The strategy used by this ghost to find paths.
     */
    public Ghost(Position startPos, double speed, double radius,
        Position goal, GhostType type, SearchStrategy searchStrategy) {
        super(startPos, radius, speed);
        if (goal == null) {
            throw new IllegalArgumentException("goal must not be null");
        }
        if (searchStrategy == null) {
            throw new IllegalArgumentException("searchStrategy must not be null");
        }
        
        this.goal = goal;
        this.ghostType = type;
        this.searchStrategy = searchStrategy;
    }

    /**
     * Updates the ghost's direction and moves it toward its current goal,
     * using the assigned search strategy to find the shortest path.
     *
     * @param board The game board used to determine walkable tiles.
     * @param deltaSeconds Time passed since the last frame.
     * @throws IllegalArgumentException if board is null.
     */
    @Override
    public void move(TileType[][] board, double deltaSeconds) {
        if (board == null) {
            throw new IllegalArgumentException("board must not be null");
        }

        List<Position> path = searchStrategy.search(
            getPosition(),
            goal,
            pos -> pos.inBounds(board) && board[pos.y()][pos.x()] != TileType.WALL
        );

        if (path != null && !path.isEmpty()) {
            Position next = path.get(0);

            for (Direction direction : Direction.values()) {
                if (getPosition().offset(direction).equals(next)) {
                    setDirection(direction);
                    break;
                }
            }
        }

        super.move(board, deltaSeconds);
    }

    /**
     * Returns the type of this ghost.
     *
     * @return The ghost's type (e.g., RED, BLUE, PINK, or ORANGE).
     */
    public GhostType getGhostType() {
        return this.ghostType;
    }

    /**
     * Returns the current goal position that this ghost is trying to reach.
     *
     * @return The ghost's target position.
     */
    public Position getGoal() {
        return this.goal;
    }

    /**
     * Sets a new goal position for this ghost.
     *
     * @param newGoal The new target position for the ghost to move toward.
     */
    public void setGoal(Position newGoal) {
        this.goal = newGoal;
    }
}
