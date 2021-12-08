package com.example.projetg29;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.util.LinkedList;

public class VoirDemandesClients extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner dropdown_service;
    private LinkedList<Service> servicesList;
    private Employe compte;
    private String[] services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_demandes_clients);
        //Afficher les demandes des clients
        //Dropdown pour afficher les demandes des clients
        dropdown_service = findViewById(R.id.dropDown_demandes);

        //Récupérer les informations de l'activité précédente
        Bundle extras = getIntent().getExtras();
        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext());
        try {
            compte = dbHandler.findEmploye(extras.getString("Employé"));
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        //Créer la liste du dropdown services
        DBServices dbServices = new DBServices(getApplicationContext());
        servicesList = dbServices.getAllServicesRemplis(compte);
        if(servicesList.size() == 0){
            servicesList.add(new Service());
        }

        updateServices();




        // Option du spinner
        //Spinner des confirmation
        // Text dans values -> strings... selection
        Spinner spinner = findViewById(R.id.planets_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Selection, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    
    //Option du bouton valider
    //A ecrire apres avoir fait les fonctionnalites du client




    // Mettre le dropdown services à jour
    public void updateServices(){
        services = new String[servicesList.size()];
        for (int i = 0; i < services.length; i++) {
            String name = servicesList.get(i).getName();
            String employe = servicesList.get(i).getEmploye();
            name = name.replace("_E"+employe+"_C"," (");
            services[i] = name + ")";
        }
        if(services[0].equals(")")){
            services[0] = "";
        }
        ArrayAdapter<String> adapterservice = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, services);
        dropdown_service.setAdapter(adapterservice);
    }


}


