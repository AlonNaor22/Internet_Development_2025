package com.gamestore.network;

import com.gamestore.controller.ControllerFactory;
import com.gamestore.dao.GameFileDao;
import com.gamestore.dao.UserFileDao;
import com.gamestore.model.Game;
import com.gamestore.model.User;
import com.gamestore.service.GameService;
import com.gamestore.service.UserService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the server and listens for requests.
 */
public class Server implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static final int DEFAULT_THREAD_POOL_SIZE = 10;
    
    private final int port;
    private ServerSocket serverSocket;
    private boolean running;
    private final ControllerFactory controllerFactory;
    private final ExecutorService threadPool;
    
    private final GameService gameService;
    private final UserService userService;
    
    /**
     * Constructor
     * 
     * @param port Port on which the server listens
     */
    public Server(int port) {
        this.port = port;
        this.running = false;
        
        // Initialize DAOs
        GameFileDao gameDao = new GameFileDao();
        UserFileDao userDao = new UserFileDao();
        
        // Initialize services
        this.gameService = new GameService(gameDao);
        this.userService = new UserService(userDao);
        
        // Initialize controller factory
        this.controllerFactory = new ControllerFactory(userService, gameService);
        
        // Initialize thread pool
        this.threadPool = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        
        // Initialize sample data
        initializeData();
    }
    
    /**
     * Start the server and listen for requests
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            LOGGER.info("Server started on port " + port);
            
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    LOGGER.info("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                    
                    // Handle the request in a separate thread
                    HandleRequest handler = new HandleRequest(clientSocket, controllerFactory);
                    threadPool.execute(handler);
                    
                } catch (IOException e) {
                    if (running) {
                        LOGGER.log(Level.SEVERE, "Error accepting client connection", e);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error starting server", e);
        } finally {
            stop();
        }
    }
    
    /**
     * Stop the server
     */
    public void stop() {
        running = false;
        
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error closing server socket", e);
            }
        }
        
        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdown();
        }
        
        LOGGER.info("Server stopped");
    }
    
    /**
     * Initialize initial data
     */
    private void initializeData() {
        try {
            // Add some sample games if none exist
            if (gameService.getAllGames().isEmpty()) {
                List<String> rpgGenres = new ArrayList<>();
                rpgGenres.add("RPG");
                rpgGenres.add("Adventure");
                
                Game game1 = new Game("The Witcher 3", 50.0, rpgGenres, 
                        "An epic RPG with a compelling story and stunning visuals.", 18, 
                        "https://example.com/witcher3.jpg");
                
                List<String> fpsGenres = new ArrayList<>();
                fpsGenres.add("FPS");
                fpsGenres.add("Action");
                
                Game game2 = new Game("Call of Duty: Modern Warfare", 60.0, fpsGenres,
                        "A first-person shooter game with intense action and multiplayer modes.", 18,
                        "https://example.com/cod.jpg");
                
                List<String> platformerGenres = new ArrayList<>();
                platformerGenres.add("Platformer");
                platformerGenres.add("Adventure");
                
                Game game3 = new Game("Super Mario Odyssey", 30.0, platformerGenres,
                        "A 3D platformer featuring Mario on a globe-trotting adventure.", 7,
                        "https://example.com/mario.jpg");
                
                gameService.addGame(game1);
                gameService.addGame(game2);
                gameService.addGame(game3);
                
                LOGGER.info("Added sample games");
            }
            
            // Add a sample admin user if none exist
            if (userService.getAllUsers().isEmpty()) {
                User adminUser = new User("admin", "admin123", "Administrator", "admin@gamestore.com", 30);
                userService.addUser(adminUser);
                
                LOGGER.info("Added admin user");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing data", e);
        }
    }
    
    /**
     * Run the server
     */
    @Override
    public void run() {
        start();
    }
}
