package com.gr15.pacman.model;

/**
 * Board
 */
public class Board {

    private TileType[][] tileBoard;
    private int height;
    private int width;

    public enum TileType { WALL, EMPTY };

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Board(int width, int height, TileType[][] tileBoard) {
        this(width, height);
        this.tileBoard = tileBoard;
    }

    public boolean isMovable(Position pos) {
        if (!pos.inBounds(width, height)) {
            return false;
        } else {
            return tileBoard[pos.y()][pos.x()] != TileType.WALL;
        }
    }


    /* Getters and setters */
    public TileType[][] getTileBoard() { return this.tileBoard; }
    public TileType getTile(int x, int y) { return tileBoard[y][x]; }
    public int getHeight() { return this.height; }
    public int getWidth() { return this.width; }

    public void setHeight(int newHeight) { this.height = newHeight; }
    public void setWidth(int newWidth) { this.width = newWidth; }
    public void setTile(int x, int y, TileType newTile) { 
        this.tileBoard[y][x] = newTile;
    }
    public void setTileBoard(TileType[][] newBoard) { 
        this.tileBoard = newBoard;
    }

}
