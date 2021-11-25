package com.example.projetg29;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ClientActivity extends AppCompatActivity {
    Client compte;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        // Définir les éléments du layout
        TextView textView = findViewById(R.id.textView5);

        // Récuperer les informations de l'activité précédente
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("user");
        String password = extras.getString("password");

        compte = new Client(username, password);


        // Mettre à jour le texte
        textView.setText("Bienvenue "+compte.getUsername()+", vous êtes connecté en tant que Client");

    }
}