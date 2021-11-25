package com.example.projetg29;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeActivity extends AppCompatActivity {
    Employe compte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employe);

        // Définir les éléments du layout
        TextView textView = findViewById(R.id.textView5);
        // Récuperer les informations de l'activité précédente
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("user");
        String password = extras.getString("password");

        compte = new Employe(username, password);


        // Mettre à jour le texte
        textView.setText("Bienvenue "+compte.getUsername()+", vous êtes connecté en tant que Employé");
    }
}
