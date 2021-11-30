package com.example.projetg29;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

public class EmployeActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private EditText heure_ouverture_edit;
    private String heure_ouverture;
    private Service selected_service;
    //private EditText Nom_entrepreneur_edit;
    //private String Nom_entrepreneur;



    Employe compte;
    LinkedList<Service> servicesList;
    String[] services;
    Spinner dropdown_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employe);


        //Spinner des heures
        // Text dans Values -> strings...Heures d'ouverture
        Spinner spinner = findViewById(R.id.planets_spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Heures_ouverture, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Définir les éléments du layout
        TextView textView = findViewById(R.id.textView7);
        //EditText serviceName = findViewById(R.id.editTextDate);
        Button addService = findViewById(R.id.button_addService);
        dropdown_service = findViewById(R.id.dropDown_Services);




        // Récuperer les informations de l'activité précédente
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("user");
        String password = extras.getString("password");
        MyDBHandler dbHandler = new MyDBHandler(EmployeActivity.this);
        compte = dbHandler.findEmploye(username);



        // Définir les éléments du layout
        /*Button creer = findViewById(R.id.button4);
        this.heure_ouverture_edit = findViewById(R.id.editTextDate);
        this.Nom_entrepreneur_edit = findViewById(R.id.editTextTextPersonName);*/


        // Mettre à jour le texte
        textView.setText("Bienvenue "+compte.getUsername()+", vous êtes connecté en tant que Employé");

        // Créer la liste d'éléments du dropdown services
        DBServices dbServices = new DBServices(EmployeActivity.this);
        servicesList = dbServices.getAllServices();
        if(servicesList.size() == 0){
            servicesList.add(new Service());
        }
        updateServices();

        // Élément sélectionné par le dropdown Service
        dropdown_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_service = servicesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Option du bouton voir les demandes
        Button button6 = findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), VoirDemandesClients.class);
                startActivity(otherActivity);
            }
        });

        // Option du bouton enregistrer
        /*creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer le nom d'utilisateur et le mot de passe
                heure_ouverture = heure_ouverture_edit.getText().toString();
                Nom_entrepreneur = Nom_entrepreneur_edit.getText().toString();


            }
        });*/


        // Option pour voir les services et les selectionner

    /* - Lire les service qui ont ete prealablement cree par l'administrateur
       - Selectionner un service



    */


    }
    //Affichage des possibilite pour les heures d'ouverture
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
         Toast.makeText(parent.getContext(),text,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Ajouter un string a la base des donnees
    /*public void signup(String string){
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
    }*/

    public void updateServices(){
        services = new String[servicesList.size()];
        for (int i = 0; i < services.length; i++) {
            services[i] = servicesList.get(i).getName();
        }
        ArrayAdapter<String> adapterservice = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, services);
        dropdown_service.setAdapter(adapterservice);
    }
}



