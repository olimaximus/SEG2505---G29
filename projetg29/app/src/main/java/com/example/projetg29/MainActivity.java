package com.example.projetg29;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Définir les éléments du layout
        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);


        // Chaque bouton transmet son type d'utilisateur à l'activité d'inscription, puis la démarre

        // Fonction du bouton Administrateur
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), InscriptionAdmin.class);
                otherActivity.putExtra("user_type", "administrateur");
                startActivity(otherActivity);
            }

        });

        // Fonction du bouton Employé
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(), InscriptionAdmin.class);
                otherActivity.putExtra("user_type", "employé");
                startActivity(otherActivity);
            }
        });

        // Fonction du bouton Client
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(), InscriptionAdmin.class);
                otherActivity.putExtra("user_type", "client");
                startActivity(otherActivity);
            }
        });
    }
}