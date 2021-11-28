package com.example.projetg29;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

public class AdminActivity extends AppCompatActivity {
    Administrateur compte;
    String compte_a_supprimer;
    LinkedList<Service> servicesList;
    String[] services;
    Spinner dropdown_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Définir les éléments du layout
        TextView textView = findViewById(R.id.textView5);
        Spinner dropdown = findViewById(R.id.dropDown_comptes);
        Button supprimer = findViewById(R.id.supprimer_compte);
        dropdown_service = findViewById(R.id.dropDown_Services);
        EditText serviceName = findViewById(R.id.editServiceName);
        Button addService = findViewById(R.id.button_addService);
        if(servicesList == null) {
            servicesList = new LinkedList<>();
            servicesList.add(new Service());
        }
        else{

        }



        // Récuperer les informations de l'activité précédente
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("user");
        String password = extras.getString("password");

        compte = new Administrateur(username, password);

        // Créer la liste d'éléments du dropdown Compte
        LinkedList<String> comptesList;
        MyDBHandler dbHandler = new MyDBHandler(this);
        comptesList = dbHandler.getAllComptes();
        String[] comptes = new String[comptesList.size()/2];
        for(int i = 0; i < comptes.length; i++){
            String temp = comptesList.removeFirst();
            comptes[i] = temp+" ("+comptesList.removeFirst()+")";
        }

        // Créer un adapter pour le dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, comptes);
        dropdown.setAdapter(adapter);

        // Élément sélectionné par le dropdown
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String select = comptes[position];
                int i = select.indexOf(' ');
                compte_a_supprimer = select.substring(0, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Créer la liste d'éléments du dropdown Service
        updateServices();





        // Créer un adapter pour le dropdown Service


        // Élément sélectionné par le dropdown Service
        dropdown_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Service selectService = servicesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Mettre à jour le texte
        textView.setText("Bienvenue "+compte.getUsername()+", vous êtes connecté en tant que Administrateur");

        // Message de confirmation de suppression
        DialogInterface.OnClickListener confirm_delete = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dbHandler.deleteCompte(compte_a_supprimer);
                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        // Fonction du bouton supprimer
        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(compte_a_supprimer.equals("admin")){
                    Toast.makeText(getApplicationContext(),"Vous ne pouvez pas supprimer ce compte", Toast.LENGTH_LONG).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                    builder.setMessage("Voulez-vous vraiment supprimer " + compte_a_supprimer + "?").setPositiveButton("Oui", confirm_delete).setNegativeButton("Non", confirm_delete).show();
                }
            }
        });

        // Fonction du bouton addService
        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = serviceName.getText().toString();
                Service service = new Service(name);
                servicesList.add(service);
                updateServices();
            }
        });


    }
    public void updateServices(){

        services = new String[servicesList.size()];
        for (int i = 0; i < services.length; i++) {
            services[i] = servicesList.get(i).getName();
        }
        ArrayAdapter<String> adapterservice = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, services);
        dropdown_service.setAdapter(adapterservice);

    }





}
