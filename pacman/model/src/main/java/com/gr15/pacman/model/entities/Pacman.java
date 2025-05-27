package com.gr15.pacman.model.entities;

import com.gr15.pacman.model.Position;

/**
 * Represents the Pacman character in the game.
 */
public class Pacman
    extends Entity {

    /**
     * Constructs a new Pacman instance.
     *
     * @param startPos the starting tile {@link Position} of Pacman.
     * @param speed the movement speed in tiles per second.
     * @param radius the radius of Pacman collision.
     */
    public Pacman(Position startPos, double speed, double radius) {
        super(startPos, radius, speed);
    }
}
