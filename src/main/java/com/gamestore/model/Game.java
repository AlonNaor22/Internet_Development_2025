package com.gamestore.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a game in the game store.
 * This class is serializable to allow saving to a file.
 */
public class Game implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Fields
    private String name;            // Game name (unique identifier)
    private double sizeGB;          // Game size in gigabytes
    private List<String> genres;    // List of game genres
    private String description;     // Game description
    private int minAge;             // Minimum age to play the game
    private String imageUrl;        // Link to game image (optional)
    
    /**
     * Default constructor
     */
    public Game() {
        this.genres = new ArrayList<>();
    }
    
    /**
     * Constructor with all fields
     */
    public Game(String name, double sizeGB, List<String> genres, String description, int minAge, String imageUrl) {
        this.name = name;
        this.sizeGB = sizeGB;
        this.genres = genres != null ? new ArrayList<>(genres) : new ArrayList<>();
        this.description = description;
        this.minAge = minAge;
        this.imageUrl = imageUrl;
    }
    
    /**
     * Constructor with required fields
     */
    public Game(String name, double sizeGB, String description, int minAge) {
        this.name = name;
        this.sizeGB = sizeGB;
        this.genres = new ArrayList<>();
        this.description = description;
        this.minAge = minAge;
    }
    
    // Getters and Setters
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getSizeGB() {
        return sizeGB;
    }
    
    public void setSizeGB(double sizeGB) {
        this.sizeGB = sizeGB;
    }
    
    public List<String> getGenres() {
        return new ArrayList<>(genres); // Return a copy to prevent unwanted changes
    }
    
    public void setGenres(List<String> genres) {
        this.genres = genres != null ? new ArrayList<>(genres) : new ArrayList<>();
    }
    
    /**
     * Add a genre to the game
     * @param genre The genre to add
     */
    public void addGenre(String genre) {
        if (genre != null && !genre.isEmpty() && !genres.contains(genre)) {
            genres.add(genre);
        }
    }
    
    /**
     * Remove a genre from the game
     * @param genre The genre to remove
     */
    public boolean removeGenre(String genre) {
        return genres.remove(genre);
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getMinAge() {
        return minAge;
    }
    
    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    /**
     * Comparison based on game name only
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(name, game.name);
    }
    
    /**
     * Hash code based on game name
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    /**
     * Returns a string representing the game
     */
    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", sizeGB=" + sizeGB +
                ", genres=" + genres +
                ", description='" + description + '\'' +
                ", minAge=" + minAge +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
