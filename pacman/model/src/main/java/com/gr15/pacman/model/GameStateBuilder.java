package com.gr15.pacman.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.gr15.pacman.model.Board.Direction;
import com.gr15.pacman.model.Board.TileType;
import com.gr15.pacman.model.entities.Entity;
import com.gr15.pacman.model.entities.Ghost;
import com.gr15.pacman.model.entities.Items;
import com.gr15.pacman.model.entities.Pacman;

/**
 * Utility class to build a {@link GameState} object from a JSON configuration.
 *
 * <p>
 * This class provides static methods to parse game data such as the board,
 * entities, and items from a JSON input stream and use it to initialize
 * a game state. It handles parsing logic and translates string representations
 * into model objects used by the game engine.
 * </p>
 */
public class GameStateBuilder {

    /**
     Parses a JSON input stream and constructs a {@link GameState} based on the configuration.
     * <p>
     * Expected JSON structure includes:
     * <ul>
     *  <li>{@code pacmanSpawnPoint}: an array of two integers 
     *      representing the X and Y coordinates.</li>
     *  <li>{@code board}: a 2D array of strings 
     *      representing the board tiles ("W" for wall, "E" for empty).</li>
     *  <li>{@code pacman}: a JSON object containing properties 
     *      such as {@code x}, {@code y}, {@code speed}, and {@code radius}.</li>
     * </ul>
     *
     * @param inputStream the input stream of the JSON file.
     * @return a {@link GameState} object built from the JSON configuration.
     * @throws RuntimeException if the JSON is invalid or the file cannot be read.
     */
    public static GameState fromJson(InputStream inputStream) {
        /* Reader gets closed automatically, 
         * because it's in the resources part of a try-with-resource statement.
         * The InputStream gets closed by Reader */
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            return buildFromJsonObject(jsonObject);
        } catch (JSONException jsonException) {
            throw new RuntimeException("""
                Failed to parse JSON content. 
                Please ensure the file contains valid JSON.""", jsonException);
        } catch (IOException ioException) {
            throw new RuntimeException("""
                Error reading JSON file. 
                Ensure the file exists and is accessible""", ioException);
        }
    }

    /**
     * Internal helper method that constructs a {@link GameState} object
     * from a given {@link JSONObject}.
     *
     * <p>
     * This method extracts the board layout, Pacman parameters, 
     * and initializes the entities and items.
     *
     * @param jsonObject the JSON object containing game configuration data.
     * @return a fully constructed {@link GameState} object.
     * @throws RuntimeException if required fields are missing or incorrectly formatted.

     */
    private static GameState buildFromJsonObject(JSONObject jsonObject) {
        try {
            JSONArray stringBoard = jsonObject.getJSONArray("board");
            TileType[][] tileBoard = new TileType
                [stringBoard.length()]
                [stringBoard.getJSONArray(0).length()];
            for (int y = 0; y < stringBoard.length(); y++) {
                JSONArray row = stringBoard.getJSONArray(y);
                for (int x = 0; x < row.length(); x++) {
                    switch (row.getString(x)) {
                        case "W" -> tileBoard[y][x] = TileType.WALL;
                        case "E" -> tileBoard[y][x] = TileType.EMPTY;
                        default -> {}
                    }
                }
            }
            Board board = new Board(tileBoard[0].length, tileBoard.length, tileBoard);
            
            JSONObject pacmanJsonObject = jsonObject.getJSONObject("pacman");
            double speed = pacmanJsonObject.getDouble("speed");
            double radius = pacmanJsonObject.getDouble("radius");
            Position pacmanStartPos = new Position(
                pacmanJsonObject.getInt("x"),
                pacmanJsonObject.getInt("y"));
            Pacman pacman = new Pacman(pacmanStartPos, speed, Direction.NONE, radius);

            ArrayList<Entity> entities = new ArrayList<>();
            Map<Position, Items> items = new HashMap<>();

            return new GameState(board, pacman, entities, items);
        } catch (JSONException jsonException) {
            throw new RuntimeException("Failed to parse GameState json", jsonException);
        }
    }
}
