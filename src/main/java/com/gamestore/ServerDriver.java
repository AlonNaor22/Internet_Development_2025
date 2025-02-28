package com.gamestore;

import com.gamestore.network.Server;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class to run the server.
 */
public class ServerDriver {
    
    private static final Logger LOGGER = Logger.getLogger(ServerDriver.class.getName());
    private static final int DEFAULT_PORT = 34567;
    
    /**
     * Main method to start the server
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        try {
            int port = DEFAULT_PORT;
            
            // Check if port is provided as command line argument
            if (args.length > 0) {
                try {
                    port = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "Invalid port number: " + args[0] + ". Using default port: " + DEFAULT_PORT);
                }
            }
            
            LOGGER.info("Starting GameStore server on port " + port);
            
            // Create and start the server
            Server server = new Server(port);
            new Thread(server).start();
            
            LOGGER.info("Server started successfully");
            
            // Add shutdown hook to stop the server gracefully
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Shutting down server...");
                server.stop();
            }));
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error starting server", e);
        }
    }
}
