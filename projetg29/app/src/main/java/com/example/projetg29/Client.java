package com.example.projetg29;

public class Client extends Compte{

    public Client(String username, String password){
        super(username, password);
        setType("Client");
    }
}
