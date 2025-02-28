package com.gamestore.dao;

import java.util.Collection;

/**
 * Generic interface for data access.
 * 
 * @param <T> Type of object the DAO handles
 * @param <ID> Type of the object's identifier
 */
public interface IDao<T, ID> {
    
    /**
     * Save an object (update if it already exists)
     * 
     * @param entity The object to save
     * @return The saved object
     */
    T save(T entity);
    
    /**
     * Delete an object by ID
     * 
     * @param id The ID of the object to delete
     * @return true if the object was deleted, false otherwise
     */
    boolean delete(ID id);
    
    /**
     * Retrieve an object by ID
     * 
     * @param id The ID of the object to retrieve
     * @return The retrieved object, or null if not found
     */
    T get(ID id);
    
    /**
     * Retrieve all objects
     * 
     * @return A collection of all objects
     */
    Collection<T> getAll();
    
    /**
     * Check if an object exists by ID
     * 
     * @param id The ID to check
     * @return true if the object exists, false otherwise
     */
    boolean exists(ID id);
}
