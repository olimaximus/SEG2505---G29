package com.example.projetg29;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

public class EmployeActivity extends AppCompatActivity {

    private EditText heure_ouverture_edit;
    private String heure_ouverture;
    private String Service;
    private EditText Nom_entrepreneur_edit;
    private String Nom_entrepreneur;



    Employe compte;
    LinkedList<Service> servicesList;
    String[] services;
    Spinner dropdown_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employe);

        // Définir les éléments du layout
        TextView textView = findViewById(R.id.textView5);
        Spinner dropdown = findViewById(R.id.dropDown_Services);
        EditText serviceName = findViewById(R.id.editTextDate);
        Button addService = findViewById(R.id.button_addService);
        dropdown_service = findViewById(R.id.dropDown_Services);
        Button creer = findViewById(R.id.button5);
        this.heure_ouverture_edit = findViewById(R.id.editTextDate);
        this.Nom_entrepreneur_edit = findViewById(R.id.editTextTextPersonName);

        if(servicesList == null) {
            servicesList = new LinkedList<>();
            servicesList.add(new Service());
        }
        else{
            //None

        }


        // Récuperer les informations de l'activité précédente
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("user");
        String password = extras.getString("password");

        compte = new Employe (username, password);





        // Mettre à jour le texte
        textView.setText("Bienvenue "+compte.getUsername()+", vous êtes connecté en tant que Employé");

        // Créer la liste d'éléments du dropdown services
        LinkedList<String> comptesList;
        MyDBHandler dbHandler = new MyDBHandler(this);
        comptesList = dbHandler.getAllServices();
        String[] comptes = new String[comptesList.size()/2];
        for(int i = 0; i < comptes.length; i++){
            String temp = comptesList.removeFirst();
            comptes[i] = temp+" ("+comptesList.removeFirst()+")";
        }

        // Option du bouton enregistrer
        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer le nom d'utilisateur et le mot de passe
                heure_ouverture = heure_ouverture_edit.getText().toString();
                Nom_entrepreneur = Nom_entrepreneur_edit.getText().toString();


            }
        });


        // Option pour voir les services et les selectionner



    }
    //Ajouter un string a la base des donnees
    /*
    public void signup(String string){

        MyDBHandler dbHandler = new MyDBHandler(this);
        dbHandler.addString(string); == null) {
        dbHandler.addString(string);
        Toast.makeText(getApplicationContext(), "Enregistrer", Toast.LENGTH_LONG).show();

        }
        Intent otherActivity = new Intent(getApplicationContext(), ClientActivity.class);
        // Transférer les informations à l'écran de bienvenue
        otherActivity.putExtra("user", compte.getUsername());
        otherActivity.putExtra("password", compte.getPassword());
        startActivity(otherActivity);
    }

     */
}



