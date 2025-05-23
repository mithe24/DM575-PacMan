package com.gr15.pacman.model;

import com.gr15.pacman.model.Board.TileType;

/**
 * Represents the configuration for a Pac-Man-style game.
 * This class is immutable and ensures defensive copying of the initial game board to
 * maintain data integrity.
 *
 * @param initialBoard The initial layout of the game board.
 * @param powerModeDuration Duration (in seconds) that Pac-Man remains in
 *      power mode after consuming a power item.
 * @param lives The number of lives {@code Pacman} starts with.
 * @param ghostSpeed The speed of the ghosts.
 * @param pacmanSpeed The speed of {@code Pacman}
 * @param numberOfItems The number of collectible items on the board.
 * @param pacmanStartPosition The starting {@link Position} of {@link Pacman}.
 * @param redGhostStartPosition The starting {@link Position} of the red ghost.
 * @param blueGhostStartPosition The starting {@link Position} of the blue ghost.
 * @param pinkGhostStartPosition The starting {@link Position} of the pink ghost.
 * @param orangeGhostStartPosition The starting {@link Position} of the orange ghost.
 */
public record GameConfig(
    TileType[][] initialBoard,
    double powerModeDuration,
    int lives,
    double ghostSpeed,
    double pacmanSpeed,
    int numberOfItems,

    Position pacmanStartPosition,
    Position redGhostStartPosition,
    Position blueGhostStartPosition,
    Position pinkGhostStartPosition,
    Position orangeGhostStartPosition) {

    /**
     * Constructs a new {@code GameConfig} record.
     * Performs a deep copy of the initial board to ensure immutability.
     */
    public GameConfig {
        initialBoard = deepCopy(initialBoard);
    }

    /**
     * Returns a deep copy of the initial board configuration.
     * This prevents external modification of the board data.
     *
     * @return A deep copy of the initial board.
     */
    @Override
    public TileType[][] initialBoard() {
        return deepCopy(initialBoard);
    }

    /**
     * Creates a deep copy of a 2D TileType array.
     *
     * @param arg The 2D array to copy.
     * @return A deep copy of the provided 2D array,
     *      or {@code null} if the input is {@code null}.
     */
    private static TileType[][] deepCopy(TileType[][] arg) {
        if (arg == null) { return null; }
        TileType[][] copy = new TileType[arg.length][];
        for (int i = 0; i < arg.length; i++) {
            copy[i] = arg[i].clone();
        }

        return copy;
    }
}
