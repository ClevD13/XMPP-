package com.example.chatproject.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String username;
    private String password;

    public LoggedInUser(String userId, String displayName, String username, String password) {
        this.userId = userId;
        this.displayName = displayName;
        this.username = username;
        this.password = password;
    }

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}