package com.gr15.pacman.model.entities;

import com.gr15.pacman.model.Board;
import com.gr15.pacman.model.Position;

/**
 * Entity
 */
public abstract class Entity {

    private Position position;
    private float subTileX = 0.0f;
    private float subTileY = 0.0f;

    private double radius;
    
    public Entity(Position startPos, double radius) {
        this.radius = radius;
        this.position = startPos;
    }

    public abstract void move(Board board, double deltaSeconds);

    /* Getters and setters */
    public Position getPosition() { return this.position; }
    public float getSubTileX() { return this.subTileX; }
    public float getSubTileY() { return this.subTileY; }
    public double getRadius() { return this.radius; }
    public double getPositionX() { return position.x() + subTileX; }
    public double getPositionY() { return position.y() + subTileY; }

    public void setPosition(Position newPos) { this.position = newPos; }
    public void setSubTileX(float newX) { this.subTileX = newX; }
    public void setSubTileY(float newY) { this.subTileY = newY; }
    public void setRadius(double newRad) { this.radius = newRad; }
}
