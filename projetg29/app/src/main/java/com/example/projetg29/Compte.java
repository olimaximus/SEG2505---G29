package com.example.projetg29;

public class Compte {
    // Attributs
    private String username;
    private String password;
    private String type;

    // Constructeur
    public Compte(String username, String password){
        this.username = username;
        this.password = password;
        type = "Compte";
    }

    public Compte(){

    }
    public String getType(){
        return type;
    }

    // Accesseurs et Modificateurs
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) { this.type = type; }
}
