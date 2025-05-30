package com.gr15.pacman.model;

import java.util.ArrayList;
import java.util.List;

import com.gr15.pacman.model.entities.EntityUtils;
import com.gr15.pacman.model.entities.Ghost;
import com.gr15.pacman.model.entities.Pacman;
import com.gr15.pacman.model.entities.Entity.Direction;
import com.gr15.pacman.model.entities.Ghost.GhostType;
import com.gr15.pacman.model.entities.searching.BreadthFirstSearch;

/**
 * Represents the current state of the game, including the game configuration,
 * board, entities (Pacman and ghosts), score, lives, and power mode status.
 * This class manages game updates, entity movements, collisions, and game progress.
 */
public class GameState {

    /** Epsilon is used as tolerance when comparing floating point numbers. */
    private static final double EPSILON = 1e-5;

    /** Configuration settings for the game such as initial board,
     * speeds, and start positions. */
    private final GameConfig config;

    /** The game board containing tile information such as walls and pellets. */
    private TileType[][] board;

    /** The Pacman entity controlled by the player. */
    private Pacman pacman;

    /** The list of ghost entities that act as AI opponents. */
    private List<Ghost> ghosts;

    /** The current score of the player. */
    private int score = 0;

    /** The remaining duration of power mode (in seconds),
     * during which Pacman can eat ghosts. */
    private double powerModeDuration = 0.0;

    /** The remaining duration of time out, in which no game updates occurs */
    private double timeOutDuration = 0.0;

    /** The number of remaining lives for the player. */
    private int lives;

    /** The number of pellets and power pellets left on the board. */
    private int numberOfItemsLeft;

    /** Enumeration of possible tile types on the board. */
    public enum TileType {

        /** The tile type for a wall */
        WALL,

        /** The tile type for a empty tile */
        EMPTY,

        /** The tile type for a tile with a pellet */
        PELLET,

        /** The tile type for a tile with a power pellet */
        POWER_PELLET
    };


    /*************************************************************
     *                         CONSTRUCTOR                       *
     *************************************************************/

    /**
     * Constructs a new {@code GameState} with the specified game configuration.
     * Initializes the board, player (Pacman), ghosts, score, lives, and item count
     * based on the provided configuration.
     *
     * @param config the game configuration to initialize the state (must not be {@code null})
     * @throws IllegalArgumentException if {@code config} is {@code null}
     */
    public GameState(GameConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config must not be null");
        }

        this.config = config;
        this.lives = config.lives();
        this.numberOfItemsLeft = config.numberOfItems();
        this.board = config.initialBoard();
        this.pacman = new Pacman(config.pacmanStartPosition(), config.pacmanSpeed(), 0.5);

