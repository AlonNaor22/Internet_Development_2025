# GameStore Project Documentation

## Purpose of this Document
This document contains all the necessary details for creating a computer game store project. The document is organized so that each developer can work on a specific file while understanding the required interfaces and dependencies. For each file, the document details the purpose, fields, methods, required classes to include, and dependencies.

## Project Architecture
The project is divided into two main parts:
1. **GameStore-Server** - Server side
2. **GameStore-Client** - Client side with JavaFX graphical user interface

Additionally, there is an algorithms module that is shared between both parts.

## Algorithms Module (algorithm-module-1.0-SNAPSHOT.jar)
Note: The algorithms module is already prepared as a JAR and should be added as a dependency to the server side. The module contains the following classes:

```
com.algorithm.IAlgoStringMatching
com.algorithm.AlgoRabinKarp
com.algorithm.AlgoBoyerMoore
com.algorithm.StringMatcherFactory
```

## Maven Project Configuration (Server Side)

### pom.xml 
**Description**: Maven configuration file for the server side.
**Purpose**: Define a Maven project, including dependencies and plugins.

**Dependencies to define**:
- com.google.code.gson:gson:2.10.1 - for working with JSON
- org.junit.jupiter:junit-jupiter:5.10.2 - for testing
- algorithm-module-1.0-SNAPSHOT.jar (as a local dependency) - for search algorithms

**Notes to the developer**:
- Add algorithm-module-1.0-SNAPSHOT.jar to the lib/ directory in the project root.
- Set Java 21 as source and target.
- Add Maven Assembly plugin to create a JAR with all dependencies.

## Server Side (GameStore-Server)

### Data Model

#### Game.java
**Package**: com.gamestore.model
**Description**: Represents a game in the game store.

**Fields**:
- String name - game name (unique identifier)
- double sizeGB - game size in gigabytes
- List<String> genres - list of game genres
- String description - game description
- int minAge - minimum age to play the game
- String imageUrl - link to game image (optional)

**Methods**:
- All getters and setters for the above fields
- addGenre(String genre) - add a genre to the game
- removeGenre(String genre) - remove a genre from the game
- equals(Object o) - comparison based on game name only
- hashCode() - returns hash code based on game name
- toString() - returns a string representing the game

**Interfaces to implement**: Serializable (java.io.Serializable)
**Notes to the developer**: The class needs to be Serializable to allow saving to a file.

#### User.java
**Package**: com.gamestore.model
**Description**: Represents a user in the game store system.

**Fields**:
- String username - username (unique identifier)
- String password - password
- String fullName - full name
- String email - email address
- int age - age
- Map<String, Game> gameLibrary - user's game library (key: game name)

**Methods**:
- All getters and setters for the above fields
- addGame(Game game) - add a game to the library
- removeGame(String gameName) - remove a game from the library
- hasGame(String gameName) - check if a game exists in the library
- equals(Object o) - comparison based on username only
- hashCode() - returns hash code based on username
- toString() - returns a string representing the user (without password)

**Interfaces to implement**: Serializable (java.io.Serializable)
**Notes to the developer**: The class needs to be Serializable to allow saving to a file. When returning gameLibrary, return a copy to prevent unwanted changes.

### DAO Layer (Data Access)

#### IDao.java
**Package**: com.gamestore.dao
**Description**: Generic interface for data access.

**Generic parameters**:
- T - type of object the DAO handles
- ID - type of the object's identifier

**Methods**:
- T save(T entity) - save an object (update if it already exists)
- boolean delete(ID id) - delete an object by ID
- T get(ID id) - retrieve an object by ID
- Collection<T> getAll() - retrieve all objects
- boolean exists(ID id) - check if an object exists by ID

**Notes to the developer**: This is a generic interface that will be implemented by specific DAOs.

#### UserFileDao.java
**Package**: com.gamestore.dao
**Description**: Implementation of IDao for users using file persistence.

**Fields**:
- String filePath - path to the save file
- Map<String, User> usersMap - memory cache of users

**Methods**:
- Constructors:
  - UserFileDao(String filePath) - constructor with custom file path
  - UserFileDao() - constructor using default path "data/users.dat"
- Implementation of all IDao methods
- private loadUsers() - load users from file
- private saveUsers() - save users to file

**Interfaces to implement**: IDao<User, String>
**Dependencies**:
- com.gamestore.model.User
- java.io.* (for file operations)
- java.util.logging.* (for logging)

