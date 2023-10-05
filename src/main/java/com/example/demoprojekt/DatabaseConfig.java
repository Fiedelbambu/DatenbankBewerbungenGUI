package com.example.demoprojekt;

public class DatabaseConfig {
    private String username;
    private String password;

    // Konstruktor
    public DatabaseConfig(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Setter-Methode für den Benutzernamen
    public void setUsername(String username) {
        this.username = username;
    }

    // Setter-Methode für das Passwort
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter-Methode für den Benutzernamen
    public String getUsername() {
        return username;
    }

    // Getter-Methode für das Passwort
    public String getPassword() {
        return password;
    }
}
