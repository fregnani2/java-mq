package com.example.mq.model;

/**
 * Enum for client role
 * Contains the roles of the client
 */
public enum ClientRole {
    ADMIN("admin"),
    CLIENT("client");

    private String role;

    ClientRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
