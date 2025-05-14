package com.gr15.pacman.view.screen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gr15.pacman.view.ResourceManager;
import com.gr15.pacman.view.Sprite;

import com.gr15.pacman.model.GameState;
import com.gr15.pacman.model.Board.TileType;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import com.gr15.pacman.model.entities.Entity;
import com.gr15.pacman.model.entities.Items;

/**
 * GameView
 */
public class GameView
    extends VBox {

    /* Utils */
    private Canvas canvas;
    private GraphicsContext gc;
    private GameState gameState;

    private float scaleX = 3.0f;
    private float scaleY = 3.0f;
    private int tileWidth;
    private int tileHeight;

    private Map<TileType, Image> tileTextures = new HashMap<>();
    private Map<Entity, Image> entityTextures = new HashMap<>();
    private Map<Items, Image> itemTextures = new HashMap<>();

    /* UI elements */
    private Sprite pacmanSprite;

    public GameView(GameState gameState, int tileWidth, int tileHeight) {
        canvas = new Canvas(tileWidth * 16 * scaleX, tileHeight * 16 * scaleY);
        gc = canvas.getGraphicsContext2D();
        getChildren().add(canvas);
        setAlignment(Pos.CENTER);

        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        this.gameState = gameState;
        
        tileTextures.put(TileType.WALL, ResourceManager.getInstance()
            .getTexture("/gameAssets/wall.png"));
        pacmanSprite = new Sprite(
            ResourceManager.getInstance().getTexture("/gameAssets/pacmanRight.png"),
            gameState.getPacman().getPositionX() - 0.5, 
            gameState.getPacman().getPositionY() - 0.5, 
            16 * scaleX, 16 * scaleY);
    }

    public void renderGame(double deltaSeconds) {
        gc.clearRect(0, 0, tileWidth * 16 * scaleX, tileHeight * 16 * scaleY);
        TileType[][] tileBoard = gameState.getBoard().getTileBoard();
        for (int y = 0; y < tileBoard.length; y++) {
            for (int x = 0; x < tileBoard[y].length; x++) {
                Image texture = tileTextures.get(tileBoard[y][x]);
                if (texture != null) {
                    gc.drawImage(texture, 
                        x * 16 * scaleX, 
                        y * 16 * scaleY,
                        texture.getWidth() * scaleX,
                        texture.getHeight() * scaleY);
                }
            }
        }

        pacmanSprite.setX((gameState.getPacman().getPositionX() 
            - 0.5) * 16 * scaleX);
        pacmanSprite.setY((gameState.getPacman().getPositionY() 
            - 0.5) * 16 * scaleY);
        pacmanSprite.render(gc);

        List<Entity> entities = gameState.getEntities();
        for (Entity entity : entities) {
            Image texture = entityTextures.get(entity);
            if (texture != null) {
                gc.drawImage(texture, 
                    entity.getPositionX() * 16 * scaleX, 
                    entity.getPositionY() * 16 * scaleY,
                    texture.getWidth() * scaleX,
                    texture.getHeight() * scaleY);
            }
        }

        gameState.getItems().forEach((pos, item) -> {
            Image texture = itemTextures.get(item);
            if (texture != null) {
                gc.drawImage(texture, 
                    pos.x() * 16 * scaleX, 
                    pos.y() * 16 * scaleY,
                    texture.getWidth() * scaleX,
                    texture.getHeight() * scaleY);
            }
        });
    }

    public void requestFocusForInput() {
        requestFocus();
    }
}
