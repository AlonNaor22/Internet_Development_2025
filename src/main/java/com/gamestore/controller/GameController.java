package com.gamestore.controller;

import com.gamestore.model.Game;
import com.gamestore.network.Response;
import com.gamestore.service.GameService;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller that handles game-related requests.
 */
public class GameController {
    
    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
    
    private final GameService gameService;
    
    /**
     * Constructor
     * 
     * @param gameService Game service
     */
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
    
    /**
     * Retrieve game details
     * 
     * @param name Game name
     * @return Response with game details
     */
    public Response getGame(String name) {
        try {
            if (name == null || name.isEmpty()) {
                return Response.error("Game name cannot be empty");
            }
            
            Game game = gameService.getGame(name);
            if (game == null) {
                return Response.error("Game not found");
            }
            
            return Response.success(game);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving game", e);
            return Response.error("An error occurred retrieving game: " + e.getMessage());
        }
    }
    
    /**
     * Retrieve all games
     * 
     * @return Response with all games
     */
    public Response getAllGames() {
        try {
            Collection<Game> games = gameService.getAllGames();
            return Response.success(games);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all games", e);
            return Response.error("An error occurred retrieving all games: " + e.getMessage());
        }
    }
    
    /**
     * Search games by description
     * 
     * @param query Search query
     * @return Response with search results
     */
    public Response searchGames(String query) {
        try {
            if (query == null || query.isEmpty()) {
                return Response.error("Search query cannot be empty");
            }
            
            List<Game> results = gameService.searchGamesByDescription(query);
            return Response.success(results);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching games", e);
            return Response.error("An error occurred searching games: " + e.getMessage());
        }
    }
    
    /**
     * Add a new game
     * 
     * @param game Game to add
     * @return Response with result
     */
    public Response addGame(Game game) {
        try {
            if (game == null) {
                return Response.error("Game cannot be null");
            }
            
            if (game.getName() == null || game.getName().isEmpty()) {
                return Response.error("Game name cannot be empty");
            }
            
            if (gameService.getGame(game.getName()) != null) {
                return Response.error("Game with this name already exists");
            }
            
            gameService.addGame(game);
            return Response.success("Game added successfully", game);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding game", e);
            return Response.error("An error occurred adding game: " + e.getMessage());
        }
    }
    
    /**
     * Update an existing game
     * 
     * @param game Game with updated details
     * @return Response with result
     */
    public Response updateGame(Game game) {
        try {
            if (game == null) {
                return Response.error("Game cannot be null");
            }
            
            if (game.getName() == null || game.getName().isEmpty()) {
                return Response.error("Game name cannot be empty");
            }
            
            if (gameService.getGame(game.getName()) == null) {
                return Response.error("Game not found");
            }
            
            gameService.updateGame(game);
            return Response.success("Game updated successfully", game);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating game", e);
            return Response.error("An error occurred updating game: " + e.getMessage());
        }
    }
    
    /**
     * Delete a game
     * 
     * @param name Game name
     * @return Response with result
     */
    public Response deleteGame(String name) {
        try {
            if (name == null || name.isEmpty()) {
                return Response.error("Game name cannot be empty");
            }
            
            if (gameService.getGame(name) == null) {
                return Response.error("Game not found");
            }
            
            boolean deleted = gameService.deleteGame(name);
            
            if (deleted) {
                return Response.success("Game deleted successfully");
            } else {
                return Response.error("Failed to delete game");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting game", e);
            return Response.error("An error occurred deleting game: " + e.getMessage());
        }
    }
}
