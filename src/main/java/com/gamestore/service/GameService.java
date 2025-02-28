package com.gamestore.service;

import com.gamestore.dao.IDao;
import com.gamestore.model.Game;
import algorithm.IAlgoStringMatching;
import algorithm.StringMatcherFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service that handles business logic related to games.
 */
public class GameService {
    
    private static final Logger LOGGER = Logger.getLogger(GameService.class.getName());
    
    private final IDao<Game, String> gameDao;
    private IAlgoStringMatching stringMatcher;
    
    /**
     * Constructor
     * 
     * @param gameDao DAO for games
     */
    public GameService(IDao<Game, String> gameDao) {
        this.gameDao = gameDao;
        // Initialize with null, will be set when needed
        this.stringMatcher = null;
    }
    
    /**
     * Change the search algorithm
     * 
     * @param matcher The new search algorithm
     */
    public void setStringMatcher(IAlgoStringMatching matcher) {
        if (matcher != null) {
            this.stringMatcher = matcher;
        }
    }
    
    /**
     * Retrieve a game by name
     * 
     * @param name The name of the game to retrieve
     * @return The retrieved game, or null if not found
     */
    public Game getGame(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return gameDao.get(name);
    }
    
    /**
     * Retrieve all games
     * 
     * @return A collection of all games
     */
    public Collection<Game> getAllGames() {
        return gameDao.getAll();
    }
    
    /**
     * Add a new game
     * 
     * @param game The game to add
     * @throws IllegalArgumentException if the game is null or already exists
     */
    public void addGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        
        if (gameDao.exists(game.getName())) {
            throw new IllegalArgumentException("Game with name '" + game.getName() + "' already exists");
        }
        
        gameDao.save(game);
    }
    
    /**
     * Update an existing game
     * 
     * @param game The game to update
     * @throws IllegalArgumentException if the game is null or doesn't exist
     */
    public void updateGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        
        if (!gameDao.exists(game.getName())) {
            throw new IllegalArgumentException("Game with name '" + game.getName() + "' does not exist");
        }
        
        gameDao.save(game);
    }
    
    /**
     * Delete a game by name
     * 
     * @param name The name of the game to delete
     * @return true if the game was deleted, false otherwise
     */
    public boolean deleteGame(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        return gameDao.delete(name);
    }
    
    /**
     * Search games by description
     * 
     * @param query The search query
     * @return A list of games matching the query
     */
    public List<Game> searchGamesByDescription(String query) {
        if (query == null || query.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Simple search implementation without using the algorithm module
        // This will be replaced with the actual algorithm when the JAR is properly integrated
        List<Game> results = new ArrayList<>();
        Collection<Game> allGames = gameDao.getAll();
        
        for (Game game : allGames) {
            String description = game.getDescription();
            if (description != null && description.toLowerCase().contains(query.toLowerCase())) {
                results.add(game);
            }
        }
        
        return results;
    }
}
