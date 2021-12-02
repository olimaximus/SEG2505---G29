package com.example.projetg29;

import android.content.Intent;
import android.content.res.Resources;
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

import org.json.JSONException;

import java.util.LinkedList;

public class EmployeActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {


    private String heure_ouverture;
    private Service selected_service;
    //private EditText Nom_entrepreneur_edit;
    //private String Nom_entrepreneur;



    Employe compte;
    LinkedList<Service> servicesList;
    String[] services;
    Spinner dropdown_service;
    Button addService;
    Button removeService;
    TextView servicesChoisis;
    Button enregistrerHeures;

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
        String[] heuresPossibles = getResources().getStringArray(R.array.Heures_ouverture);




        // Définir les éléments du layout
        TextView textView = findViewById(R.id.textView7);
        //EditText serviceName = findViewById(R.id.editTextDate);
        addService = findViewById(R.id.btnChoisirService);
        removeService = findViewById(R.id.btnRetirerService);
        dropdown_service = findViewById(R.id.dropDown_Services);
        servicesChoisis = findViewById(R.id.textServicesChoisis);
        enregistrerHeures = findViewById(R.id.button5);





        // Récuperer les informations de l'activité précédente
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("user");
        String password = extras.getString("password");
        MyDBHandler dbHandler = new MyDBHandler(EmployeActivity.this);
        try {
            compte = dbHandler.findEmploye(username);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        // Mettre la bonne heure si le compte avait déja choisi des heures
        for(int i = 0; i < heuresPossibles.length; i++){
            if(compte.getHeures().equals(heuresPossibles[i])){
                spinner.setSelection(i);
            }
        }




        // Mettre à jour le texte
        textView.setText("Bienvenue "+compte.getUsername()+", vous êtes connecté en tant que Employé");

        // Créer la liste d'éléments du dropdown services
        DBServices dbServices = new DBServices(EmployeActivity.this);
        servicesList = dbServices.getAllServices();
        if(servicesList.size() == 0){
            servicesList.add(new Service());
        }
        updateServices();
        try {
            updateText();
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

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

        // Option du bouton enregistrerHeures
        enregistrerHeures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveHeures();
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        });


        // Fonction du bouton Ajouter service
        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    selectService();
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        });

        // Fonction du bouton retirer service
        removeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteService();
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        });


    }
    //Affichage des possibilite pour les heures d'ouverture
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        heure_ouverture = parent.getItemAtPosition(position).toString();
         //Toast.makeText(parent.getContext(),text,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    // Mettre à jour le dropdown services
    public void updateServices(){
        services = new String[servicesList.size()];
        for (int i = 0; i < services.length; i++) {
            services[i] = servicesList.get(i).getName();
        }
        ArrayAdapter<String> adapterservice = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, services);
        dropdown_service.setAdapter(adapterservice);
    }

    // Mettre à jour les services choisis par l'employé
    public void updateText() throws JSONException {
        String result = "Services sélectionnés:\n";
        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext());
        Employe newcompte = dbHandler.findEmploye(compte.getUsername());
        for(int i = 0; i < newcompte.getServicesList().size(); i++){
            result += "\n" + newcompte.getServicesList().get(i);
        }
        servicesChoisis.setText(result);
    }

    // Ajouter un service
    public void selectService() throws JSONException {
        if(selected_service.getName().equals("")){
            Toast.makeText(getApplicationContext(), "Veuillez d'abord choisir un service", Toast.LENGTH_LONG).show();
        }
        else if(compte.containsService(selected_service)){
            Toast.makeText(getApplicationContext(), "Ce service a déjà été ajouté", Toast.LENGTH_LONG).show();
        }
        else{
            compte.addService(selected_service);
            MyDBHandler dbHandler = new MyDBHandler(EmployeActivity.this);
            dbHandler.deleteCompte(compte.getUsername(), "Employé");
            dbHandler.addEmploye(compte);
            updateText();
            Toast.makeText(getApplicationContext(), "Service ajouté avec succès", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteService() throws JSONException {
        if(selected_service.getName().equals("")){
            Toast.makeText(getApplicationContext(), "Veuillez d'abord choisir un service", Toast.LENGTH_LONG).show();
        }
        else if(!compte.containsService(selected_service)){
            Toast.makeText(getApplicationContext(), "Ce service n'était pas choisi", Toast.LENGTH_LONG).show();
        }
        else{
            compte.deleteService(selected_service.getName());
            MyDBHandler dbHandler = new MyDBHandler(EmployeActivity.this);
            dbHandler.deleteCompte(compte.getUsername(), "Employé");
            dbHandler.addEmploye(compte);
            updateText();
            Toast.makeText(getApplicationContext(), "Service retiré avec succès", Toast.LENGTH_LONG).show();

        }
    }

    public void saveHeures() throws JSONException {
        compte.setHeures(heure_ouverture);
        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext());
        dbHandler.deleteCompte(compte.getUsername(), "Employé");
        dbHandler.addEmploye(compte);
        Toast.makeText(getApplicationContext(), compte.getHeures(), Toast.LENGTH_LONG).show();
    }
}



