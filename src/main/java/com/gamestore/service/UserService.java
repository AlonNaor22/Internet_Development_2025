package com.gamestore.service;

import com.gamestore.dao.IDao;
import com.gamestore.model.Game;
import com.gamestore.model.User;

import java.util.Collection;
import java.util.logging.Logger;

/**
 * Service that handles business logic related to users.
 */
public class UserService {
    
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    
    private final IDao<User, String> userDao;
    
    /**
     * Constructor
     * 
     * @param userDao DAO for users
     */
    public UserService(IDao<User, String> userDao) {
        this.userDao = userDao;
    }
    
    /**
     * Retrieve a user by username
     * 
     * @param username The username of the user to retrieve
     * @return The retrieved user, or null if not found
     */
    public User getUser(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }
        return userDao.get(username);
    }
    
    /**
     * Retrieve all users
     * 
     * @return A collection of all users
     */
    public Collection<User> getAllUsers() {
        return userDao.getAll();
    }
    
    /**
     * Add a new user
     * 
     * @param user The user to add
     * @throws IllegalArgumentException if the user is null or already exists
     */
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (userDao.exists(user.getUsername())) {
            throw new IllegalArgumentException("User with username '" + user.getUsername() + "' already exists");
        }
        
        userDao.save(user);
    }
    
    /**
     * Update an existing user
     * 
     * @param user The user to update
     * @throws IllegalArgumentException if the user is null or doesn't exist
     */
    public void updateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (!userDao.exists(user.getUsername())) {
            throw new IllegalArgumentException("User with username '" + user.getUsername() + "' does not exist");
        }
        
        userDao.save(user);
    }
    
    /**
     * Delete a user by username
     * 
     * @param username The username of the user to delete
     * @return true if the user was deleted, false otherwise
     */
    public boolean deleteUser(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return userDao.delete(username);
    }
    
    /**
     * Authenticate a user by username and password
     * 
     * @param username The username
     * @param password The password
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticate(String username, String password) {
        if (username == null || username.isEmpty() || password == null) {
            return false;
        }
        
        User user = userDao.get(username);
        return user != null && password.equals(user.getPassword());
    }
    
    /**
     * Add a game to a user's library
     * 
     * @param username The username of the user
     * @param game The game to add
     * @throws IllegalArgumentException if the user doesn't exist or the game is null
     */
    public void addGameToUserLibrary(String username, Game game) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        
        User user = userDao.get(username);
        if (user == null) {
            throw new IllegalArgumentException("User with username '" + username + "' does not exist");
        }
        
        user.addGame(game);
        userDao.save(user);
    }
    
    /**
     * Remove a game from a user's library
     * 
     * @param username The username of the user
     * @param gameName The name of the game to remove
     * @return true if the game was removed, false otherwise
     * @throws IllegalArgumentException if the user doesn't exist
     */
    public boolean removeGameFromUserLibrary(String username, String gameName) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        
        if (gameName == null || gameName.isEmpty()) {
            throw new IllegalArgumentException("Game name cannot be null or empty");
        }
        
        User user = userDao.get(username);
        if (user == null) {
            throw new IllegalArgumentException("User with username '" + username + "' does not exist");
        }
        
        boolean removed = user.removeGame(gameName);
        if (removed) {
            userDao.save(user);
        }
        return removed;
    }
}
