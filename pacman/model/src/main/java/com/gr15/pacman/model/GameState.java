package com.gr15.pacman.model;

import java.util.ArrayList;
import java.util.List;

import com.gr15.pacman.model.Board.TileType;
import com.gr15.pacman.model.entities.EntityUtils;
import com.gr15.pacman.model.entities.Ghost;
import com.gr15.pacman.model.entities.Pacman;
import com.gr15.pacman.model.entities.Entity.Direction;
import com.gr15.pacman.model.entities.Ghost.GhostType;

/**
 * Represents the current state of the game, including the game configuration,
 * board, entities (Pacman and ghosts), score, lives, and power mode status.
 * This class manages game updates, entity movements, collisions, and game progress.
 */
public class GameState {

    /** Configuration settings for the game such as initial board,
     * speeds, and start positions. */
    private final GameConfig config;

    /** The game board containing tile information such as walls and pellets. */
    private Board board;

    /** The Pacman entity controlled by the player. */
    private Pacman pacman;

    /** The list of ghost entities that act as AI opponents. */
    private List<Ghost> ghosts;

    /** The current score of the player. */
    private int score = 0;

    /** The remaining duration of power mode (in seconds),
     * during which Pacman can eat ghosts. */
    private double powerModeDuration = 0;

    /** The number of remaining lives for the player. */
    private int lives;

    /** The number of pellets and power pellets left on the board. */
    private int numberOfItemsLeft;

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
        this.board = new Board(config.initialBoard());
        this.pacman = new Pacman(config.pacmanStartPosition(), config.pacmanSpeed(), 0.5);

        this.ghosts = new ArrayList<>();
        this.ghosts.add(new Ghost(config.redGhostStartPosition(), config.ghostSpeed(),
            0.5, pacman, GhostType.RED));
        ghosts.add(new Ghost(config.blueGhostStartPosition(), config.ghostSpeed(),
            0.5, pacman, GhostType.BLUE));
        ghosts.add(new Ghost(config.pinkGhostStartPosition(), config.ghostSpeed(),
            0.5, pacman, GhostType.PINK));
        ghosts.add(new Ghost(config.orangeGhostStartPosition(), config.ghostSpeed(),
            0.5, pacman, GhostType.ORANGE));
    }

    /**
     * Updates the game state based on the elapsed time.
     * Moves Pacman and ghosts, handles power mode duration, detects collisions,
     * updates score, and processes pellet consumption.
     *
     * @param deltaSeconds the time elapsed since the last update (in seconds)
     */
    public void update(double deltaSeconds) {
        pacman.move(board, deltaSeconds);

        /* Power mode */
        powerModeDuration -= deltaSeconds;
        powerModeDuration = Math.max(powerModeDuration, 0);

        /* updating and checking collisions with entities */
        for (Ghost ghost : ghosts) {
            ghost.move(board, deltaSeconds);
            if (EntityUtils.hasCollided(pacman, ghost)
                && powerModeDuration < 0.03) {
                pacmanDied();
            } else if (EntityUtils.hasCollided(pacman, ghost)) {
                ghostDied(ghost);
            }
        }

        Position pacmanPos = pacman.getPosition();
        switch (board.getTile(pacmanPos.x(), pacmanPos.y())) {
            case PELLET -> {
                numberOfItemsLeft--;
                score++;
                board.setTile(pacmanPos.x(), pacmanPos.y(), TileType.EMPTY);
            }
            case POWER_PELLET -> {
                numberOfItemsLeft--;
                powerModeDuration = config.powerModeDuration();
                board.setTile(pacmanPos.x(), pacmanPos.y(), TileType.EMPTY);
            }
            default -> {}
        }
    }

    /**
     * Handles Pacman's death by decrementing lives, resetting Pacman's position
     * and direction. Does not handle game over logic here.
     */
    private void pacmanDied() {
        lives--;
        if (lives == 0) {
        }

        pacman.setPosition(config.pacmanStartPosition());
        pacman.setDirection(Direction.NONE);
    }

    /**
     * Handles the event when a ghost dies during power mode.
     * Resets the ghost's position based on its type and increments the score.
     *
     * @param ghost the ghost that died (must not be {@code null})
     */
    private void ghostDied(Ghost ghost) {
        assert ghost != null;

        score += 100;
        switch (ghost.getGhostType()) {
            case RED -> ghost.setPosition(config.redGhostStartPosition());
            case BLUE -> ghost.setPosition(config.blueGhostStartPosition());
            case PINK -> ghost.setPosition(config.pinkGhostStartPosition());
            case ORANGE -> ghost.setPosition(config.orangeGhostStartPosition());
        }
    }

    /**
     * Checks if the player has won the game.
     * Winning occurs if the game is over and there are still lives left.
     *
     * @return {@code true} if the player has won, {@code false} otherwise
     */
    public boolean isWon() {
        return GameOver() && lives != 0;
    }

    /**
     * Checks if the game is over.
     * The game is over if all items are collected or if no lives remain.
     *
     * @return {@code true} if the game is over, {@code false} otherwise
     */
    public boolean GameOver() {
        return numberOfItemsLeft == 0 || lives == 0;
    }

    /**
     * Returns the current game board.
     *
     * @return the current board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Returns the current Pacman entity.
     *
     * @return the Pacman
     */
    public Pacman getPacman() {
        return this.pacman;
    }

    /**
     * Returns the list of ghost entities.
     *
     * @return the ghosts
     */
    public List<Ghost> getGhosts() {
        return this.ghosts;
    } 

    /**
     * Returns the current score.
     *
     * @return the score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the number of remaining lives.
     *
     * @return the lives
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

    /**
     * Sets the game board.
     *
     * @param newBoard the new board to set (must not be {@code null})
     * @throws IllegalArgumentException if {@code newBoard} is {@code null}
     */
    public void setBoard(Board newBoard) {
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
