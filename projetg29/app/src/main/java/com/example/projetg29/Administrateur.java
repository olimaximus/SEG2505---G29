package com.example.projetg29;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Administrateur extends Compte{

    public Administrateur(String username, String password){
        super(username, password);
        setType("Administrateur");
    }

}