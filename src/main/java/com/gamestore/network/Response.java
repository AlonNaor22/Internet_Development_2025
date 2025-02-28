package com.gamestore.network;

import java.io.Serializable;

/**
 * Represents a response from the server to the client.
 */
public class Response implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int status;        // Status code (200 for success, 400 for error, etc.)
    private String message;    // Status message
    private Object data;       // Response data
    
    /**
     * Constructor with all fields
     * 
     * @param status Status code
     * @param message Status message
     * @param data Response data
     */
    public Response(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    
    /**
     * Default constructor
     */
    public Response() {
        this(200, "OK", null);
    }
    
    // Getters and Setters
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    /**
     * Create a success response with data
     * 
     * @param data Response data
     * @return A success response
     */
    public static Response success(Object data) {
        return new Response(200, "OK", data);
    }
    
    /**
     * Create a success response with message and data
     * 
     * @param message Status message
     * @param data Response data
     * @return A success response
     */
    public static Response success(String message, Object data) {
        return new Response(200, message, data);
    }
    
    /**
     * Create an error response with message
     * 
     * @param message Error message
     * @return An error response
     */
    public static Response error(String message) {
        return new Response(400, message, null);
    }
    
    /**
     * Create an error response with status and message
     * 
     * @param status Status code
     * @param message Error message
     * @return An error response
     */
    public static Response error(int status, String message) {
        return new Response(status, message, null);
    }
    
    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