        this.ghosts = new ArrayList<>();
        this.ghosts.add(new Ghost(config.redGhostStartPosition(), config.ghostSpeed(),
            0.5, pacman.getPosition(), GhostType.RED, new BreadthFirstSearch()));
        ghosts.add(new Ghost(config.blueGhostStartPosition(), config.ghostSpeed(),
            0.5, pacman.getPosition(), GhostType.BLUE, new BreadthFirstSearch()));
        ghosts.add(new Ghost(config.pinkGhostStartPosition(), config.ghostSpeed(),
            0.5, pacman.getPosition(), GhostType.PINK, new BreadthFirstSearch()));
        ghosts.add(new Ghost(config.orangeGhostStartPosition(), config.ghostSpeed(),
            0.5, pacman.getPosition(), GhostType.ORANGE, new BreadthFirstSearch()));
    }

    /*************************************************************
     *                          GAME LOGIC                       *
     *************************************************************/

    /**
     * Updates the game state based on the elapsed time.
     * Moves Pacman and ghosts, handles power mode duration, detects collisions,
     * updates score, and processes pellet consumption.
     *
     * @param deltaSeconds the time elapsed since the last update (in seconds)
     */
    public void update(double deltaSeconds) {

        if (timeOutDuration > EPSILON) {
            timeOutDuration -= deltaSeconds;
            return; /* Skip updating everything else */
        }

        pacman.move(board, deltaSeconds);

        /* Power mode */
        powerModeDuration -= deltaSeconds;
        powerModeDuration = Math.max(powerModeDuration, 0);

        if (powerModeDuration < EPSILON) {
            for (Ghost ghost : ghosts) {
                ghost.setGoal(pacman.getPosition());
            }
        } else {
            for (Ghost ghost : ghosts) {
                switch (ghost.getGhostType()) {
                    case RED -> ghost.setGoal(config.redGhostStartPosition());
                    case BLUE -> ghost.setGoal(config.blueGhostStartPosition());
                    case PINK -> ghost.setGoal(config.pinkGhostStartPosition());
                    case ORANGE -> ghost.setGoal(config.orangeGhostStartPosition());
                }
            }
        }

        /* updating and checking collisions with entities */
        for (Ghost ghost : ghosts) {
            ghost.move(board, deltaSeconds);
            if (EntityUtils.hasCollided(pacman, ghost)
                && powerModeDuration < EPSILON) {
                pacmanDied();
            } else if (EntityUtils.hasCollided(pacman, ghost)) {
                ghostDied(ghost);
            }
        }

        Position pacmanPos = pacman.getPosition();
        switch (board[pacmanPos.y()][pacmanPos.x()]) {
            case PELLET -> {
                numberOfItemsLeft--;
                score++;
                board[pacmanPos.y()][pacmanPos.x()] = TileType.EMPTY;
            }
            
            case POWER_PELLET -> {
                numberOfItemsLeft--;
                powerModeDuration = config.powerModeDuration();
                board[pacmanPos.y()][pacmanPos.x()] = TileType.EMPTY;
            }
            default -> {}
        }
    }

    /*************************************************************
     *                      HELPER FUNCTIONS                     *
     *************************************************************/

    /**
     * Handles Pacman's death by decrementing lives, resetting Pacman's position
     * and direction. It also resets the position of all ghosts.
     * Does not handle game over logic here.
     */
    private void pacmanDied() {
        lives--;
        timeOutDuration = 2.0;

        for (Ghost ghost : ghosts) {
            switch (ghost.getGhostType()) {
                case RED -> {
                    ghost.setPosition(config.redGhostStartPosition());
                    ghost.setSubTileX(0.5f);
                    ghost.setSubTileY(0.5f);
                }
                case BLUE -> {
                    ghost.setPosition(config.blueGhostStartPosition());
                    ghost.setSubTileX(0.5f);
                    ghost.setSubTileY(0.5f);
                }
                case PINK -> {
                    ghost.setPosition(config.pinkGhostStartPosition());
                    ghost.setSubTileX(0.5f);
                    ghost.setSubTileY(0.5f);
                }
                case ORANGE -> {
                    ghost.setPosition(config.orangeGhostStartPosition());
                    ghost.setSubTileX(0.5f);
                    ghost.setSubTileY(0.5f);
                }
            }
        }

        pacman.setPosition(config.pacmanStartPosition());
        pacman.setSubTileX(0.5f);
        pacman.setSubTileY(0.5f);
        pacman.setDirection(Direction.NONE);
    }

    /**
     * Handles the event when a ghost dies during power mode.
     * Resets the ghost's position based on its type and increments the score.
     *
     * @param ghost the ghost that died (must not be {@code null})
     */
    private void ghostDied(Ghost ghost) {

        score += 50;
        switch (ghost.getGhostType()) {
            case RED -> {
                ghost.setPosition(config.redGhostStartPosition());
                ghost.setSubTileX(0.5f);
                ghost.setSubTileY(0.5f);
            }
            case BLUE -> {
                ghost.setPosition(config.blueGhostStartPosition());
                ghost.setSubTileX(0.5f);
                ghost.setSubTileY(0.5f);
            }
            case PINK -> {
                ghost.setPosition(config.pinkGhostStartPosition());
                ghost.setSubTileX(0.5f);
                ghost.setSubTileY(0.5f);
            }
            case ORANGE -> {
                ghost.setPosition(config.orangeGhostStartPosition());
                ghost.setSubTileX(0.5f);
                ghost.setSubTileY(0.5f);
            }
        }
    }

    /**
     * Reset game to initial start state.
     */
    public void resetGame() {
        this.lives = config.lives();
        this.numberOfItemsLeft = config.numberOfItems();
        this.board = config.initialBoard();
        this.score = 0;
        pacman.setPosition(config.pacmanStartPosition());

        for (Ghost ghost : ghosts) {
            switch (ghost.getGhostType()) {
                case RED -> {
                    ghost.setPosition(config.redGhostStartPosition());
                    ghost.setSubTileX(0.5f);
                    ghost.setSubTileY(0.5f);
                }
                case BLUE -> {
                    ghost.setPosition(config.blueGhostStartPosition());
                    ghost.setSubTileX(0.5f);
                    ghost.setSubTileY(0.5f);
                }
                case PINK -> {
                    ghost.setPosition(config.pinkGhostStartPosition());
                    ghost.setSubTileX(0.5f);
                    ghost.setSubTileY(0.5f);
                }
                case ORANGE -> {
                    ghost.setPosition(config.orangeGhostStartPosition());
                    ghost.setSubTileX(0.5f);
                    ghost.setSubTileY(0.5f);
                }
            }
        }
    }

    /**
     * Checks if the player has won the game.
     * Winning occurs if the game is over and there are still lives left.
     *
     * @return {@code true} if the player has won, {@code false} otherwise
     */
    public boolean isWon() {
        return gameOver() && lives > 0;
    }

    /**
     * Checks if the game is over.
     * The game is over if all items are collected or if no lives remain.
     *
     * @return {@code true} if the game is over, {@code false} otherwise
     */
    public boolean gameOver() {
        return numberOfItemsLeft == 0 || lives <= 0;
    }

    /*************************************************************
     *                          GETTERS                          *
     *************************************************************/

    /**
     * Returns the current game board.
     *
     * @return the current board
     */
    public TileType[][] getBoard() {
        return this.board;
    }

    /**
     * Returns the current Pacman entity.
     *
     * @return the Pacman {@link Pacman}
     */
    public Pacman getPacman() {
        return this.pacman;
    }

    /**
     * Returns the list of ghost entities.
     *
     * @return list of all ghosts
     */
    public List<Ghost> getGhosts() {
        return this.ghosts;
    } 

    /**
     * Returns the current score.
     *
     * @return the current score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the number of remaining lives.
     *
     * @return the current remaining lives
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * Returns the remaining duration of the power mode.
     *
     * @return the power mode duration in seconds
     */
    public double getPowerModeDuration() {
        return this.powerModeDuration;
    }

    /*************************************************************
     *                          SETTERS                          *
     *************************************************************/

    /**
     * Sets the game board.
     *
     * @param newBoard the new board to set (must not be {@code null})
     * @throws IllegalArgumentException if {@code newBoard} is {@code null}
     */
    public void setBoard(TileType[][] newBoard) {
        if (newBoard == null) {
            throw new IllegalArgumentException("newBoard must not be null");
        }
        this.board = newBoard;
    }

    /**
     * Sets the Pacman entity.
     *
     * @param newPacman the new Pacman to set (must not be {@code null})
     * @throws IllegalArgumentException if {@code newPacman} is {@code null}
     */
    public void setPacman(Pacman newPacman) {
        if (newPacman == null) {
            throw new IllegalArgumentException("newPacman must not be null");
        }
        this.pacman = newPacman;
    }

    /**
     * Sets the current score.
     *
     * @param newScore the new score value
     */
    public void setScore(int newScore) {
        this.score = newScore;
    }

    /**
     * Sets the number of remaining lives.
     *
     * @param newLives the new number of lives
     */
    public void setLives(int newLives) {
        this.lives = newLives;
    }

    /**
     * Sets the list of ghost entities.
     *
     * @param newGhosts the new list of ghosts (must not be {@code null})
     * @throws IllegalArgumentException if {@code newGhosts} is {@code null}
     */
    public void setEntities(List<Ghost> newGhosts) {
        if (newGhosts == null) {
            throw new IllegalArgumentException("newGhost must not be null");
        }
        this.ghosts = newGhosts;
    }

    /**
     * Sets the remaining power mode duration.
     *
     * @param duration the new power mode duration in seconds
     */
    public void setPowerMode(double duration) {
        this.powerModeDuration = duration;
    }
}
