package com.gr15.pacman.view.screen;

import com.gr15.pacman.model.GameState;
import com.gr15.pacman.model.Board.TileType;
import com.gr15.pacman.model.entities.Entity;
import com.gr15.pacman.model.entities.Ghost;
import com.gr15.pacman.model.entities.Pacman;
import com.gr15.pacman.view.AnimatedSprite;
import com.gr15.pacman.view.ResourceManager;
import com.gr15.pacman.view.Sprite;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox; 
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
    private VBox root = new VBox();

    /** Canvas used to draw the game graphics. */
    private Canvas canvas;

    /*************************************************************
     *                        CONSTANTS                          *
     *************************************************************/

    /** Logical width of the canvas for consistent rendering. */
    private static final double LOGICAL_WIDTH = 1920;

    /** Logical width of the canvas for consistent rendering. */
    private static final double LOGICAL_HEIGHT = 1080;

    /** Reference to the current game state for rendering entities and board. */
    private static final int TILE_SIZE = 16; 

    /*************************************************************
     *                        UTILITIES                          *
     *************************************************************/

    /** Reference to the {@link ResourceManager} singleton instance. */
    private ResourceManager resourceManager = ResourceManager.getInstance();

    /** Reference to the current game state for rendering entities and board. */
    private final GameState gameState;

    /** Graphics context used for rendering on the {@link Canvas}. */
    private GraphicsContext gc;

    /** Affine transformation for simulating camera movement and zooming. */
    private Affine camara = new Affine();

    /** Current zoom factor. */
    private double currentZoom = 1;

    /*************************************************************
     *                          SPRITES                          *
     *************************************************************/

    /** Pacman sprite for rendering the player's character. */
    private AnimatedSprite pacman;

    private Sprite redGhost;
    private Sprite blueGhost;
    private Sprite pinkGhost;
    private Sprite orangeGhost;

    /**
     * Constructs a new {@code GameView} with the given game state.
     * Initializes the rendering canvas and adds it to the scene graph.
     *
     * @param gameState the current game state to be rendered
     */
    public GameView(GameState gameState) {
        this.gameState = gameState;

        canvas = new Canvas(LOGICAL_WIDTH, LOGICAL_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().add(canvas);
        getChildren().add(root);

        /* Setting up animated sprites */

        Image[] pacmanFrames = {
            resourceManager.getTexture("/gameAssets/pacman1.png"),
            resourceManager.getTexture("/gameAssets/pacman2.png"),
            resourceManager.getTexture("/gameAssets/pacman3.png")
        };
        pacman = new AnimatedSprite(pacmanFrames, 0, 0, TILE_SIZE, TILE_SIZE);
    }

    /*************************************************************
     *                          METHODS                          *
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

        for (Ghost ghost : gameState.getGhosts()) {
            /* Update each ghost sprite */
        }

        /* Rotating pacman sprite */
        Pacman pacmanEntity = gameState.getPacman();
        switch (pacmanEntity.getDirection()) {
            case UP -> pacman.setRotation(0);
            case DOWN -> pacman.setRotation(180);
            case LEFT -> pacman.setRotation(270);
            case RIGHT -> pacman.setRotation(90);
            default -> {}
        }
        pacman.update(deltaSeconds);
        renderEntity(pacmanEntity, pacman);
    }

    /**
     * Updates the camera transform based on Pacman's position,
     * centering the camera and applying zoom.
     */
    private void updateCamara() {
        camara.setToIdentity();
        double screenWidth = canvas.getWidth();
        double screenHeight = canvas.getHeight();
        double scaleX = (screenWidth / LOGICAL_WIDTH) * currentZoom;
        double scaleY = (screenHeight / LOGICAL_HEIGHT) * currentZoom;
        double scale = Math.min(scaleX, scaleY);

        /* Define center of camara */
        double centerX = gameState.getPacman().getPositionX() * TILE_SIZE;
        double centerY = gameState.getPacman().getPositionY() * TILE_SIZE;

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
        TileType[][] tileBoard = gameState.getBoard().getTileBoard();
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
        double spriteX = entity.getPositionX() * TILE_SIZE;
        double spriteY = entity.getPositionY() * TILE_SIZE;

        sprite.setX(spriteX - sprite.getWidth() / 2);
        sprite.setY(spriteY - sprite.getHeight()  / 2);

        sprite.render(gc);
    }

    /**
     * Adjusts the current zoom level of the camera.
     *
     * @param deltaZoom the amount to change the zoom level by
     */
    public void changeZoom(double deltaZoom) {
        currentZoom += deltaZoom;
    }

    /**
     * Called when this view becomes active. Requests focus for input handling.
     */
    @Override
    public void onEnter() {
        this.requestFocus();
    }
    
    /**
     * Called when this view is deactivated or switched away from.
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
    public VBox getRoot() {
        return root;
    }
}
