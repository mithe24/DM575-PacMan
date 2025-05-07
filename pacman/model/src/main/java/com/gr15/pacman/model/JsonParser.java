package com.gr15.pacman.model;

import java.util.HashMap;
import java.util.Map;

import com.gr15.pacman.model.entities.Entity.Direction;
import com.gr15.pacman.model.Board.TileType;
import com.gr15.pacman.model.entities.Entity;
import com.gr15.pacman.model.entities.Pacman;
import com.gr15.pacman.model.entities.Items;

/**
 * jsonParser
 */
public final class JsonParser {

    public static GameState getGameState(String path) {
        TileType[][] tileBoard = {
            {TileType.WALL, TileType.WALL, TileType.WALL, TileType.WALL, TileType.WALL,},
            {TileType.WALL, TileType.EMPTY, TileType.EMPTY, TileType.EMPTY, TileType.WALL,},
            {TileType.WALL, TileType.EMPTY, TileType.WALL, TileType.EMPTY, TileType.WALL,},
            {TileType.WALL, TileType.EMPTY, TileType.WALL, TileType.EMPTY, TileType.WALL,},
            {TileType.WALL, TileType.EMPTY, TileType.EMPTY, TileType.EMPTY, TileType.WALL,},
            {TileType.WALL, TileType.EMPTY, TileType.WALL, TileType.EMPTY, TileType.WALL,},
            {TileType.WALL, TileType.EMPTY, TileType.EMPTY, TileType.EMPTY, TileType.WALL,},
            {TileType.WALL, TileType.WALL, TileType.WALL, TileType.WALL, TileType.WALL,},
        };
        Board board = new Board(8, 5, tileBoard);
        Pacman pacman = new Pacman(new Position(1, 1), 3.0, Direction.NONE, 1);
        Map<Position, Entity> entities = new HashMap<>();
        Map<Position, Items> items = new HashMap<>();

        return new GameState(board, pacman, entities, items);
    }
    
}
