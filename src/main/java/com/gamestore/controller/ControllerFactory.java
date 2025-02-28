package com.gamestore.controller;

import com.gamestore.service.GameService;
import com.gamestore.service.UserService;

import java.util.logging.Logger;

/**
 * Factory for creating appropriate controllers.
 */
public class ControllerFactory {
    
    private static final Logger LOGGER = Logger.getLogger(ControllerFactory.class.getName());
    
    private final UserService userService;
    private final GameService gameService;
    
    private final UserController userController;
    private final GameController gameController;
    
    /**
     * Constructor
     * 
     * @param userService User service
     * @param gameService Game service
     */
    public ControllerFactory(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
        
        // Create controllers
        this.userController = new UserController(userService, gameService);
        this.gameController = new GameController(gameService);
    }
    
    /**
     * Returns appropriate controller by type
     * 
     * @param type Controller type ("user", "game")
     * @return The controller
     * @throws IllegalArgumentException if the type is unknown
     */
    public Object getController(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Controller type cannot be empty");
        }
        
        switch (type.toLowerCase()) {
            case "user":
                return userController;
            case "game":
                return gameController;
            default:
                throw new IllegalArgumentException("Unknown controller type: " + type);
        }
    }
    
    /**
     * Get the user controller
     * 
     * @return The user controller
     */
    public UserController getUserController() {
        return userController;
    }
    
    /**
     * Get the game controller
     * 
     * @return The game controller
     */
    public GameController getGameController() {
        return gameController;
    }
}
