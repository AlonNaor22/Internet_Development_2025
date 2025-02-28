package com.gamestore.controller;

import com.gamestore.model.Game;
import com.gamestore.model.User;
import com.gamestore.network.Response;
import com.gamestore.service.GameService;
import com.gamestore.service.UserService;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller that handles user-related requests.
 */
public class UserController {
    
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    
    private final UserService userService;
    private final GameService gameService;
    
    /**
     * Constructor
     * 
     * @param userService User service
     * @param gameService Game service
     */
    public UserController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }
    
    /**
     * Login to the system
     * 
     * @param username Username
     * @param password Password
     * @return Response with login result
     */
    public Response login(String username, String password) {
        try {
            if (username == null || username.isEmpty()) {
                return Response.error("Username cannot be empty");
            }
            
            if (password == null || password.isEmpty()) {
                return Response.error("Password cannot be empty");
            }
            
            boolean authenticated = userService.authenticate(username, password);
            if (authenticated) {
                User user = userService.getUser(username);
                // Don't include password in the response
                user.setPassword(null);
                return Response.success("Login successful", user);
            } else {
                return Response.error("Invalid username or password");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during login", e);
            return Response.error("An error occurred during login: " + e.getMessage());
        }
    }
    
    /**
     * Register a new user
     * 
     * @param user User to register
     * @return Response with registration result
     */
    public Response register(User user) {
        try {
            if (user == null) {
                return Response.error("User cannot be null");
            }
            
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                return Response.error("Username cannot be empty");
            }
            
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return Response.error("Password cannot be empty");
            }
            
            if (userService.getUser(user.getUsername()) != null) {
                return Response.error("Username already exists");
            }
            
            userService.addUser(user);
            
            // Don't include password in the response
            User registeredUser = userService.getUser(user.getUsername());
            registeredUser.setPassword(null);
            
            return Response.success("Registration successful", registeredUser);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during registration", e);
            return Response.error("An error occurred during registration: " + e.getMessage());
        }
    }
    
    /**
     * Retrieve user details
     * 
     * @param username Username
     * @return Response with user details
     */
    public Response getUser(String username) {
        try {
            if (username == null || username.isEmpty()) {
                return Response.error("Username cannot be empty");
            }
            
            User user = userService.getUser(username);
            if (user == null) {
                return Response.error("User not found");
            }
            
            // Don't include password in the response
            user.setPassword(null);
            
            return Response.success(user);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user", e);
            return Response.error("An error occurred retrieving user: " + e.getMessage());
        }
    }
    
    /**
     * Update user details
     * 
     * @param user User with updated details
     * @return Response with update result
     */
    public Response updateUser(User user) {
        try {
            if (user == null) {
                return Response.error("User cannot be null");
            }
            
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                return Response.error("Username cannot be empty");
            }
            
            User existingUser = userService.getUser(user.getUsername());
            if (existingUser == null) {
                return Response.error("User not found");
            }
            
            // If password is not provided, keep the existing one
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            }
            
            // Preserve the game library
            user.setGameLibrary(existingUser.getGameLibrary());
            
            userService.updateUser(user);
            
            // Don't include password in the response
            User updatedUser = userService.getUser(user.getUsername());
            updatedUser.setPassword(null);
            
            return Response.success("User updated successfully", updatedUser);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user", e);
            return Response.error("An error occurred updating user: " + e.getMessage());
        }
    }
    
    /**
     * Retrieve user's game library
     * 
     * @param username Username
     * @return Response with user's games
     */
    public Response getUserGames(String username) {
        try {
            if (username == null || username.isEmpty()) {
                return Response.error("Username cannot be empty");
            }
            
            User user = userService.getUser(username);
            if (user == null) {
                return Response.error("User not found");
            }
            
            return Response.success(user.getGameLibrary().values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user games", e);
            return Response.error("An error occurred retrieving user games: " + e.getMessage());
        }
    }
    
    /**
     * Add a game to user's library
     * 
     * @param username Username
     * @param gameName Game name
     * @return Response with result
     */
    public Response addGameToUser(String username, String gameName) {
        try {
            if (username == null || username.isEmpty()) {
                return Response.error("Username cannot be empty");
            }
            
            if (gameName == null || gameName.isEmpty()) {
                return Response.error("Game name cannot be empty");
            }
            
            User user = userService.getUser(username);
            if (user == null) {
                return Response.error("User not found");
            }
            
            Game game = gameService.getGame(gameName);
            if (game == null) {
                return Response.error("Game not found");
            }
            
            if (user.hasGame(gameName)) {
                return Response.error("User already has this game");
            }
            
            userService.addGameToUserLibrary(username, game);
            
            return Response.success("Game added to user's library", game);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding game to user", e);
            return Response.error("An error occurred adding game to user: " + e.getMessage());
        }
    }
    
    /**
     * Remove a game from user's library
     * 
     * @param username Username
     * @param gameName Game name
     * @return Response with result
     */
    public Response removeGameFromUser(String username, String gameName) {
        try {
            if (username == null || username.isEmpty()) {
                return Response.error("Username cannot be empty");
            }
            
            if (gameName == null || gameName.isEmpty()) {
                return Response.error("Game name cannot be empty");
            }
            
            User user = userService.getUser(username);
            if (user == null) {
                return Response.error("User not found");
            }
            
            if (!user.hasGame(gameName)) {
                return Response.error("User does not have this game");
            }
            
            boolean removed = userService.removeGameFromUserLibrary(username, gameName);
            
            if (removed) {
                return Response.success("Game removed from user's library");
            } else {
                return Response.error("Failed to remove game from user's library");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error removing game from user", e);
            return Response.error("An error occurred removing game from user: " + e.getMessage());
        }
    }
    
    /**
     * Get all users
     * 
     * @return Response with all users
     */
    public Response getAllUsers() {
        try {
            Collection<User> users = userService.getAllUsers();
            
            // Don't include passwords in the response
            for (User user : users) {
                user.setPassword(null);
            }
            
            return Response.success(users);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all users", e);
            return Response.error("An error occurred retrieving all users: " + e.getMessage());
        }
    }
}
