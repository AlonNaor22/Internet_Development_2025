package com.gamestore.dao;

import com.gamestore.model.User;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of IDao for users using file persistence.
 */
public class UserFileDao implements IDao<User, String> {
    
    private static final Logger LOGGER = Logger.getLogger(UserFileDao.class.getName());
    private static final String DEFAULT_FILE_PATH = "data/users.dat";
    
    private final String filePath;
    private Map<String, User> usersMap;
    
    /**
     * Constructor with custom file path
     * 
     * @param filePath Path to the save file
     */
    public UserFileDao(String filePath) {
        this.filePath = filePath;
        this.usersMap = new HashMap<>();
        loadUsers();
    }
    
    /**
     * Constructor using default path "data/users.dat"
     */
    public UserFileDao() {
        this(DEFAULT_FILE_PATH);
    }
    
    /**
     * Save a user (update if it already exists)
     * 
     * @param user The user to save
     * @return The saved user
     */
    @Override
    public User save(User user) {
        if (user == null || user.getUsername() == null) {
            throw new IllegalArgumentException("User or username cannot be null");
        }
        
        usersMap.put(user.getUsername(), user);
        saveUsers();
        return user;
    }
    
    /**
     * Delete a user by username
     * 
     * @param username The username of the user to delete
     * @return true if the user was deleted, false otherwise
     */
    @Override
    public boolean delete(String username) {
        if (username == null) {
            return false;
        }
        
        boolean removed = usersMap.remove(username) != null;
        if (removed) {
            saveUsers();
        }
        return removed;
    }
    
    /**
     * Retrieve a user by username
     * 
     * @param username The username of the user to retrieve
     * @return The retrieved user, or null if not found
     */
    @Override
    public User get(String username) {
        return usersMap.get(username);
    }
    
    /**
     * Retrieve all users
     * 
     * @return A collection of all users
     */
    @Override
    public Collection<User> getAll() {
        return usersMap.values();
    }
    
    /**
     * Check if a user exists by username
     * 
     * @param username The username to check
     * @return true if the user exists, false otherwise
     */
    @Override
    public boolean exists(String username) {
        return usersMap.containsKey(username);
    }
    
    /**
     * Load users from file
     */
    private void loadUsers() {
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
            usersMap = new HashMap<>();
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, User> loadedMap = (Map<String, User>) obj;
                usersMap = loadedMap;
            } else {
                LOGGER.log(Level.WARNING, "Loaded object is not a Map: {0}", obj.getClass().getName());
                usersMap = new HashMap<>();
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.INFO, "No existing users file found. Starting with empty map.");
            usersMap = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error loading users from file", e);
            usersMap = new HashMap<>();
        }
    }
    
    /**
     * Save users to file
     */
    private void saveUsers() {
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
            oos.writeObject(usersMap);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving users to file", e);
        }
    }
}