**Notes to the developer**: Ensure the data directory exists before trying to read/write files.

#### GameFileDao.java
**Package**: com.gamestore.dao
**Description**: Implementation of IDao for games using file persistence.

**Fields**:
- String filePath - path to the save file
- Map<String, Game> gamesMap - memory cache of games

**Methods**:
- Constructors:
  - GameFileDao(String filePath) - constructor with custom file path
  - GameFileDao() - constructor using default path "data/games.dat"
- Implementation of all IDao methods
- private loadGames() - load games from file
- private saveGames() - save games to file

**Interfaces to implement**: IDao<Game, String>
**Dependencies**:
- com.gamestore.model.Game
- java.io.* (for file operations)
- java.util.logging.* (for logging)

**Notes to the developer**: Ensure the data directory exists before trying to read/write files.

### Service Layer

#### GameService.java
**Package**: com.gamestore.service
**Description**: Service that handles business logic related to games.

**Fields**:
- IDao<Game, String> gameDao - DAO for games
- IAlgoStringMatching stringMatcher - current search algorithm

**Methods**:
- GameService(IDao<Game, String> gameDao) - constructor
- setStringMatcher(IAlgoStringMatching matcher) - change the search algorithm
- Game getGame(String name) - retrieve a game by name
- Collection<Game> getAllGames() - retrieve all games
- void addGame(Game game) - add a new game
- void updateGame(Game game) - update an existing game
- void deleteGame(String name) - delete a game by name
- List<Game> searchGamesByDescription(String query) - search games by description

**Dependencies**:
- com.gamestore.dao.IDao
- com.gamestore.model.Game
- com.algorithm.IAlgoStringMatching
- com.algorithm.StringMatcherFactory

**Notes to the developer**: The searchGamesByDescription method should use StringMatcherFactory to choose the appropriate search algorithm based on query length.

#### UserService.java
**Package**: com.gamestore.service
**Description**: Service that handles business logic related to users.

**Fields**:
- IDao<User, String> userDao - DAO for users

**Methods**:
- UserService(IDao<User, String> userDao) - constructor
- User getUser(String username) - retrieve a user by username
- Collection<User> getAllUsers() - retrieve all users
- void addUser(User user) - add a new user
- void updateUser(User user) - update an existing user
- void deleteUser(String username) - delete a user by username
- boolean authenticate(String username, String password) - authenticate a user by username and password
- void addGameToUserLibrary(String username, Game game) - add a game to a user's library
- void removeGameFromUserLibrary(String username, String gameName) - remove a game from a user's library

**Dependencies**:
- com.gamestore.dao.IDao
- com.gamestore.model.User
- com.gamestore.model.Game

**Notes to the developer**: The authenticate method should check if the provided password matches the stored password.

### Controller Layer

#### Response.java
**Package**: com.gamestore.network
**Description**: Represents a response from the server to the client.

**Fields**:
- int status - status code (200 for success, 400 for error, etc.)
- String message - status message
- Object data - response data

**Methods**:
- Constructor with all fields
- All getters and setters
- static methods for creating common responses:
  - success(Object data)
  - success(String message, Object data)
  - error(String message)
  - error(int status, String message)

**Interfaces to implement**: Serializable (java.io.Serializable)
**Notes to the developer**: The data field should be serializable for network transmission.

#### Request.java
**Package**: com.gamestore.network
**Description**: Represents a request from the client to the server.

**Fields**:
- Map<String, String> headers - request headers (including "action")
- Object body - request body (the data being transferred)

**Methods**:
- Constructor with all fields
- All getters and setters
- getAction() - helper method to get the "action" header

**Interfaces to implement**: Serializable (java.io.Serializable)
**Notes to the developer**: The body field should be serializable for network transmission.

#### UserController.java
**Package**: com.gamestore.controller
**Description**: Controller that handles user-related requests.

**Fields**:
- UserService userService - user service

**Methods**:
- UserController(UserService userService) - constructor
- Response login(String username, String password) - login to the system
- Response register(User user) - register a new user
- Response getUser(String username) - retrieve user details
- Response updateUser(User user) - update user details
- Response getUserGames(String username) - retrieve user's game library
- Response addGameToUser(String username, String gameName) - add a game to library
- Response removeGameFromUser(String username, String gameName) - remove a game from library

**Dependencies**:
- com.gamestore.service.UserService
- com.gamestore.model.User
- com.gamestore.network.Response

**Notes to the developer**: The controller methods should handle exceptions and return appropriate Response objects.

