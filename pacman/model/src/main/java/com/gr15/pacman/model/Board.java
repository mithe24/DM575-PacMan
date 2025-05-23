package com.gr15.pacman.model;

/**
 * Represents the game board for a Pac-Man-style game.
 * The board is a 2D grid of {@link TileType} elements,
 * indicating different tile types such as walls, pellets, and empty spaces.
 */
public class Board {

    /** 2D array representing the board layout with tile types. */
    private TileType[][] board;

    /** Enumeration of possible tile types on the board. */
    public enum TileType { WALL, EMPTY, PELLET, POWER_PELLET };

    /**
     * Constructs a new {@code Board} with the given 2D tile array.
     *
     * @param board the initial board layout; must not be {@code null}.
     * @throws IllegalArgumentException if the provided board is {@code null}.
     */
    public Board(TileType[][] board) {
        if (board == null) {
            throw new IllegalArgumentException("board must not be null");
        }
        this.board = board;
    }

    /**
     * Checks if a position on the board is movable
     * (i.e., not a wall and within bounds).
     *
     * @param pos the position to check.
     * @return {@code true} if the position is within bounds
     *      and not a wall, {@code false} otherwise.
     */
    public boolean isMovable(Position pos) {
        if (pos == null) { return false; }
        return pos.inBounds(board) && getTile(pos) != TileType.WALL;
    }

    /**
     * Returns the entire 2D tile board array.
     *
     * @return the tile board.
     */
    public TileType[][] getTileBoard() { return this.board; }

    /**
     * Returns the tile type at the specified coordinates.
     *
     * @param x the x-coordinate (column).
     * @param y the y-coordinate (row).
     * @return the tile type at (x, y).
     * @throws IllegalArgumentException if x and y is out of bounds.
     */
    public TileType getTile(int x, int y) {
        return getTile(new Position(x, y));
    }

    /**
     * Returns the tile type at the specified coordinates.
     *
     * @param pos the position to retrieve the tile from.
     * @return the tile type at (x, y).
     * @throws IllegalArgumentException if x and y is out of bounds.
     */
    public TileType getTile(Position pos) {
        if (!pos.inBounds(board)) {
            throw new IllegalArgumentException("x and y must be within bounds");
        }
        return board[pos.y()][pos.x()];
    }

    /**
     * Sets the tile type at the specified coordinates.
     *
     * @param x       the x-coordinate (column).
     * @param y       the y-coordinate (row).
     * @param newTile the new tile type to set.
     * @throws IllegalArgumentException if x and y is out of bounds,
     *      or newTile is {@code null}.
     */
    public void setTile(int x, int y, TileType newTile) { 
        if (!new Position(x, y).inBounds(board)) {
            throw new IllegalArgumentException("x and y must be within bounds");
        }
        if (newTile == null) {
            throw new IllegalArgumentException("newTile must not be null");
        }
        this.board[y][x] = newTile;
    }

    /**
     * Replaces the current tile board with a new 2D tile array.
     *
     * @param newBoard the new tile board to set.
     * @throws IllegalArgumentException if newBoard is {@code null}
     */
    public void setTileBoard(TileType[][] newBoard) { 
        if (board == null) {
            throw new IllegalArgumentException("board must not be null");
        }
        this.board = newBoard;
    }
}
