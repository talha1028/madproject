package com.example.madproject.models;

/**
 * ChatMessage Model
 *
 * Represents a single message in the AI chat
 */
public class ChatMessage {
    private String message;
    private boolean isUser;  // true = user message, false = AI message
    private long timestamp;

    // Empty constructor for Firestore (if you want to save chat history)
    public ChatMessage() {
    }

    public ChatMessage(String message, boolean isUser, long timestamp) {
        this.message = message;
        this.isUser = isUser;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}