#### GameController.java
**Package**: com.gamestore.controller
**Description**: Controller that handles game-related requests.

**Fields**:
- GameService gameService - game service

**Methods**:
- GameController(GameService gameService) - constructor
- Response getGame(String name) - retrieve game details
- Response getAllGames() - retrieve all games
- Response searchGames(String query) - search games by description

**Dependencies**:
- com.gamestore.service.GameService
- com.gamestore.model.Game
- com.gamestore.network.Response

**Notes to the developer**: The searchGames method should use the GameService's searchGamesByDescription method.

#### ControllerFactory.java
**Package**: com.gamestore.controller
**Description**: Factory for creating appropriate controllers.

**Fields**:
- UserService userService - user service
- GameService gameService - game service

**Methods**:
- ControllerFactory(UserService userService, GameService gameService) - constructor
- Object getController(String type) - returns appropriate controller by type ("user", "game")

**Dependencies**:
- com.gamestore.service.UserService
- com.gamestore.service.GameService
- com.gamestore.controller.UserController
- com.gamestore.controller.GameController

**Notes to the developer**: The getController method should throw an IllegalArgumentException for unknown controller types.

### Networking

#### HandleRequest.java
**Package**: com.gamestore.network
**Description**: Handles a single request from a client.

**Fields**:
- Socket clientSocket - connection to the client
- ControllerFactory controllerFactory - controller factory

**Methods**:
- HandleRequest(Socket clientSocket, ControllerFactory controllerFactory) - constructor
- void run() - handle the current request
- Response processRequest(Request request) - process request to response

**Interfaces to implement**: Runnable
**Dependencies**:
- java.net.Socket
- com.gamestore.controller.ControllerFactory
- com.gamestore.network.Request
- com.gamestore.network.Response
- com.google.gson.Gson (for JSON parsing)

**Notes to the developer**: This class should be run in a separate thread for each client connection.

#### Server.java
**Package**: com.gamestore.network
**Description**: Manages the server and listens for requests.

**Fields**:
- int port - port on which the server listens
- ServerSocket serverSocket - listening socket
- boolean running - whether the server is running
- ControllerFactory controllerFactory - controller factory
- ExecutorService threadPool - thread pool for handling client connections

**Methods**:
- Server(int port) - constructor
- void start() - start the server and listen for requests
- void stop() - stop the server
- void initializeData() - initialize initial data

**Dependencies**:
- java.net.ServerSocket
- java.net.Socket
- java.util.concurrent.ExecutorService
- com.gamestore.controller.ControllerFactory
- com.gamestore.dao.GameFileDao
- com.gamestore.dao.UserFileDao
- com.gamestore.service.GameService
- com.gamestore.service.UserService
- com.gamestore.network.HandleRequest

**Notes to the developer**: The server should use a thread pool to handle multiple client connections concurrently.

#### ServerDriver.java
**Package**: com.gamestore
**Description**: Main class to run the server.

**Methods**:
- public static void main(String[] args) - starts the server

**Dependencies**:
- com.gamestore.network.Server

**Notes to the developer**: This is the entry point for the server application.

## Client Side (GameStore-Client)

### Client Network

#### ServerConnector.java
**Package**: com.gamestore.client.network
**Description**: Handles communication with the server.

**Fields**:
- String serverAddress - server address
- int serverPort - server port
- Gson gson - for JSON serialization/deserialization

**Methods**:
- ServerConnector(String serverAddress, int serverPort) - constructor
- Response sendRequest(Request request) - send a request and receive a response
- void disconnect() - disconnect from the server

**Dependencies**:
- com.gamestore.network.Request
- com.gamestore.network.Response
- com.google.gson.Gson

**Notes to the developer**: This class should handle network communication with the server, including connection, sending requests, and receiving responses.

#### GameStoreClient.java
**Package**: com.gamestore.client
**Description**: Main class managing the client side.

**Fields**:
- ServerConnector connector - server connector
- User currentUser - currently logged in user

**Methods**:
- GameStoreClient(String serverAddress, int serverPort) - constructor
- boolean login(String username, String password) - attempt to login
- boolean register(User user) - register a new user
- boolean updateUser(User user) - update user details
- List<Game> getUserGames() - retrieve user's games
- Game getGame(String name) - retrieve game details
- List<Game> searchGames(String query) - search games
- boolean addGameToLibrary(String gameName) - add a game to library
- boolean removeGameFromLibrary(String gameName) - remove a game from library
- void logout() - logout from the system

