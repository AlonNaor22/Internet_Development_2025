package com.gamestore.dao;

import com.gamestore.model.Game;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of IDao for games using file persistence.
 */
public class GameFileDao implements IDao<Game, String> {
    
    private static final Logger LOGGER = Logger.getLogger(GameFileDao.class.getName());
    private static final String DEFAULT_FILE_PATH = "data/games.dat";
    
    private final String filePath;
    private Map<String, Game> gamesMap;
    
    /**
     * Constructor with custom file path
     * 
     * @param filePath Path to the save file
     */
    public GameFileDao(String filePath) {
        this.filePath = filePath;
        this.gamesMap = new HashMap<>();
        loadGames();
    }
    
    /**
     * Constructor using default path "data/games.dat"
     */
    public GameFileDao() {
        this(DEFAULT_FILE_PATH);
    }
    
    /**
     * Save a game (update if it already exists)
     * 
     * @param game The game to save
     * @return The saved game
     */
    @Override
    public Game save(Game game) {
        if (game == null || game.getName() == null) {
            throw new IllegalArgumentException("Game or game name cannot be null");
        }
        
        gamesMap.put(game.getName(), game);
        saveGames();
        return game;
    }
    
    /**
     * Delete a game by name
     * 
     * @param name The name of the game to delete
     * @return true if the game was deleted, false otherwise
     */
    @Override
    public boolean delete(String name) {
        if (name == null) {
            return false;
        }
        
        boolean removed = gamesMap.remove(name) != null;
        if (removed) {
            saveGames();
        }
        return removed;
    }
    
    /**
     * Retrieve a game by name
     * 
     * @param name The name of the game to retrieve
     * @return The retrieved game, or null if not found
     */
    @Override
    public Game get(String name) {
        return gamesMap.get(name);
    }
    
    /**
     * Retrieve all games
     * 
     * @return A collection of all games
     */
    @Override
    public Collection<Game> getAll() {
        return gamesMap.values();
    }
    
    /**
     * Check if a game exists by name
     * 
     * @param name The name to check
     * @return true if the game exists, false otherwise
     */
    @Override
    public boolean exists(String name) {
        return gamesMap.containsKey(name);
    }
    
    /**
     * Load games from file
     */
    private void loadGames() {
        File file = new File(filePath);
        
        // Ensure the directory exists
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                LOGGER.log(Level.WARNING, "Failed to create directory: {0}", directory.getPath());
            }
        }
        
        // If file doesn't exist, start with an empty map
        if (!file.exists()) {
            gamesMap = new HashMap<>();
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Game> loadedMap = (Map<String, Game>) obj;
                gamesMap = loadedMap;
            } else {
                LOGGER.log(Level.WARNING, "Loaded object is not a Map: {0}", obj.getClass().getName());
                gamesMap = new HashMap<>();
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.INFO, "No existing games file found. Starting with empty map.");
            gamesMap = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error loading games from file", e);
            gamesMap = new HashMap<>();
        }
    }
    
    /**
     * Save games to file
     */
    private void saveGames() {
        File file = new File(filePath);
        
        // Ensure the directory exists
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                LOGGER.log(Level.WARNING, "Failed to create directory: {0}", directory.getPath());
                return;
            }
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(gamesMap);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving games to file", e);
        }
    }
}
