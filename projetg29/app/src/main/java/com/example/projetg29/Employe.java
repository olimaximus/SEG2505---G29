package com.example.projetg29;

public class Employe extends Compte{

    public Employe(String username, String password){
        super(username, password);
        setType("Employé");
    }
}