**Dependencies**:
- com.gamestore.client.network.ServerConnector
- com.gamestore.model.User
- com.gamestore.model.Game
- com.gamestore.network.Request
- com.gamestore.network.Response

**Notes to the developer**: This class serves as a facade for client-side operations, handling communication with the server and managing the current user session.

### User Interface (UI)

#### LoginWindow.java
**Package**: com.gamestore.client.ui
**Description**: Login window.

**Components**:
- Text fields for username and password
- Buttons for login and navigation to registration

**Actions**:
- Validate login credentials with the server
- Navigate to main window or display error message

**Dependencies**:
- javafx.scene.*
- com.gamestore.client.GameStoreClient

**Notes to the developer**: Use JavaFX for creating the UI components. Handle login errors appropriately.

#### RegisterWindow.java
**Package**: com.gamestore.client.ui
**Description**: User registration window.

**Components**:
- Text fields for all user details (username, password, full name, email, age)
- Buttons for registration and return to login window

**Actions**:
- Validate input
- Register the user with the server
- Navigate to main window or display error message

**Dependencies**:
- javafx.scene.*
- com.gamestore.client.GameStoreClient
- com.gamestore.model.User

**Notes to the developer**: Implement input validation before sending data to the server.

#### MainWindow.java
**Package**: com.gamestore.client.ui
**Description**: Main window (displayed after login).

**Components**:
- Buttons for the three options (view library, download game, change details)
- Display of logged-in user details
- Logout button

**Actions**:
- Navigate to appropriate windows based on user selection

**Dependencies**:
- javafx.scene.*
- com.gamestore.client.GameStoreClient
- com.gamestore.model.User

**Notes to the developer**: This is the central hub for the application after login.

#### UserLibraryWindow.java
**Package**: com.gamestore.client.ui
**Description**: User's game library window.

**Components**:
- List of user's games
- Buttons for launching a game and deleting from library

**Actions**:
- Display games from the server
- Handle clicks on games
- Show image window for game launch
- Delete game from library

**Dependencies**:
- javafx.scene.*
- com.gamestore.client.GameStoreClient
- com.gamestore.model.Game

**Notes to the developer**: Use a ListView or TableView to display the games.

#### GameSearchWindow.java
**Package**: com.gamestore.client.ui
**Description**: Game search window.

**Components**:
- Text field for search
- Search button
- Display of search results

**Actions**:
- Send search request to server (automatically choose algorithm based on search length)
- Display search results
- Option to view game details and add to library

**Dependencies**:
- javafx.scene.*
- com.gamestore.client.GameStoreClient
- com.gamestore.model.Game

**Notes to the developer**: Implement responsive search results display, possibly with pagination for large result sets.

#### UserEditWindow.java
**Package**: com.gamestore.client.ui
**Description**: User details editing window.

**Components**:
- Text fields for editing user details (password, full name, email, age)
- Update details button

**Actions**:
- Validate input
- Send update request to server

**Dependencies**:
- javafx.scene.*
- com.gamestore.client.GameStoreClient
- com.gamestore.model.User

**Notes to the developer**: Implement input validation before sending data to the server.

#### GameDetailsWindow.java
**Package**: com.gamestore.client.ui
**Description**: Game details display window.

**Components**:
- Display of all game details
- Button for adding to library

**Actions**:
- Display selected game details
- Send request to add game to library when button is clicked

**Dependencies**:
- javafx.scene.*
- com.gamestore.client.GameStoreClient
- com.gamestore.model.Game

**Notes to the developer**: Design an attractive layout to display all game information.

#### ClientDriver.java
**Package**: com.gamestore.client
**Description**: Entry point for the application.

**Methods**:
- public static void main(String[] args) - initializes the application and displays the login window

**Dependencies**:
- javafx.application.Application
- com.gamestore.client.ui.LoginWindow
- com.gamestore.client.GameStoreClient

**Notes to the developer**: This is the entry point for the client application.

## Summary of Technologies

- Java - main programming language
- JavaFX - for graphical user interface
- Socket Programming - for Client/Server communication
- JSON (Gson) - for data representation in communication
- Strategy Pattern - for managing string search algorithms
- DAO Pattern - for data access
- MVC Pattern - for separating business logic from interface
- Factory Pattern - for creating controllers

The application is organized in a modular way and demonstrates advanced OOP principles such as Encapsulation, Inheritance, Polymorphism, and Abstraction, as well as implementing various Design Patterns to address different design challenges.
