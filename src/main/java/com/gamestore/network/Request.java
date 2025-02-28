package com.gamestore.network;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a request from the client to the server.
 */
public class Request implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Map<String, String> headers;    // Request headers (including "action")
    private Object body;                    // Request body (the data being transferred)
    
    /**
     * Constructor with all fields
     * 
     * @param headers Request headers
     * @param body Request body
     */
    public Request(Map<String, String> headers, Object body) {
        this.headers = headers != null ? new HashMap<>(headers) : new HashMap<>();
        this.body = body;
    }
    
    /**
     * Default constructor
     */
    public Request() {
        this.headers = new HashMap<>();
        this.body = null;
    }
    
    /**
     * Constructor with action and body
     * 
     * @param action The action to perform
     * @param body Request body
     */
    public Request(String action, Object body) {
        this.headers = new HashMap<>();
        this.headers.put("action", action);
        this.body = body;
    }
    
    // Getters and Setters
    
    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }
    
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers != null ? new HashMap<>(headers) : new HashMap<>();
    }
    
    public Object getBody() {
        return body;
    }
    
    public void setBody(Object body) {
        this.body = body;
    }
    
    /**
     * Helper method to get the "action" header
     * 
     * @return The action, or null if not set
     */
    public String getAction() {
        return headers.get("action");
    }
    
    /**
     * Set the "action" header
     * 
     * @param action The action to set
     */
    public void setAction(String action) {
        headers.put("action", action);
    }
    
    /**
     * Add a header
     * 
     * @param key Header key
     * @param value Header value
     */
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
    
    /**
     * Get a header value
     * 
     * @param key Header key
     * @return Header value, or null if not set
     */
    public String getHeader(String key) {
        return headers.get(key);
    }
    
    @Override
    public String toString() {
        return "Request{" +
                "headers=" + headers +
                ", body=" + body +
                '}';
    }
}
