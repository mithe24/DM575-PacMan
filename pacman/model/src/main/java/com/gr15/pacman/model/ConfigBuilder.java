package com.gr15.pacman.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.gr15.pacman.model.Board.TileType;

/**
 * Utility class for building a {@link GameConfig} instance from a JSON file.
 * <p>
 * This class provides methods to parse a JSON configuration and convert it into
 * an instance of {@code GameConfig}, handling all necessary conversions and validations.
 */
public class ConfigBuilder {

    /**
     * Parses a JSON configuration file from an {@link InputStream},
     * and returns a {@link GameConfig} object.
     *
     * @param inputStream the input stream pointing to a valid JSON configuration file.
     * @return the constructed {@code GameConfig} instance.
     * @throws RuntimeException if the JSON is malformed
     *      or if an I/O error occurs during reading.
     */
    public static GameConfig fromJson(InputStream inputStream) {
        /* Notice the reader gets closed by
         * the try-with-resource statement */
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            return buildFromJsonObject(jsonObject);
        } catch (JSONException jsonException) {
            throw new RuntimeException("""
                Failed to parse JSON file, ill formated JSON file.""", jsonException);
        } catch (IOException ioException) {
            throw new RuntimeException("""
                Error reading JSON file. 
                Ensure the file exists and is accessible""", ioException);
        }
    }

    /**
     * Constructs a {@link GameConfig} object from a parsed {@link JSONObject}.
     *
     * @param jsonObject the JSON object containing all required game configuration fields.
     * @return the constructed {@code GameConfig} instance.
     * @throws RuntimeException if required fields are missing or have invalid types.
     */
    private static GameConfig buildFromJsonObject(JSONObject jsonObject) {
        try {
            TileType[][] initialBoard = parseBoard(jsonObject.getJSONArray("board"));

            /* Settings */
            int lives = jsonObject.getInt("lives");
            double powerModeDuration = jsonObject.getDouble("powerModeDuration");
            double ghostSpeed = jsonObject.getDouble("ghostSpeed");
            int numberOfItems = jsonObject.getInt("numberOfItems");

            /* Pacman Entity */
            JSONObject pacmanObject = jsonObject.getJSONObject("pacman");
            double pacmanSpeed = pacmanObject.getDouble("speed");
            Position pacmanStartPosition = new Position(pacmanObject.getInt("x"),
                pacmanObject.getInt("y"));

            /* Ghost Entities */
            /* Red Ghost */
            JSONObject redGhostObject = jsonObject.getJSONObject("redGhost");
            Position redGhostStartPosition = new Position(redGhostObject.getInt("x"),
                redGhostObject.getInt("y"));
            /* Blue Ghost */
            JSONObject blueGhostObject = jsonObject.getJSONObject("blueGhost");
            Position blueGhostStartPosition = new Position(blueGhostObject.getInt("x"),
                blueGhostObject.getInt("y"));

            /* Pink Ghost */
            JSONObject pinkGhostObject = jsonObject.getJSONObject("pinkGhost");
            Position pinkGhostStartPosition = new Position(pinkGhostObject.getInt("x"),
                pinkGhostObject.getInt("y"));

            /* Orange Ghost */
            JSONObject orangeGhostObject = jsonObject.getJSONObject("orangeGhost");
            Position orangeGhostStartPosition = new Position(orangeGhostObject.getInt("x"),
                orangeGhostObject.getInt("y"));

            return new GameConfig(
                initialBoard,
                powerModeDuration,
                lives,
                ghostSpeed,
                pacmanSpeed,
                numberOfItems,
                pacmanStartPosition,
                redGhostStartPosition,
                blueGhostStartPosition,
                pinkGhostStartPosition,
                orangeGhostStartPosition);
        } catch (JSONException jsonException) {
            throw new RuntimeException("""
                Missing requried config fields in JSON file.""", jsonException);
        }
    }

    /**
     * Parses a 2D array of {@link TileType} from a {@link JSONArray} representing the board.
     * <p>
     * Expected JSON tile symbols:
     * <ul>
     *   <li>"W" - Wall</li>
     *   <li>"E" - Empty</li>
     *   <li>"p" - Pellet</li>
     *   <li>"P" - Power Pellet</li>
     * </ul>
     *
     * @param jsonArray the JSON array representing the 2D board.
     * @return a 2D array of {@code TileType}.
     */
    private static TileType[][] parseBoard(JSONArray jsonArray) {
        assert jsonArray != null;

        TileType[][] tileBoard = new TileType[jsonArray.length()][jsonArray.getJSONArray(0).length()];
        for (int y = 0; y < tileBoard.length; y++) {
            JSONArray row = jsonArray.getJSONArray(y);
            for (int x = 0; x < row.length(); x++) {
                switch (row.getString(x)) {
                    case "W" -> tileBoard[y][x] = TileType.WALL;
                    case "E" -> tileBoard[y][x] = TileType.EMPTY;
                    case "p" -> tileBoard[y][x] = TileType.PELLET;
                    case "P" -> tileBoard[y][x] = TileType.POWER_PELLET;
                    default -> {}
                }
            }
        }

        return tileBoard;
    }
}
