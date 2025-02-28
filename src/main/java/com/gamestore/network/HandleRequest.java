package com.gamestore.network;

import com.gamestore.controller.ControllerFactory;
import com.gamestore.controller.GameController;
import com.gamestore.controller.UserController;
import com.gamestore.model.Game;
import com.gamestore.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles a single request from a client.
 */
public class HandleRequest implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(HandleRequest.class.getName());
    
    private final Socket clientSocket;
    private final ControllerFactory controllerFactory;
    private final Gson gson;
    
    /**
     * Constructor
     * 
     * @param clientSocket Connection to the client
     * @param controllerFactory Controller factory
     */
    public HandleRequest(Socket clientSocket, ControllerFactory controllerFactory) {
        this.clientSocket = clientSocket;
        this.controllerFactory = controllerFactory;
        this.gson = new Gson();
    }
    
    /**
     * Handle the current request
     */
    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)
        ) {
            // Read the request JSON
            String requestJson = reader.readLine();
            LOGGER.info("Received request: " + requestJson);
            
            // Parse the request
            Request request = gson.fromJson(requestJson, Request.class);
            
            // Process the request
            Response response = processRequest(request);
            
            // Send the response
            String responseJson = gson.toJson(response);
            writer.println(responseJson);
            LOGGER.info("Sent response: " + responseJson);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling request", e);
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error closing client socket", e);
            }
        }
    }
    
    /**
     * Process request to response
     * 
     * @param request The request to process
     * @return The response
     */
    private Response processRequest(Request request) {
        try {
            if (request == null) {
                return Response.error("Invalid request: null");
            }
            
            String action = request.getAction();
            if (action == null || action.isEmpty()) {
                return Response.error("Invalid request: missing action");
            }
            
            // Parse the action (format: "controller/method")
            String[] parts = action.split("/");
            if (parts.length != 2) {
                return Response.error("Invalid action format: " + action);
            }
            
            String controllerType = parts[0];
            String methodName = parts[1];
            
            // Get the controller
            Object controller;
            try {
                controller = controllerFactory.getController(controllerType);
            } catch (IllegalArgumentException e) {
                return Response.error("Unknown controller: " + controllerType);
            }
            
            // Process based on controller type and method
            if (controllerType.equalsIgnoreCase("user")) {
                return processUserRequest((UserController) controller, methodName, request);
            } else if (controllerType.equalsIgnoreCase("game")) {
                return processGameRequest((GameController) controller, methodName, request);
            } else {
                return Response.error("Unsupported controller: " + controllerType);
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing request", e);
            return Response.error("Error processing request: " + e.getMessage());
        }
    }
    
    /**
     * Process a user-related request
     * 
     * @param controller The user controller
     * @param methodName The method to call
     * @param request The request
     * @return The response
     */
    private Response processUserRequest(UserController controller, String methodName, Request request) {
        try {
            Object body = request.getBody();
            JsonObject jsonBody = body != null ? JsonParser.parseString(gson.toJson(body)).getAsJsonObject() : null;
            
            switch (methodName.toLowerCase()) {
                case "login":
                    if (jsonBody == null) {
                        return Response.error("Missing login credentials");
                    }
                    String username = jsonBody.has("username") ? jsonBody.get("username").getAsString() : null;
                    String password = jsonBody.has("password") ? jsonBody.get("password").getAsString() : null;
                    return controller.login(username, password);
                    
                case "register":
                    if (jsonBody == null) {
                        return Response.error("Missing user data");
                    }
                    User user = gson.fromJson(jsonBody, User.class);
                    return controller.register(user);
                    
                case "getuser":
                    if (jsonBody == null) {
                        return Response.error("Missing username");
                    }
                    String getUserUsername = jsonBody.has("username") ? jsonBody.get("username").getAsString() : null;
                    return controller.getUser(getUserUsername);
                    
                case "updateuser":
                    if (jsonBody == null) {
                        return Response.error("Missing user data");
                    }
                    User updateUser = gson.fromJson(jsonBody, User.class);
                    return controller.updateUser(updateUser);
                    
                case "getusergames":
                    if (jsonBody == null) {
                        return Response.error("Missing username");
                    }
                    String getUserGamesUsername = jsonBody.has("username") ? jsonBody.get("username").getAsString() : null;
                    return controller.getUserGames(getUserGamesUsername);
                    
                case "addgametouser":
                    if (jsonBody == null) {
                        return Response.error("Missing data");
                    }
                    String addGameUsername = jsonBody.has("username") ? jsonBody.get("username").getAsString() : null;
                    String addGameName = jsonBody.has("gameName") ? jsonBody.get("gameName").getAsString() : null;
                    return controller.addGameToUser(addGameUsername, addGameName);
                    
                case "removegamefromuser":
                    if (jsonBody == null) {
                        return Response.error("Missing data");
                    }
                    String removeGameUsername = jsonBody.has("username") ? jsonBody.get("username").getAsString() : null;
                    String removeGameName = jsonBody.has("gameName") ? jsonBody.get("gameName").getAsString() : null;
                    return controller.removeGameFromUser(removeGameUsername, removeGameName);
                    
                case "getallusers":
                    return controller.getAllUsers();
                    
                default:
                    return Response.error("Unknown method: " + methodName);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing user request", e);
            return Response.error("Error processing user request: " + e.getMessage());
        }
    }
    
    /**
     * Process a game-related request
     * 
     * @param controller The game controller
     * @param methodName The method to call
     * @param request The request
     * @return The response
     */
    private Response processGameRequest(GameController controller, String methodName, Request request) {
        try {
            Object body = request.getBody();
            JsonObject jsonBody = body != null ? JsonParser.parseString(gson.toJson(body)).getAsJsonObject() : null;
            
            switch (methodName.toLowerCase()) {
                case "getgame":
                    if (jsonBody == null) {
                        return Response.error("Missing game name");
                    }
                    String gameName = jsonBody.has("name") ? jsonBody.get("name").getAsString() : null;
                    return controller.getGame(gameName);
                    
                case "getallgames":
                    return controller.getAllGames();
                    
                case "searchgames":
                    if (jsonBody == null) {
                        return Response.error("Missing search query");
                    }
                    String query = jsonBody.has("query") ? jsonBody.get("query").getAsString() : null;
                    return controller.searchGames(query);
                    
                case "addgame":
                    if (jsonBody == null) {
                        return Response.error("Missing game data");
                    }
                    Game addGame = gson.fromJson(jsonBody, Game.class);
                    return controller.addGame(addGame);
                    
                case "updategame":
                    if (jsonBody == null) {
                        return Response.error("Missing game data");
                    }
                    Game updateGame = gson.fromJson(jsonBody, Game.class);
                    return controller.updateGame(updateGame);
                    
                case "deletegame":
                    if (jsonBody == null) {
                        return Response.error("Missing game name");
                    }
                    String deleteGameName = jsonBody.has("name") ? jsonBody.get("name").getAsString() : null;
                    return controller.deleteGame(deleteGameName);
                    
                default:
                    return Response.error("Unknown method: " + methodName);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing game request", e);
            return Response.error("Error processing game request: " + e.getMessage());
        }
    }
}
