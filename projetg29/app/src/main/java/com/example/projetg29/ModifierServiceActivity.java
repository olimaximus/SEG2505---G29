package com.example.projetg29;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ModifierServiceActivity extends AppCompatActivity {
    String serviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_service);

        // Récupérer les infos de l'activité précédente
        Bundle extras = getIntent().getExtras();
        serviceName = extras.getString("Service");

        // Définir le text
        TextView titre = findViewById(R.id.text_service_modifier);
        titre.setText(serviceName);



    }
}