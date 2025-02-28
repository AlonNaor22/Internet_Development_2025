package com.gamestore.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a user in the game store system.
 * This class is serializable to allow saving to a file.
 */
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Fields
    private String username;                    // Username (unique identifier)
    private String password;                    // Password
    private String fullName;                    // Full name
    private String email;                       // Email address
    private int age;                            // Age
    private Map<String, Game> gameLibrary;      // User's game library (key: game name)
    
    /**
     * Default constructor
     */
    public User() {
        this.gameLibrary = new HashMap<>();
    }
    
    /**
     * Constructor with all fields
     */
    public User(String username, String password, String fullName, String email, int age) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.gameLibrary = new HashMap<>();
    }
    
    // Getters and Setters
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    /**
     * Get a copy of the user's game library
     * @return A copy of the game library map
     */
    public Map<String, Game> getGameLibrary() {
        // Return a copy to prevent unwanted changes
        return new HashMap<>(gameLibrary);
    }
    
    /**
     * Set the user's game library
     * @param gameLibrary The new game library
     */
    public void setGameLibrary(Map<String, Game> gameLibrary) {
        this.gameLibrary = gameLibrary != null ? new HashMap<>(gameLibrary) : new HashMap<>();
    }
    
    /**
     * Add a game to the library
     * @param game The game to add
     */
    public void addGame(Game game) {
        if (game != null) {
            gameLibrary.put(game.getName(), game);
        }
    }
    
    /**
     * Remove a game from the library
     * @param gameName The name of the game to remove
     * @return true if the game was removed, false otherwise
     */
    public boolean removeGame(String gameName) {
        return gameLibrary.remove(gameName) != null;
    }
    
    /**
     * Check if a game exists in the library
     * @param gameName The name of the game to check
     * @return true if the game exists, false otherwise
     */
    public boolean hasGame(String gameName) {
        return gameLibrary.containsKey(gameName);
    }
    
    /**
     * Comparison based on username only
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }
    
    /**
     * Hash code based on username
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
    
    /**
     * Returns a string representing the user (without password)
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", gameLibrary size=" + gameLibrary.size() +
                '}';
    }
}
