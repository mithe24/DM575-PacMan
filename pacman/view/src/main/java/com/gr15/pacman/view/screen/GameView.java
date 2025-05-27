package com.gr15.pacman.view.screen;

import com.gr15.pacman.model.GameState;
import com.gr15.pacman.model.GameState.TileType;
import com.gr15.pacman.model.entities.Entity;
import com.gr15.pacman.model.entities.Ghost;
import com.gr15.pacman.model.entities.Pacman;
import com.gr15.pacman.view.AnimatedSprite;
import com.gr15.pacman.view.ResourceManager;
import com.gr15.pacman.view.Sprite;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Affine;

/**
 * Represents the main in-game view where the gameplay takes place.
 * 
 * This class is responsible for rendering the game board, entities such as Pacman
 * and ghosts, and applying camera transformations. It uses a {@link Canvas} for rendering
 * with a fixed logical resolution.
 * This view extends {@link BaseView}.
 */
public class GameView
    extends BaseView {

    /** The root layout container for this view. */
    private BorderPane root = new BorderPane();

    /** {@link Canvas} used to draw the game graphics. */
    private Canvas canvas;

    /** {@link HBox} used as layout container for HUD */
    private HBox hudPanel = new HBox(20);

    /*************************************************************
     *                        CONSTANTS                          *
     *************************************************************/

    /** Target width of the {@link Canvas} for consistent rendering. */
    private static final double VIRTUAL_WIDTH = 500;

    /** Target width of the {@link Canvas} for consistent rendering. */
    private static final double VIRTUAL_HEIGHT = 400;

    /** Size of each tile in pixels. */
    private static final int TILE_SIZE = 16; 

    /*************************************************************
     *                        UTILITIES                          *
     *************************************************************/

    /** Reference to the {@link ResourceManager} singleton instance. */
    private ResourceManager resourceManager = ResourceManager.getInstance();

    /** Reference to the current {@link GameState} for rendering entities and board. */
    private final GameState gameState;

    /** Graphics context used for rendering on the {@link Canvas}. */
    private GraphicsContext gc;

    /** Affine transformation for simulating camera movement and zooming. */
    private Affine camara = new Affine();

    /** Current zoom factor. */
    private double currentZoom = 1;

    /** Boolean for keeping track of power mode. Needed for changing sprites */
    private boolean powerMode = false;

    /*************************************************************
     *                          SPRITES                          *
     *************************************************************/

    /** {@link Sprite} for rendering {@link Pacman}. */
    private AnimatedSprite pacman;

    /** {@link Sprite} for rendering red {@link Ghost}. */
    private Sprite redGhost = new Sprite(resourceManager
        .getTexture("/gameAssets/redGhost.png"),
        0, 0, TILE_SIZE, TILE_SIZE);

    /** {@link Sprite} for rendering blue {@link Ghost}. */
    private Sprite blueGhost = new Sprite(resourceManager
        .getTexture("/gameAssets/blueGhost.png"),
        0, 0, TILE_SIZE, TILE_SIZE);

    /** {@link Sprite} for rendering pink {@link Ghost}. */
    private Sprite pinkGhost = new Sprite(resourceManager
        .getTexture("/gameAssets/pinkGhost.png"),
        0, 0, TILE_SIZE, TILE_SIZE);

    /** {@link Sprite} for rendering orange {@link Ghost}. */
    private Sprite orangeGhost = new Sprite(resourceManager
        .getTexture("/gameAssets/orangeGhost.png"),
        0, 0, TILE_SIZE, TILE_SIZE);

    /*************************************************************
     *                       UI ELEMENTS                         *
     *************************************************************/

    /** {@link Label} for displaying game score. */
    private Label scoreLabel = new Label("Score: 0");

    /** {@link Label} for displaying game lives. */
    Label livesLabel = new Label("Lives: ");

    /** {@link Label} for displaying game time. */
    Label timeLabel = new Label("Time: ");
    /* To keep track of time */
    private double time = 0;

    /*************************************************************
     *                       CONSTRUCTOR                         *
     *************************************************************/

    /**
     * Constructs a new {@code GameView} with the given game state.
     * Initializes the rendering canvas and adds it to the scene graph.
     *
     * @param gameState the current game state to be rendered
     */
    public GameView(GameState gameState) {
        this.gameState = gameState;

        canvas = new Canvas(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        livesLabel.setTextFill(Color.WHITE);
        livesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        timeLabel.setTextFill(Color.WHITE);
        timeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        hudPanel.setStyle("-fx-background-color: red; -fx-padding: 10;");
        hudPanel.getChildren().addAll(scoreLabel, livesLabel, timeLabel);

        root.setCenter(canvas);
        root.setBottom(hudPanel);
        this.getChildren().add(root);

        /* Setting up animated sprites */
        Image[] pacmanFrames = {
            resourceManager.getTexture("/gameAssets/pacman1.png"),
            resourceManager.getTexture("/gameAssets/pacman2.png"),
            resourceManager.getTexture("/gameAssets/pacman3.png")
        };
        pacman = new AnimatedSprite(pacmanFrames, 0, 0, TILE_SIZE, TILE_SIZE);
    }

    /*************************************************************
     *                        RENDER LOOP                        *
     *************************************************************/

    /**
     * Renders the game frame. This includes clearing the screen,
     * updating the camera transformation, drawing the game board, and
     * rendering all game entities (e.g., Pacman and ghosts).
     *
     * @param deltaSeconds time since the last frame, used for animation timing
     */
    public void renderGame(double deltaSeconds) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        updateCamara();
        renderBoard();

        /* Updating hud */
        scoreLabel.setText("Score: " + gameState.getScore());
        livesLabel.setText("Lives: " + gameState.getLives());
        time += deltaSeconds;
        int integerTime = (int)Math.round(time);
        timeLabel.setText("Time: " + integerTime);

        /* Rotating pacman sprite */
        Pacman pacmanEntity = gameState.getPacman();
        switch (pacmanEntity.getDirection()) {
            case UP -> pacman.setRotation(0);
            case DOWN -> pacman.setRotation(180);
            case LEFT -> pacman.setRotation(270);
            case RIGHT -> pacman.setRotation(90);
            default -> {}
        }

        /* Updating animated sprite */
        pacman.update(deltaSeconds);
        renderEntity(pacmanEntity, pacman);

        for (Ghost ghost : gameState.getGhosts()) {
            switch (ghost.getGhostType()) {
                case RED -> renderEntity(ghost, redGhost);
                case BLUE -> renderEntity(ghost, blueGhost);
                case PINK -> renderEntity(ghost, pinkGhost);
                case ORANGE -> renderEntity(ghost, orangeGhost);
            }
        }

        /* Updating sprite texture if in power mode */
        if (gameState.getPowerModeDuration() > 0 && !powerMode) {
            powerMode = true;
            redGhost.setImage(resourceManager
                .getTexture("/gameAssets/scaredGhost.png"));
            blueGhost.setImage(resourceManager
                .getTexture("/gameAssets/scaredGhost.png"));
            pinkGhost.setImage(resourceManager
                .getTexture("/gameAssets/scaredGhost.png"));
            orangeGhost.setImage(resourceManager
                .getTexture("/gameAssets/scaredGhost.png"));
        } else if (gameState.getPowerModeDuration() == 0 && powerMode) {
            powerMode = false;
            redGhost.setImage(resourceManager
                .getTexture("/gameAssets/redGhost.png"));
            blueGhost.setImage(resourceManager
                .getTexture("/gameAssets/blueGhost.png"));
            pinkGhost.setImage(resourceManager
                .getTexture("/gameAssets/pinkGhost.png"));
            orangeGhost.setImage(resourceManager
                .getTexture("/gameAssets/orangeGhost.png"));
        }
    }

    /*************************************************************
     *                    HELPER FUNCTIONS                       *
     *************************************************************/

    /**
     * Updates the camera transform based on Pacman's position,
     * centering the camera and applying zoom.
     */
    private void updateCamara() {
        camara.setToIdentity();
        double screenWidth = canvas.getWidth();
        double screenHeight = canvas.getHeight();
        double scaleX = (screenWidth / VIRTUAL_WIDTH) * currentZoom;
        double scaleY = (screenHeight / VIRTUAL_HEIGHT) * currentZoom;
        double scale = Math.min(scaleX, scaleY);

        /* Define center of camara.
         * Could be changed to center on anything. */
        double centerX = gameState.getPacman().getX() * TILE_SIZE;
        double centerY = gameState.getPacman().getY() * TILE_SIZE;

        camara.appendTranslation(screenWidth / 2, screenHeight / 2);
        camara.appendScale(scale, scale);
        camara.appendTranslation(-centerX, -centerY);

        gc.setTransform(camara);
    }

    /**
     * Renders the game board based on tile types from the current game state.
     * Draws wall textures and leaves empty tiles blank.
     */
    private void renderBoard() {
        TileType[][] tileBoard = gameState.getBoard();
        for (int y = 0; y < tileBoard.length; y++) {
            for (int x = 0; x < tileBoard[y].length; x++) {
                TileType tile = tileBoard[y][x];
                double worldX = x * TILE_SIZE;
                double worldY = y * TILE_SIZE;

                switch (tile) {
                    case WALL -> {
                        gc.drawImage(ResourceManager.getInstance()
                            .getTexture("/gameAssets/wall.png"),
                            worldX, worldY, TILE_SIZE, TILE_SIZE
                        );
                    }
                    case PELLET -> {
                        gc.drawImage(ResourceManager.getInstance()
                            .getTexture("/gameAssets/food.png"),
                            worldX, worldY, TILE_SIZE, TILE_SIZE);
                    }
                    case POWER_PELLET -> {
                        gc.drawImage(ResourceManager.getInstance()
                            .getTexture("/gameAssets/powerFood.png"),
                            worldX, worldY, TILE_SIZE, TILE_SIZE);
                    }
                    case EMPTY -> {}
                }
            }
        }
    }

    /**
     * Renders a given entity (e.g., Pacman or ghost),
     * using its corresponding sprite.
     *
     * @param entity the game entity to render
     * @param sprite the sprite to use for rendering the entity
     */
    private void renderEntity(Entity entity, Sprite sprite) {
        double spriteX = entity.getX() * TILE_SIZE;
        double spriteY = entity.getY() * TILE_SIZE;

        sprite.setX(spriteX - sprite.getWidth() / 2);
        sprite.setY(spriteY - sprite.getHeight()  / 2);

        sprite.render(gc);
    }

    /*************************************************************
     *                          METHODS                          *
     *************************************************************/

    /**
     * Adjusts the current zoom level of the camera.
     *
     * @param deltaZoom the amount to change the zoom level by
     */
    public void changeZoom(double deltaZoom) {
        currentZoom = Math.max(currentZoom + deltaZoom, 1);
    }

    /**
     * Called when this view becomes active. Requests focus for input handling.
     */
    @Override
    public void onEnter() {
        this.requestFocus();
    }
    
    /**
     * Called when this view is switched away from.
     */
    @Override
    public void onExit() {
        /* No specific behavior on exit */
    }

    /**
     * Returns the root container of this view.
     *
     * @return the root VBox
     */
    public BorderPane getRoot() {
        return root;
    }
}
