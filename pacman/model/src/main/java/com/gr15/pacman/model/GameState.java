package com.gr15.pacman.model;

import java.util.List;
import java.util.Map;

import com.gr15.pacman.model.entities.Entity;
import com.gr15.pacman.model.entities.EntityUtils;
import com.gr15.pacman.model.entities.Items;
import com.gr15.pacman.model.entities.Pacman;

/**
 * GameState
 */
public class GameState {
    
    private Board board;

    private Pacman pacman;
    private List<Entity> entities;
    private Map<Position, Items> items;

    private int score = 0;
    private int lives = 3;
    private double powerModeDuration = 0;

    protected GameState(Board board, Pacman pacman, 
            List<Entity> entities, Map<Position, Items> items) {
        this.board = board;
        this.entities = entities;
        this.items = items;
        this.pacman = pacman;
    }

    public void update(double deltaSeconds) {
        if (lives == 0) {
            /* Gameover */
        }

        pacman.move(board, deltaSeconds);

        /* Power mode */
        powerModeDuration -= deltaSeconds;
        powerModeDuration = Math.max(powerModeDuration, 0);

        /* updating and checking collisions with entities */
        for (Entity entity : entities) {
            entity.move(board, deltaSeconds);
            if (EntityUtils.hasCollided(pacman, entity)
                && powerModeDuration == 0) {
                lives -= 1;
            } else if (EntityUtils.hasCollided(pacman, entity)) {
                score += 100;
            }
        }

        /* checking collisions with items */
        Items item = items.get(pacman.getPosition());
        if (item != null) {
            switch (item) {
                case PELLET -> score++;
                case POWER_PELLET -> powerModeDuration = 10.0;
                default -> {}
            }
        }
    }

    /* Getters and setters */
    public Board getBoard() { return this.board; }
    public Pacman getPacman() { return this.pacman; }
    public List<Entity> getEntities() { return this.entities; }
    public Map<Position, Items> getItems() { return this.items; }
    public int getScore() { return this.score; }
    public int getLives() { return this.lives; }

    public void setBoard(Board newBoard) { this.board = newBoard; }
    public void setPacman(Pacman newPacman) { this.pacman = newPacman; }
    public void setScore(int newScore) { this.score = newScore; }
    public void setLives(int newLives) { this.lives = newLives; }

    public void setItems(Map<Position, Items> newItems) { 
        this.items = newItems; 
    }
    public void setEntities(List<Entity> newEntities) { 
        this.entities = newEntities; 
    }
}
