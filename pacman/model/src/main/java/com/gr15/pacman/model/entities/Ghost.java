package com.gr15.pacman.model.entities;

import com.gr15.pacman.model.Board;
import com.gr15.pacman.model.Position;

/**
 * Ghost
 */
public class Ghost
    extends Entity{

    public Ghost(Position startPos, double radius) {
        super(startPos, radius);
    }
    @Override
    public void move(Board board, double deltaSeconds) {
        // TODO Auto-generated method stub
    }
}
